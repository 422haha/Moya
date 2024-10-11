package com.ssafy.ui.explorestart

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMapComposable
import com.naver.maps.map.overlay.OverlayImage
import com.ssafy.model.LatLng

@OptIn(ExperimentalNaverMapApi::class)
@NaverMapComposable
@Composable
fun ExploreMarker(
    modifier: Modifier = Modifier,
    state: ExploreMarkerState,
) {
    val context = LocalContext.current
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        val request =
            ImageRequest
                .Builder(context)
                .data(state.imageUrl)
                .build()
        with(density) {
            bitmap =
                context.imageLoader.execute(request).drawable?.toBitmap(
                    width = 70.dp.toPx().toInt(),
                    height = 70.dp.toPx().toInt(),
                )
        }
    }

    for (position in state.positions) {
        bitmap?.let {
            Marker(
                state =
                    MarkerState(
                        position =
                            com.naver.maps.geometry.LatLng(
                                position.latitude,
                                position.longitude,
                            ),
                    ),
                icon = OverlayImage.fromBitmap(it),
                captionText = state.name,
            )
        }
    }
}

data class ExploreMarkerState(
    val name: String,
    val imageUrl: String?,
    val positions: List<LatLng>,
)
