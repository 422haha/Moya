package com.ssafy.ar.ui

import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.Manifest
import android.content.pm.PackageManager
import android.media.Image
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.ar.core.Config
import com.google.ar.core.TrackingFailureReason
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.ssafy.ar.ARViewModel
import com.ssafy.ar.R
import com.ssafy.ar.data.QuestState
import com.ssafy.ar.data.SpeciesType
import com.ssafy.ar.data.getImageResource
import com.ssafy.ar.data.scripts
import com.ssafy.ar.manager.generateUniqueFileName
import com.ssafy.ar.manager.imageToBitmap
import com.ssafy.ar.manager.saveImageToInternalStorage
import com.ssafy.ar.util.MultiplePermissionsHandler
import com.ssafy.moya.ai.DataProcess
import com.ssafy.moya.ai.Result
import com.ssafy.network.request.RegisterSpeciesRequestBody
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.isTrackingPlane
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "ArScreen"

@Composable
fun ARSceneComposable(
    explorationId: Long,
    onPop: () -> Unit,
    onTTSClicked: (String) -> Unit,
    onTTSShutDown: () -> Unit,
    onTTSReStart: () -> Unit,
) {
    // Screen Size
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val density = LocalDensity.current
    val widthPx = with(density) { screenWidth.toPx() }.toInt()
    val heightPx = with(density) { screenHeight.toPx() }.toInt()

    // 화면 채우기
    val localView = LocalView.current
    val window = (localView.context as ComponentActivity).window
    val insetsController = WindowCompat.getInsetsController(window, localView)
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()

    DisposableEffect(Unit) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = true

        onDispose {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            window.statusBarColor = Color.White.toArgb()
            window.navigationBarColor = Color.White.toArgb()
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    // LifeCycle
    val context = LocalContext.current
    val viewModel: ARViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val imageProcessingScope = remember { CoroutineScope(Dispatchers.Default + SupervisorJob()) }

    // Dialog & SnackBar
    val showDialog by viewModel.showDialog.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    // STT
    var showSTT by remember { mutableStateOf(false) }

    // Permission
    var hasPermission by remember { mutableStateOf(false) }
    val requiredPermissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.RECORD_AUDIO
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showSTT = true
        } else {
            coroutineScope.launch {
                snackBarHostState.showSnackbar("권한을 허용 해야 사용할 수 있습니다.")
            }
        }
    }

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
    val nearestQuestInfo by viewModel.nearestQuestInfo.collectAsState()

    // RatingBar
    val rating by viewModel.rating.collectAsState()
    var showRating by remember { mutableStateOf(true) }
    val animatedRating by animateFloatAsState(
        targetValue = if (showRating) rating else 0f,
        label = "Rating Animation",
    )

    // AI 모델 초기화 및 세션 설정
    val dataProcess = remember { DataProcess(context = context) }
    val ortEnvironment = remember { OrtEnvironment.getEnvironment() }
    var ortSession by remember { mutableStateOf<OrtSession?>(null) }

    var detectionResults by remember { mutableStateOf(listOf<Result>()) }

    var frameCounter = 0
    var isProcessingImage = false
    val registeredSpecies = mutableSetOf<Int>()

    LaunchedEffect(detectionResults) {
        // 도감 등록
        detectionResults.forEach {
            viewModel.registerSpecies(
                explorationId,
                RegisterSpeciesRequestBody(
                    (it.classIndex + 1).toLong(),
                    it.imageUrl ?: "",
                    viewModel.locationManager.currentLocation.value
                        ?.latitude ?: 0.0,
                    viewModel.locationManager.currentLocation.value
                        ?.longitude ?: 0.0,
                ),
                onSuccess = { result ->
                    registeredSpecies.add((result.speciesId - 1).toInt())
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar("도감에 등록되었습니다!")
                    }
                },
                onError = {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(it)
                    }
                },
            )
        }

        // 미션 등록
        val detectedClassIndexes = detectionResults.map { it.classIndex }

        val progressQuests =
            questInfos
                .filter { (_, questInfo) -> questInfo.isComplete == QuestState.PROGRESS }
                .map { it.value }

        val completeQuests =
            progressQuests.filter { quest ->
                quest.speciesId.toInt() in detectedClassIndexes
            }

        completeQuests.firstOrNull()?.let {
            coroutineScope.launch {
                val anchorId: Long = it.id

                val modelNode: ModelNode? =
                    childNodes
                        .flatMap { it.childNodes }
                        .filterIsInstance<ModelNode>()
                        .firstOrNull { it.name == anchorId.toString() }

                modelNode?.let {
                    val result =
                        viewModel.completeQuest(
                            explorationId,
                            anchorId,
                        )
                    when (result) {
                        true -> {
                            viewModel.updateQuestState(
                                anchorId,
                                QuestState.COMPLETE,
                            )

                            val imageNode =
                                modelNode.childNodes
                                    .filterIsInstance<ImageNode>()
                                    .firstOrNull()

                            imageNode?.let {
                                viewModel.updateModelNode(
                                    imageNode,
                                    modelNode,
                                    materialLoader,
                                )
                            }

                            snackBarHostState.showSnackbar("퀘스트가 완료되었습니다!")
                        }

                        false -> snackBarHostState.showSnackbar("알 수 없는 오류가 발생했습니다.")
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            imageProcessingScope.cancel()
            coroutineScope.cancel()
            onTTSShutDown()
        }
    }

    MultiplePermissionsHandler(
        permissions =
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
    ) { permissionResults ->
        if (permissionResults.all { permissions -> permissions.value }) {
            hasPermission = true
        } else { onPop() }
    }

    if (hasPermission) {
        // AR 씬 및 기타 초기화 로직
        LaunchedEffect(Unit) {
            onTTSReStart()
            viewModel.locationManager.startLocationUpdates()
            viewModel.getAllQuests(explorationId)

            imageProcessingScope.launch {
                withContext(Dispatchers.IO) {
                    dataProcess.loadModel()
                    ortSession = ortEnvironment.createSession(
                        context.filesDir.absolutePath.toString() + "/" + DataProcess.FILE_NAME,
                        OrtSession.SessionOptions()
                    )
                    dataProcess.loadLabel()
                }
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
                    config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                    config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
                    config.focusMode = Config.FocusMode.AUTO
//                config.geospatialMode = Config.GeospatialMode.ENABLED
                },
                cameraNode = cameraNode,
                planeRenderer = planeRenderer,
                onTrackingFailureChanged = { trackingFailureReason = it },
                onSessionUpdated = { session, frame ->
                    frameCounter++
                    if (frameCounter % 30 == 0 && !isProcessingImage) {
                        isProcessingImage = true

                        imageProcessingScope.launch(Dispatchers.IO) {
                            var image: Image? = null
                            try {
                                // 이미지 획득
                                image = frame.acquireCameraImage()

                                // 이미지 변환 (백그라운드 스레드)
                                val bitmap = imageToBitmap(image)

                                // AI 처리
                                ortSession?.let { session ->
                                    val results =
                                        dataProcess.processImage(bitmap, ortEnvironment, session)

                                    var updatedResults = results

                                    // 로컬에 이미지 저장
                                    if (results.isNotEmpty()) {
                                        val fileName = generateUniqueFileName()
                                        val file = File(context.filesDir, fileName)
                                        saveImageToInternalStorage(image, file)

                                        Log.d(TAG, "ARSceneComposable: ${file.absolutePath}")
                                        updatedResults =
                                            results.map { result ->
                                                result.copy(imageUrl = file.absolutePath ?: "")
                                            }

                                        detectionResults = updatedResults

                                        Log.d("DataProcess", "인식 결과 : $detectionResults")
                                    }
                                }
                            } catch (e: Exception) {
                                // 예외 처리
                                Log.e("errorInAR", e.toString())
                            } finally {
                                // 이미지 닫기 및 상태 업데이트
                                image?.close()
                                isProcessingImage = false
                            }
                        }
                    }

                    if (trackingFailureReason == null) {
                        val desiredPlaneFindingMode =
                            if (nearestQuestInfo.shouldPlace || childNodes.lastOrNull()?.isVisible == false) {
                                Config.PlaneFindingMode.HORIZONTAL
                            } else {
                                Config.PlaneFindingMode.DISABLED
                            }

                        if (desiredPlaneFindingMode != session.config.planeFindingMode) {
                            session.configure(
                                session.config.apply {
                                    setPlaneFindingMode(desiredPlaneFindingMode)
                                },
                            )
                        }
                    }

                    if (frame.isTrackingPlane() && nearestQuestInfo.shouldPlace) {
                        nearestQuestInfo.npc?.let { quest ->
                            val planeAndPose =
                                viewModel.nodeManager.findPlaneInView(
                                    frame,
                                    widthPx,
                                    heightPx,
                                    frame.camera
                                )

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
                                        childNodes,
                                    )
                                }
                            }
                        }
                    }
                },
                onGestureListener =
                rememberOnGestureListener(
                    onSingleTapConfirmed = { motionEvent, node ->
                        if (node is ModelNode || node?.parent is ModelNode) {
                            val modelNode =
                                if (node is ModelNode) node else node.parent as? ModelNode

                            val anchorNode = modelNode?.parent as? AnchorNode

                            val anchorId = anchorNode?.name?.toLong()

                            if (anchorId != null) {
                                val quest = questInfos[anchorId]

                                quest?.let {
                                    when (quest.isComplete) {
                                        // 퀘스트 진행전
                                        QuestState.WAIT -> {
                                            viewModel.showQuestDialog(
                                                quest,
                                            ) { accepted ->
                                                if (accepted) {
                                                    viewModel.updateQuestState(
                                                        anchorId,
                                                        QuestState.PROGRESS,
                                                    )

                                                    viewModel.updateAnchorNode(
                                                        quest,
                                                        modelNode,
                                                        anchorNode,
                                                        modelLoader,
                                                        materialLoader,
                                                    )
                                                }
                                            }
                                        }
                                        // 퀘스트 진행중
                                        QuestState.PROGRESS -> {
                                            viewModel.showQuestDialog(
                                                quest,
                                            ) { accepted -> }
                                        }
                                        // 퀘스트 완료
                                        QuestState.COMPLETE -> {
                                            coroutineScope.launch {
                                                snackBarHostState.showSnackbar(
                                                    scripts[quest.questType]?.completeMessage ?: "",
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                ),
            )

            Column {
                Box(
                    modifier =
                    Modifier
                        .padding(top = 60.dp, start = 40.dp, end = 40.dp),
                ) {
                    CustomCard(
                        imageUrl =
                        SpeciesType
                            .fromLong(nearestQuestInfo.npc?.speciesId ?: 1L)
                            ?.getImageResource() ?: (R.drawable.maple),
                        title = "가까운 미션 ${nearestQuestInfo.npc?.id ?: "검색중.."} ",
                        state = nearestQuestInfo.npc?.isComplete ?: QuestState.WAIT,
                        distanceText = "${
                            nearestQuestInfo.distance?.let {
                                if (nearestQuestInfo.shouldPlace) {
                                    "목적지 도착!"
                                } else {
                                    "%.2f m".format(it)
                                }
                            } ?: "검색중.."
                        } ",
                    )
                    Card(
                        modifier =
                        Modifier
                            .offset(y = (-20).dp)
                            .align(Alignment.TopCenter),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Gray),
                    ) {
                        RatingBar(
                            value = animatedRating,
                            config =
                            RatingBarConfig()
                                .isIndicator(true)
                                .stepSize(StepSize.HALF)
                                .numStars(3)
                                .size(28.dp)
                                .inactiveColor(Color.LightGray)
                                .style(RatingBarStyle.Normal),
                            onValueChange = { },
                            onRatingChanged = { },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }

                ArStatusText(
                    trackingFailureReason = trackingFailureReason,
                    isAvailable = nearestQuestInfo.shouldPlace,
                    isPlace = nearestQuestInfo.npc?.id?.let { viewModel.getIsPlaceQuest(it) },
                )
            }

            IconButton(
                onClick = { onPop() },
                modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF32A287))
                    .size(52.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_map_24),
                    contentDescription = "Search",
                    tint = Color(0xFFF7F7F7),
                )
            }

            FloatingActionButton(
                onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) -> {
                            showSTT = true
                        }
                        else -> {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    } },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 85.dp, end = 26.dp)
                    .size(72.dp),
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chatbot),
                    contentDescription = "Chat Bot",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
            }

            if (showSTT) {
                STTOverlay(
                    onDismiss = { showSTT = false },
                    onResult = { result ->
                        showSTT = false
                        result?.let {
                            viewModel.chattingNPC(explorationId,
                                nearestQuestInfo.npc?.npcPosId ?: 0,
                                it,
                                onSuccess = { msg ->
                                    onTTSClicked(msg)
                                },
                                onError = { error ->
                                    coroutineScope.launch { snackBarHostState.showSnackbar(error) }
                                })
                        }
                    }
                )
            }

            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            ) { snackbarData ->
                Snackbar(snackbarData = snackbarData)
            }
        }

        if (showDialog) {
            QuestDialog(
                dialogData,
                onConfirm = { viewModel.onDialogConfirm() },
                onDismiss = { viewModel.onDialogDismiss() },
            )
        }
    }
}