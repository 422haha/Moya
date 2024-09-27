package com.ssafy.ar.data

import com.google.ar.core.TrackingFailureReason

enum class TrackingMessage(val message: String) {
    NONE("위치를 찾고 있어.."),
    BAD_STATE("상태가 불안정해!"),
    INSUFFICIENT_LIGHT("깜깜해서 찾을 수 없어!"),
    EXCESSIVE_MOTION("빠르면 찾을 수 없어!"),
    INSUFFICIENT_FEATURES("막혀서 찾을 수 없어!"),
    CAMERA_UNAVAILABLE("카메라를 사용할 수 없어!"),
    SEARCH_AROUND("주변을 천천히 둘러봐!"),
    EMPTY("");

    companion object {
        fun fromTrackingFailureReason(reason: TrackingFailureReason?): TrackingMessage {
            return when (reason) {
                TrackingFailureReason.NONE -> NONE
                TrackingFailureReason.BAD_STATE -> BAD_STATE
                TrackingFailureReason.INSUFFICIENT_LIGHT -> INSUFFICIENT_LIGHT
                TrackingFailureReason.EXCESSIVE_MOTION -> EXCESSIVE_MOTION
                TrackingFailureReason.INSUFFICIENT_FEATURES -> INSUFFICIENT_FEATURES
                TrackingFailureReason.CAMERA_UNAVAILABLE -> CAMERA_UNAVAILABLE
                null -> EMPTY
            }
        }
    }
}