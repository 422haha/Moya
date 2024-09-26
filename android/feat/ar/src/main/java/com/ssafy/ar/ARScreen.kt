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
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.TrackingFailureReason
import com.ssafy.ar.data.QuestStatus
import com.ssafy.ar.dummy.scriptNode
import com.ssafy.ar.manager.ARLocationManager
import com.ssafy.ar.manager.ARNodeManager
import com.ssafy.ar.util.MultiplePermissionsHandler
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.isTrackingPlane
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.node.ImageNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlinx.coroutines.launch
import kotlin.math.sqrt

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
    var planeRenderer by remember { mutableStateOf(true) }
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
                val desiredPlaneFindingMode = if (nearestNPCInfo.shouldPlaceNode)
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

                if (frame.isTrackingPlane()
                    && nearestNPCInfo.shouldPlaceNode
                ) {
                    nearestNPCInfo.nearestNPC?.id?.let { id ->
                        val planeAndPose = findPlaneInView(frame, widthPx, heightPx)

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
        ArStatusText(
            trackingFailureReason = trackingFailureReason,
            isEmpty = childNodes.isEmpty(),
        )

        Column {
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
                text = "가까운 마커: ${nearestNPCInfo.nearestNPC?.id ?: "모니터링중..."} ",
                color = Color.Red
            )
            Text(
                text = "마커 거리(m): ${nearestNPCInfo.nearestNPCDistance ?: "모니터링중..."} ",
                color = Color.Red
            )
            Text(
                text = "배치된 노드: ${questNodes.keys} ",
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

private fun findPlaneInView(frame: Frame, width: Int, height: Int): Pair<Plane, Pose>? {
    val center = android.graphics.PointF(width / 2f, height / 2f)
    val hits = frame.hitTest(center.x, center.y)

    Log.d("ARPlacement", "Performing hit test. Hits found: ${hits.size}")

    val planeHit = hits.firstOrNull { hit ->
        val trackable = hit.trackable
        val distance = calculateDistance(frame.camera.pose, hit.hitPose)

        trackable is Plane && trackable.type == Plane.Type.HORIZONTAL_UPWARD_FACING && distance <= 3f
    }

    return planeHit?.let { hit ->
        val plane = hit.trackable as Plane
        Log.d("ARPlacement", "Plane found - extentX: ${plane.extentX}, extentZ: ${plane.extentZ}")
        Pair(plane, plane.centerPose)
    }
}

private fun calculateDistance(pose1: Pose, pose2: Pose): Float {
    val dx = pose1.tx() - pose2.tx()
    val dy = pose1.ty() - pose2.ty()
    val dz = pose1.tz() - pose2.tz()
    return sqrt(dx * dx + dy * dy + dz * dz)
}

@Composable
fun ArStatusText(
    trackingFailureReason: TrackingFailureReason?,
    isEmpty: Boolean,
) {
    Text(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(top = 16.dp, start = 32.dp, end = 32.dp),
        textAlign = TextAlign.Center,
        fontSize = 28.sp,
        color = Color.White,
        text = (when (trackingFailureReason) {
            TrackingFailureReason.NONE -> "위치를 찾고 있어"
            TrackingFailureReason.BAD_STATE -> "지금 내부 상태가\n불안정해"
            TrackingFailureReason.INSUFFICIENT_LIGHT -> "주변이 어두워서\n찾을 수 없어"
            TrackingFailureReason.EXCESSIVE_MOTION -> "너무 빨리 움직이면\n찾을 수 없어"
            TrackingFailureReason.INSUFFICIENT_FEATURES -> "주변이 막혀 있어서\n찾을 수 없어"
            TrackingFailureReason.CAMERA_UNAVAILABLE -> "카메라를 사용할 수 없어"
            else -> {
                if (isEmpty) "친구가 놀라지 않게\n주변을 천천히 둘러봐!"
                else "클릭해서 미션을 확인해 봐!"
            }
        })
    )
}

