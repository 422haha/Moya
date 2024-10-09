package com.ssafy.ui.encycdetail

interface EncycDetailUserIntent {
    data object OnPop : EncycDetailUserIntent

    data class OnTTSClicked(
        val text: String,
    ) : EncycDetailUserIntent

    data object OnTTSShutDown : EncycDetailUserIntent

    data object OnImageButtonClicked : EncycDetailUserIntent
}
