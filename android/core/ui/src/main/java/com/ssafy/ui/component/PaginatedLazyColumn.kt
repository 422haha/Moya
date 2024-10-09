package com.ssafy.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun PaginatedLazyColumn(
    modifier: Modifier = Modifier,
    loadMoreItems: () -> Unit,
    state: LazyListState,
    buffer: Int = 2,
    content: LazyListScope.() -> Unit,
) {
    val shouldLoadMore =
        remember {
            derivedStateOf {
                val totalItemsCount = state.layoutInfo.totalItemsCount
                val lastVisibleItemIndex =
                    state.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index ?: 0
                lastVisibleItemIndex >= totalItemsCount - buffer
            }
        }

    LaunchedEffect(state) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                loadMoreItems()
            }
    }

    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
        state = state,
        content = content,
    )
}
