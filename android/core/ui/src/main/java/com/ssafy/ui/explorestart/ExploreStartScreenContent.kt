package com.ssafy.ui.explorestart

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import com.ssafy.ui.R
import com.ssafy.ui.component.ChallengeDialog
import com.ssafy.ui.component.CustomButtonWithImage
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.ExploreDialog
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SecondarySurfaceColor

val requiredPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

private const val PERMISSION_REQUEST_CODE = 101

@Composable
fun ExploreStartScreenContent(
    modifier: Modifier = Modifier,
    exploreStartScreenState: ExploreStartScreenState,
    onIntent: (ExploreStartUserIntent) -> Unit = {}
) {

    Scaffold(
        content = { paddingValues ->
            when (exploreStartScreenState) {
                is ExploreStartScreenState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValues))
                }

                is ExploreStartScreenState.Loaded -> {
                    ExploreStartScreenLoaded(
                        modifier = modifier.padding(paddingValues),
                        exploreStartScreenState = exploreStartScreenState,
                        onIntent = onIntent
                    )
                }

                is ExploreStartScreenState.Error -> {
                    ErrorScreen(
                        modifier = modifier.padding(paddingValues),
                        message = exploreStartScreenState.message
                    )
                }

                is ExploreStartScreenState.ShowExitDialog -> {
                    if (exploreStartScreenState.isVisible) {
                        Dialog(onDismissRequest = { onIntent(ExploreStartUserIntent.OnExitExploreDismissed) }) {
                            ExploreDialog(
                                title = "탐험을 끝마칠까요?",
                                onConfirm = {
                                    onIntent(ExploreStartUserIntent.OnExitExploreConfirmed)
                                },
                                onDismiss = {
                                    onIntent(ExploreStartUserIntent.OnExitExploreDismissed)
                                }
                            )
                        }
                    }
                }

                is ExploreStartScreenState.ShowChallengeDialog -> {
                    if (exploreStartScreenState.isVisible) {
                        Dialog(onDismissRequest = { onIntent(ExploreStartUserIntent.OnChallengeDismissed) }) {
                            ChallengeDialog(
                                onConfirm = { onIntent(ExploreStartUserIntent.OnChallengeConfirmed) },
                                onDismiss = { onIntent(ExploreStartUserIntent.OnChallengeDismissed) },
                                state = exploreStartScreenState
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ExploreStartScreenLoaded(
    modifier: Modifier,
    exploreStartScreenState: ExploreStartScreenState.Loaded,
    onIntent: (ExploreStartUserIntent) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(fusedLocationClient) {
        if (requiredPermissions.all { permission ->
                ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    cameraPositionState.position =
                        CameraPosition(LatLng(it.latitude, it.longitude), 16.0)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                requiredPermissions,
                PERMISSION_REQUEST_CODE
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier
                .fillMaxSize(),
            locationSource = rememberFusedLocationSource(),
            properties = MapProperties(
                locationTrackingMode = LocationTrackingMode.Follow
            ),
            cameraPositionState = cameraPositionState
        ) {
            exploreStartScreenState.markerPositions.forEach { position ->
                Marker(
                    state = MarkerState(position = position),
                    icon = OverlayImage.fromResource(R.drawable.ic_launcher_background)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomButtonWithImage(
                    text = "탐험 끝내기",
                    onClick = { onIntent(ExploreStartUserIntent.OnExitExploreRequested) },
                    buttonColor = PrimaryColor
                )
                CustomButtonWithImage(
                    text = "도감",
                    onClick = { onIntent(ExploreStartUserIntent.OnEnterEncyc) },
                    textColor = SecondarySurfaceColor,
                    imagePainter = R.drawable.auto_stories
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomButtonWithImage(
                text = "도전과제",
                onClick = { onIntent(ExploreStartUserIntent.OnChallengeConfirmed) },
                textColor = SecondarySurfaceColor,
                imagePainter = R.drawable.assignment_late
            )
        }

        IconButton(
            onClick = { onIntent(ExploreStartUserIntent.OnCameraClicked) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PrimaryColor)
                .size(52.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_center_focus_weak_24),
                contentDescription = "Search",
                tint = LightBackgroundColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreStartScreenPreview() {
    ExploreStartScreenContent(
        exploreStartScreenState = ExploreStartScreenState.Loaded(
            markerPositions = listOf(LatLng(36.106646, 128.421260))
        )
    )
}