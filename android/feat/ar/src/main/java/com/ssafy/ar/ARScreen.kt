package com.ssafy.ar

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.arcoretest.ARNode
import com.example.arcoretest.ARViewModel
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Earth
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Rotation
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView

private const val kMaxModelInstances = 10
private const val VISIBLE_DISTANCE_THRESHOLD = 100.0 // meters

private const val TAG = "ArScreen"

@Composable
fun ARSceneComposable(viewModel: ARViewModel) {
    val arNodes by viewModel.arNodes.collectAsState()
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val cameraNode = rememberARCameraNode(engine)
    val childNodes = rememberNodes()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)
    val planeRenderer by remember { mutableStateOf(false) }

    val modelInstances = remember { mutableMapOf<String, ModelInstance>() }
    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    var frame by remember { mutableStateOf<Frame?>(null) }

    var nodesProcessed by remember { mutableStateOf(false) }

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
                config.lightEstimationMode =
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
                config.geospatialMode = Config.GeospatialMode.ENABLED
            },
            cameraNode = cameraNode,
            planeRenderer = false,
            onTrackingFailureChanged = {
                trackingFailureReason = it
            },
            // 매 프레임마다 호출
            onSessionUpdated = { session, updatedFrame ->
                frame = updatedFrame

                Log.d(TAG, "ARSceneComposable: ${session.earth?.cameraGeospatialPose?.horizontalAccuracy}")
                session.earth?.let { earth ->
                    if (earth.trackingState == TrackingState.TRACKING && !nodesProcessed) {
                        arNodes.forEach { node ->
                            session.earth?.let {
                                processNode(
                                    node,
                                    it,
                                    engine,
                                    modelLoader,
                                    modelInstances,
                                    childNodes,
                                )
                            }
                        }

                        nodesProcessed = true
                    }
                }
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, node -> })
        )
    }
}

private fun processNode(
    node: ARNode,
    earth: Earth,
    engine: Engine,
    modelLoader: ModelLoader,
    modelInstances: MutableMap<String, ModelInstance>,
    childNodes: MutableList<Node>) {
    if (node.isActive) {
        try {
            val earthAnchor = earth.createAnchor(
                node.latitude,
                node.longitude,
                node.altitude,
                0f, 0f, 0f, 1f
            )

            val anchorNode = createAnchorNode(
                node,
                engine = engine,
                modelLoader = modelLoader,
                modelInstances = modelInstances,
                anchor = earthAnchor
            )

            childNodes.add(anchorNode)
        } catch (e: Exception) {
            Log.e("ARScene", "Error creating anchor for ${node.id}: ${e.message}")
        }
    }
}

// 특정 위치에 3D 모델을 배치
private fun createAnchorNode(
    node: ARNode,
    engine: Engine,
    modelLoader: ModelLoader,
    modelInstances: MutableMap<String, ModelInstance>,
    anchor: Anchor
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor).apply {
        isPositionEditable = false
        isRotationEditable = false
        isScaleEditable = false
    }

    val modelInstance = modelInstances[node.id] ?: modelLoader.createInstancedModel(
        node.model,
        kMaxModelInstances
    ).first()

    val modelNode = ModelNode(
        modelInstance = modelInstance,
        // Scale to fit in a 0.5 meters cube
        scaleToUnits = 0.5f
    ).apply {
        // Model Node needs to be editable for independent rotation from the anchor rotation
        rotation = Rotation(0f, 180f, 0f)
    }

    anchorNode.addChildNode(modelNode)

    return anchorNode
}