package com.ssafy.ar

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.google.ar.core.Camera
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.TrackingFailureReason
import com.ssafy.ar.data.QuestStatus
import com.ssafy.ar.data.TrackingMessage
import com.ssafy.ar.dummy.scriptNode
import com.ssafy.ar.manager.ARLocationManager
import com.ssafy.ar.manager.ARNodeManager
import com.ssafy.ar.util.MultiplePermissionsHandler
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.isTrackingPlane
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.node.ImageNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlinx.coroutines.launch

private const val TAG = "ArScreen"

@Composable
fun ARSceneComposable(
    onPermissionDenied: () -> Unit
) {
    // Screen Size
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val density = LocalDensity.current
    val widthPx = with(density) { screenWidth.toPx() }.toInt()
    val heightPx = with(density) { screenHeight.toPx() }.toInt()

    // LifeCycle
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Permission
    var hasPermission by remember { mutableStateOf(false) }

    // AR Basic
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)
    val childNodes = rememberNodes()
    val cameraNode = rememberARCameraNode(engine)
    var planeRenderer by remember { mutableStateOf(false) }
    var trackingFailureReason by remember { mutableStateOf<TrackingFailureReason?>(null) }

    // Manager
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationManager = remember { ARLocationManager(context, fusedLocationClient) }
    val nodeManager = remember { ARNodeManager(engine, modelLoader, materialLoader) }

    val viewModel: ARViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ARViewModel(nodeManager, locationManager) as T
        }
    })

    // AR State
    val questNodes by viewModel.questNodes.collectAsState()
    val nearestNPCInfo by viewModel.nearestNPCInfo.collectAsState()
    val currentLocation by locationManager.currentLocation.collectAsState()

    // Dialog & SnackBar
    val showDialog by viewModel.showDialog.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    MultiplePermissionsHandler(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) { permissionResults ->
        if (permissionResults.all { permissions -> permissions.value }) {
            hasPermission = true

            locationManager.startLocationUpdates()

            viewModel.getAllNpcMarker()
        } else {
            hasPermission = false

            onPermissionDenied()

            return@MultiplePermissionsHandler
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            view = view,
            modelLoader = modelLoader,
            collisionSystem = collisionSystem,
            sessionConfiguration = { session, config ->
                config.depthMode =
                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        true -> Config.DepthMode.AUTOMATIC
                        else -> Config.DepthMode.DISABLED
                    }
                config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                config.lightEstimationMode = Config.LightEstimationMode.DISABLED
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
                config.focusMode = Config.FocusMode.AUTO
//                config.geospatialMode = Config.GeospatialMode.ENABLED
            },
            cameraNode = cameraNode,
            planeRenderer = planeRenderer,
            onTrackingFailureChanged = { trackingFailureReason = it },
            onSessionUpdated = { session, frame ->
                if(trackingFailureReason == null) {
                    val desiredPlaneFindingMode = if (nearestNPCInfo.shouldPlace || childNodes.lastOrNull()?.isVisible == false)
                        Config.PlaneFindingMode.HORIZONTAL
                    else
                        Config.PlaneFindingMode.DISABLED

                    if (desiredPlaneFindingMode != session.config.planeFindingMode) {
                        runCatching {
                            session.configure(session.config.apply {
                                setPlaneFindingMode(desiredPlaneFindingMode)
                            })
                        }
                    }
                }

                if (frame.isTrackingPlane()
                    && nearestNPCInfo.shouldPlace
                ) {
                    nearestNPCInfo.npc?.id?.let { id ->
                        val planeAndPose = findPlaneInView(frame, widthPx, heightPx, frame.camera)

                        if(planeAndPose != null) {
                            val (plane, pose) = planeAndPose

                            viewModel.addAnchorNode(plane, pose, id, childNodes)
                        }
                    }
                }
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, node ->
                    if (node is ModelNode || node is ImageNode) {
                        val modelNode = if (node is ModelNode) node else node.parent as? ModelNode
                        val anchorNode = modelNode?.parent as? AnchorNode

                        val nodeId = modelNode?.name?.toInt()
                        val anchorId = anchorNode?.name

                        if (nodeId != null && anchorId != null) {
                            val quest = scriptNode[nodeId]
                            val state = questNodes[anchorId]

                            state?.let {
                                when (state) {
                                    // 퀘스트 진행전
                                    QuestStatus.WAIT -> {
                                        viewModel.showQuestDialog(nodeId, state) { accepted ->
                                            if (accepted) {
                                                viewModel.updateQuest(anchorId,
                                                    QuestStatus.PROGRESS)
                                                    .apply {
                                                        viewModel.updateAnchorNode(
                                                            modelNode,
                                                            quest,
                                                            anchorNode
                                                        )
                                                    }
                                            }
                                        }
                                    }
                                    // 퀘스트 진행중
                                    QuestStatus.PROGRESS -> {
                                        viewModel.showQuestDialog(nodeId, state) { accepted ->
                                            if (accepted) {
                                                // TODO 온디바이스 AI로 검사
                                                viewModel.updateQuest(
                                                    anchorId,
                                                    QuestStatus.COMPLETE
                                                ).apply {
                                                    val imageNode = modelNode.childNodes.filterIsInstance<ImageNode>()
                                                        .firstOrNull()

                                                    imageNode?.let {
                                                        viewModel.updateModelNode(imageNode, modelNode)
                                                    }

                                                    coroutineScope.launch {
                                                        snackBarHostState.showSnackbar("퀘스트가 완료되었습니다!")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    // 퀘스트 완료
                                    QuestStatus.COMPLETE -> {
                                        coroutineScope.launch {
                                            snackBarHostState.showSnackbar(quest.completeMessage)
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
        )

        Column {
            ArStatusText(
                trackingFailureReason = trackingFailureReason,
                isAvailable = nearestNPCInfo.shouldPlace,
                isPlace = nearestNPCInfo.npc?.id?.let { viewModel.getNpcMarker(it) }
            )
            Text(
                text = "위도: ${currentLocation?.latitude ?: "모니터링중..."} ",
                color = Color.White
            )
            Text(
                text = "경도: ${currentLocation?.longitude ?: "모니터링중..."} ",
                color = Color.White
            )
            Text(
                text = "정확도: ${currentLocation?.accuracy ?: "모니터링중..."} ",
                color = Color.Red
            )
            Text(
                text = "생성된 미션: ${questNodes.keys} ",
                color = Color.Red
            )
            Text(
                text = "가까운 미션: ${nearestNPCInfo.npc?.id ?: "모니터링중..."} ",
                color = Color.Red
            )
            Text(
                text = "미션까지 거리: ${nearestNPCInfo.distance?.let { "%.2fm".format(it) } ?: "모니터링중..."} ",
                color = Color.Red
            )
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            Snackbar(snackbarData = snackbarData)
        }
    }

    if (showDialog) {
        QuestDialog(
            idx = dialogData.first,
            state = dialogData.second,
            onConfirm = { viewModel.onDialogConfirm() },
            onDismiss = { viewModel.onDialogDismiss() }
        )
    }
}

private fun findPlaneInView(frame: Frame, width: Int, height: Int, camera: Camera): Pair<Plane, Pose>? {
    val center = android.graphics.PointF(width / 2f, height / 2f)
    val hits = frame.hitTest(center.x, center.y)

    val planeHit = hits.firstOrNull {
        it.isValid (
            depthPoint = true,
            point = true,
            planePoseInPolygon = true,
            instantPlacementPoint = false,
            minCameraDistance = Pair(camera, 0.5f),
            predicate = { hitResult -> hitResult.distance <= 3.0f && hitResult.trackable is Plane },
            planeTypes = setOf(Plane.Type.HORIZONTAL_UPWARD_FACING)
        )
    }

    return planeHit?.let { hit ->
        val plane = hit.trackable as Plane
        val pose = hit.hitPose
        Pair(plane, pose)
    }
}

@Composable
fun ArStatusText(
    trackingFailureReason: TrackingFailureReason?,
    isAvailable: Boolean,
    isPlace: Boolean?
) {
    Log.d(TAG, "ArStatusText: $trackingFailureReason ${isAvailable} $isPlace")
    
    Text(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(top = 16.dp, start = 32.dp, end = 32.dp),
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        color = Color.White,
        text = when {
            trackingFailureReason != TrackingFailureReason.NONE && trackingFailureReason != null ->
                TrackingMessage.fromTrackingFailureReason(trackingFailureReason).message
            else -> {
                if(isAvailable && isPlace != null && !isPlace)
                    TrackingMessage.SEARCH_AROUND.message
                else
                    TrackingMessage.EMPTY.message
            }
        }
    )
}

