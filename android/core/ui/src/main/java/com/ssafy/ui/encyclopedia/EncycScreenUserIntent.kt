package com.ssafy.ui.encyclopedia

interface EncycScreenUserIntent {
    data object OnPop : EncycScreenUserIntent
    data class OnItemSelect(val id: Long) : EncycScreenUserIntent
    data class OnChipSelected(val index: Int) : EncycScreenUserIntent
}