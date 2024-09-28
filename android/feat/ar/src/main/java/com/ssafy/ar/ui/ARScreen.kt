package com.ssafy.ar.ui

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.Camera
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.TrackingFailureReason
import com.ssafy.ar.ARViewModel
import com.ssafy.ar.data.QuestState
import com.ssafy.ar.data.QuestType
import com.ssafy.ar.data.TrackingMessage
import com.ssafy.ar.data.getImageResource
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
    val viewModel: ARViewModel = hiltViewModel()
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

    // AR State
    val questInfos by viewModel.questInfos.collectAsState()
    val scriptInfos by viewModel.scriptInfos.collectAsState()
    val nearestQuestInfo by viewModel.nearestQuestInfo.collectAsState()

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

            viewModel.locationManager.startLocationUpdates()

            viewModel.getAllQuests()

            viewModel.getAllScripts()
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
                if (trackingFailureReason == null) {
                    val desiredPlaneFindingMode =
                        if (nearestQuestInfo.shouldPlace || childNodes.lastOrNull()?.isVisible == false)
                            Config.PlaneFindingMode.HORIZONTAL
                        else
                            Config.PlaneFindingMode.DISABLED

                    if (desiredPlaneFindingMode != session.config.planeFindingMode) {
                        session.configure(session.config.apply {
                            setPlaneFindingMode(desiredPlaneFindingMode)
                        })
                    }
                }

                if (frame.isTrackingPlane() && nearestQuestInfo.shouldPlace) {
                    nearestQuestInfo.npc?.let { quest ->
                        val planeAndPose = findPlaneInView(frame, widthPx, heightPx, frame.camera)

                        if (planeAndPose != null) {
                            val (plane, pose) = planeAndPose

                            if (childNodes.all { it.name != quest.id.toString() }) {
                                viewModel.placeNode(
                                    plane,
                                    pose,
                                    quest,
                                    engine,
                                    modelLoader,
                                    materialLoader,
                                    childNodes
                                )
                            }
                        }
                    }
                }
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, node ->
                    if (node is ModelNode || node?.parent is ModelNode) {
                        val modelNode = if (node is ModelNode) node else node.parent as? ModelNode

                        val anchorNode = modelNode?.parent as? AnchorNode

                        val anchorId = anchorNode?.name?.toLong()

                        if (anchorId != null) {
                            val quest = questInfos[anchorId]

                            quest?.let {
                                when (val state = quest.isComplete) {
                                    // 퀘스트 진행전
                                    QuestState.WAIT -> {
                                        viewModel.showQuestDialog(scriptInfos[quest.questType], state) { accepted ->
                                            if (accepted) {
                                                viewModel.updateQuestState(
                                                    anchorId,
                                                    QuestState.PROGRESS
                                                ).apply {
                                                    viewModel.updateAnchorNode(
                                                        quest,
                                                        modelNode,
                                                        anchorNode,
                                                        modelLoader,
                                                        materialLoader
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    // 퀘스트 진행중
                                    QuestState.PROGRESS -> {
                                        viewModel.showQuestDialog(scriptInfos[quest.questType], state) { accepted ->
                                            if (accepted) {
                                                // TODO 온디바이스 AI로 검사
                                                viewModel.updateQuestState(
                                                    anchorId,
                                                    QuestState.COMPLETE
                                                ).apply {
                                                    val imageNode =
                                                        modelNode.childNodes.filterIsInstance<ImageNode>()
                                                            .firstOrNull()

                                                    imageNode?.let {
                                                        viewModel.updateModelNode(
                                                            imageNode,
                                                            modelNode,
                                                            materialLoader
                                                        )
                                                    }

                                                    coroutineScope.launch {
                                                        snackBarHostState.showSnackbar("퀘스트가 완료되었습니다!")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    // 퀘스트 완료
                                    QuestState.COMPLETE -> {
                                        coroutineScope.launch {
                                            snackBarHostState.showSnackbar(scriptInfos[quest.questType]?.completeMessage ?: "")
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
        )

        Column {
            CustomCard(
                imageUrl = QuestType.fromInt(nearestQuestInfo.npc?.questType ?: 0)?.getImageResource() ?: 0,
                title = "가까운 미션: ${nearestQuestInfo.npc?.id ?: "검색중.."} ",
                state = nearestQuestInfo.npc?.isComplete ?: QuestState.WAIT,
                distanceText = "거리: ${
                    nearestQuestInfo.distance?.let { if(it > 10.0f) "%.2f m".format(it) else "수행 가능" } ?: "검색중.."
                } ")

            ArStatusText(
                trackingFailureReason = trackingFailureReason,
                isAvailable = nearestQuestInfo.shouldPlace,
                isPlace = nearestQuestInfo.npc?.id?.let { viewModel.getIsPlaceQuest(it) }
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
            script = dialogData.first,
            state = dialogData.second,
            onConfirm = { viewModel.onDialogConfirm() },
            onDismiss = { viewModel.onDialogDismiss() }
        )
    }
}

private fun findPlaneInView(
    frame: Frame,
    width: Int,
    height: Int,
    camera: Camera
): Pair<Plane, Pose>? {
    val center = android.graphics.PointF(width / 2f, height / 2f)
    val hits = frame.hitTest(center.x, center.y)

    val planeHit = hits.firstOrNull {
        it.isValid(
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

