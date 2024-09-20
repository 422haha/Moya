package com.ssafy.ar

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.filament.Engine
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.dependencies.f
import com.ssafy.ar.ArData.NPCLocation
import com.ssafy.ar.ArData.QuestStatus
import com.ssafy.ar.dummy.npcs
import com.ssafy.ar.dummy.scriptNode
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

private const val TAG = "ArScreen"

@Composable
fun ARSceneComposable(
    viewModel: ARViewModel,
    onPermissionDenied: () -> Unit
) {
    // LifeCycle
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // Permission
    var hasPermission by remember { mutableStateOf(false) }

    // Dialog & SnackBar
    val showDialog by viewModel.showDialog.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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

    // AR State
    val anchorNodes by viewModel.anchorNodes.collectAsState()
    val npcMarkerLocations by viewModel.npcMarketNodes.collectAsState()

    val currentLocation by viewModel.currentLocation.collectAsState()
    // Location
    var nearestNPCDistance by remember { mutableStateOf<Float?>(0f) }
    var nearestNPC by remember { mutableStateOf<NPCLocation?>(null) }

    val fusedLocationClient: FusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    viewModel.updateLocation(location)
                }
            }
        }
    }

    MultiplePermissionsHandler(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) { permissionResults ->
        if (permissionResults.all { permissions -> permissions.value }) {
            hasPermission = true

            viewModel.getAllNpcMarker()
        } else {
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
                        val npcId = nearestNPC?.id ?: ""
                        val isPlaceNPC = npcId.let { viewModel.isNPCPlaced(it) }

                        if (npcId.isNotBlank() &&
                            !isPlaceNPC &&
                            (currentLocation?.accuracy ?: 100.0f) >= 8.0f &&
                            (nearestNPCDistance ?: 100.0f) <= 10.0f
                        ) {
                            coroutineScope.launch {
                                frame = updatedFrame
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

                                            viewModel.updateNPCLocationIsPlace(npcId, true)
                                        }

                                        childNodes.add(anchorNode)
                                    }
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
                                    QuestStatus.COMPLETE -> {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(quest.completeMessage)
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
            childNodesEmpty = childNodes.isEmpty()
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
                text = "가까운 마커: ${nearestNPC?.id ?: "모니터링중..."} ",
                color = Color.Red
            )
            Text(
                text = "마커 거리(m): ${nearestNPCDistance ?: "모니터링중..."} ",
                color = Color.Red
            )
        }

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

    LaunchedEffect(currentLocation) {
        nearestNPC = currentLocation?.let { findNearestNPC(it, npcMarkerLocations) }
        nearestNPCDistance = currentLocation?.let { curLoc ->
            nearestNPC?.let { curNPC ->
                measureNearestNpcDistance(curLoc, curNPC)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                startLocationUpdates(context, fusedLocationClient, locationCallback)
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                stopLocationUpdates(fusedLocationClient, locationCallback)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            stopLocationUpdates(fusedLocationClient, locationCallback)
        }
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

// 위치 추적 시작
private fun startLocationUpdates(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 1000
    )
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(1000)
        .setMinUpdateDistanceMeters(0.2f)
        .build()

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}

// 위치 추적 종료
private fun stopLocationUpdates(
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {
    fusedLocationClient.removeLocationUpdates(locationCallback)
}

// 가장 가까운 노드와의 거리를 측정
private fun measureNearestNpcDistance(location: Location, npcLocation: NPCLocation): Float {
    val targetLocation = Location("target").apply {
        latitude = npcLocation.latLng.latitude
        longitude = npcLocation.latLng.longitude
    }

    val distanceInMeters = location.distanceTo(targetLocation)
    return distanceInMeters
}

// 가장 가까운 노드를 찾기
private fun findNearestNPC(
    currentLocation: Location,
    npcLocations: Map<String, NPCLocation>
): NPCLocation? {
    return npcLocations.values.minByOrNull { location ->
        val npcLocation = Location("npc").apply {
            latitude = location.latLng.latitude
            longitude = location.latLng.longitude
        }

        currentLocation.distanceTo(npcLocation)
    }
}

// viewModel에서 관리해야할 것으로 옮김