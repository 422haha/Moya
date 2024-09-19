package com.ssafy.ar

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import com.ssafy.ar.ArData.QuestStatus
import com.ssafy.ar.dummy.scriptNode
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ImageNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

private const val TAG = "ArScreen"

@Composable
fun ARSceneComposable(viewModel: ARViewModel) {
    // AR Basic
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val cameraNode = rememberARCameraNode(engine)
    val childNodes = rememberNodes()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)
    var planeRenderer by remember { mutableStateOf(true) }

    val modelInstances = remember { mutableMapOf<String, ModelInstance>() }
    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    var frame by remember { mutableStateOf<Frame?>(null) }

    // AR Quest State
    val coroutineScope = rememberCoroutineScope()
    val anchorNodes by viewModel.anchorNodes.collectAsState()

    // Dialog & SnackBar
    val showDialog by viewModel.showDialog.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
//                config.geospatialMode = Config.GeospatialMode.ENABLED
            },
            cameraNode = cameraNode,
            planeRenderer = planeRenderer,
            onTrackingFailureChanged = {
                trackingFailureReason = it
            },
            onSessionUpdated = { session, updatedFrame ->
                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        frame = updatedFrame
                        if (childNodes.isEmpty()) {
                            updatedFrame.getUpdatedPlanes()
                                .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                                ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                                    planeRenderer = false
                                    val anchorNode = viewModel.createAnchorNode(
                                        scriptNode[0],
                                        engine,
                                        modelLoader,
                                        materialLoader,
                                        modelInstances,
                                        anchor,
                                    ).apply {
                                        val uuid = UUID.randomUUID().toString()

                                        name = uuid

                                        viewModel.addAnchorNode(uuid, QuestStatus.WAIT)
                                    }

                                    childNodes.add(anchorNode)
                                }
                        }
                    }
                }
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, node ->
                    val anchorId = node?.parent?.name

                    if (anchorId != null) {
                        val nodeId = node.name?.toInt()

                        if (nodeId != null && node is ModelNode) {
                            val quest = scriptNode[nodeId]
                            val state = anchorNodes[anchorId]

                            state?.let {
                                when (state) {
                                    // 퀘스트 진행전
                                    QuestStatus.WAIT -> {
                                        viewModel.showQuestDialog(
                                            nodeId, state
                                        ) { accepted ->
                                            if (accepted) { // 수락
                                                val parentAnchorNode = node.parent as? AnchorNode
                                                parentAnchorNode?.let {
                                                    viewModel.updateAnchorNode(
                                                        anchorId,
                                                        QuestStatus.PROGRESS
                                                    )

                                                    coroutineScope.launch {
                                                        viewModel.updateAnchorNode(
                                                            prevNode = node,
                                                            questId = quest.id,
                                                            questModel = quest.model,
                                                            parentAnchor = parentAnchorNode,
                                                            modelLoader = modelLoader,
                                                            materialLoader = materialLoader,
                                                            modelInstances = modelInstances,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    // 퀘스트 진행중
                                    QuestStatus.PROGRESS -> {
                                        viewModel.showQuestDialog(
                                            nodeId, state
                                        ) { accepted ->
                                            if (accepted) { // 완료
                                                // TODO 온디바이스 AI로 검사
                                                viewModel.updateAnchorNode(
                                                    anchorId,
                                                    QuestStatus.COMPLETE
                                                )
                                                    .apply {
                                                        node.childNodes.filterIsInstance<ImageNode>()
                                                            .firstOrNull()
                                                            ?.setBitmap("picture/complete.png")

                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("퀘스트가 완료되었습니다!")
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                    // 퀘스트 완료
                                    QuestStatus.COMPLETE -> {}
                                }
                            }
                        }
                    }
                })
        )
        ArStatusText(
            trackingFailureReason = trackingFailureReason,
            childNodesEmpty = childNodes.isEmpty()
        )

        SnackbarHost(
            hostState = snackbarHostState,
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

@Composable
fun ArStatusText(
    trackingFailureReason: TrackingFailureReason?,
    childNodesEmpty: Boolean
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
            TrackingFailureReason.NONE -> {
                "위치를 찾고 있어"
            }

            TrackingFailureReason.BAD_STATE -> "지금 내부 상태가\n불안정해"
            TrackingFailureReason.INSUFFICIENT_LIGHT -> "주변이 어두워서\n찾을 수 없어"
            TrackingFailureReason.EXCESSIVE_MOTION -> "너무 빨리 움직이면\n찾을 수 없어"
            TrackingFailureReason.INSUFFICIENT_FEATURES -> "주변이 막혀 있어서\n찾을 수 없어"
            TrackingFailureReason.CAMERA_UNAVAILABLE -> "카메라를 사용할 수 없어"
            else -> {
                if (childNodesEmpty)
                    "동물이 놀라지 않게\n천천히 주변을 둘러봐!"
                else
                    "클릭해서 미션을 확인해!"
            }
        })
    )
}

// 지금 childNode에 1개만 할 수 있음
// 앵커를 효율적으로 여러개 배치해야함

// track 메시지 분기처리

// GPS마다 노드 배치