package com.ssafy.ui.encyclopedia

interface EncycUserIntent {
    data object OnPop : EncycUserIntent

    data class OnItemSelect(
        val id: Long,
    ) : EncycUserIntent

    data class OnChipSelected(
        val index: Int,
    ) : EncycUserIntent
}
