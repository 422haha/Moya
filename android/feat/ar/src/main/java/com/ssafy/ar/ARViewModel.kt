package com.ssafy.ar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.ar.ArData.CurrentLocation
import com.ssafy.ar.ArData.QuestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ARViewModel : ViewModel() {
    // AR AnchorNode
    private val _anchorNodes = MutableStateFlow<Map<String, QuestStatus>>(emptyMap())
    val anchorNodes: StateFlow<Map<String, QuestStatus>> = _anchorNodes.asStateFlow()

    // Location
    private val _curLocation = MutableStateFlow(CurrentLocation())
    val curLocation: StateFlow<CurrentLocation> = _curLocation.asStateFlow()

    // Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    // Dialog Data
    private val _dialogData = MutableStateFlow<Pair<Int, QuestStatus> >(Pair(0, QuestStatus.WAIT))
    val dialogData: StateFlow<Pair<Int, QuestStatus> > = _dialogData
    private var dialogCallback: ((Boolean) -> Unit)? = null

    fun addAnchorNode(id: String, state: QuestStatus) {
        val updatedMap = _anchorNodes.value.toMutableMap().apply {
            put(id, state)
        }
        _anchorNodes.value = updatedMap
    }

    fun updateAnchorNode(id: String, newState: QuestStatus) {
        viewModelScope.launch {
            val updatedMap = _anchorNodes.value.toMutableMap().apply {
                this[id] = newState
            }
            _anchorNodes.value = updatedMap
        }
    }

    fun removeAnchorNode(id: String) {
        viewModelScope.launch {
            val updatedMap = _anchorNodes.value.toMutableMap().apply {
                remove(id)
            }
            _anchorNodes.value = updatedMap
        }
    }

    fun getAnchorNodeId(id: String): QuestStatus? {
        return _anchorNodes.value[id]
    }

    fun updateLocation(newLocation: CurrentLocation) {
        _curLocation.value = newLocation
    }

    fun showQuestDialog(index: Int, state: QuestStatus, callback: (Boolean) -> Unit) {
        _dialogData.value = Pair(index, state)
        _showDialog.value = true
        dialogCallback = callback
    }

    fun onDialogConfirm() {
        _showDialog.value = false
        _dialogData.value = Pair(0, QuestStatus.WAIT)
        dialogCallback?.invoke(true)
        dialogCallback = null
    }

    fun onDialogDismiss() {
        _showDialog.value = false
        _dialogData.value = Pair(0, QuestStatus.WAIT)
        dialogCallback?.invoke(false)
        dialogCallback = null
    }
}