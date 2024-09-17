package com.ssafy.ar

import android.system.Os.remove
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.ar.ArData.ARNode
import com.ssafy.ar.ArData.CurrentLocation
import com.ssafy.ar.ArData.QuestData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ARViewModel : ViewModel() {
    // AR AnchorNode
    private val _anchorNodes = MutableStateFlow<Map<String, Int>>(emptyMap())
    val anchorNodes: StateFlow<Map<String, Int>> = _anchorNodes.asStateFlow()

    // Location
    private val _curLocation = MutableStateFlow(CurrentLocation())
    val curLocation: StateFlow<CurrentLocation> = _curLocation.asStateFlow()

    // Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    // Dialog Data
    private val _dialogData = MutableStateFlow(Pair(0, 0))
    val dialogData: StateFlow<Pair<Int, Int> > = _dialogData
    private var dialogCallback: ((Boolean) -> Unit)? = null

    fun addAnchorNode(id: String, state: Int) {
        val updatedMap = _anchorNodes.value.toMutableMap().apply {
            put(id, state)
        }
        _anchorNodes.value = updatedMap
    }

    fun updateAnchorNode(id: String, newState: Int) {
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

    fun getAnchorNodeId(id: String): Int? {
        return _anchorNodes.value[id]
    }

    fun updateLocation(newLocation: CurrentLocation) {
        _curLocation.value = newLocation
    }

    fun showQuestDialog(index: Int, state: Int, callback: (Boolean) -> Unit) {
        _dialogData.value = Pair(index, state)
        _showDialog.value = true
        dialogCallback = callback
    }

    fun onDialogConfirm() {
        _showDialog.value = false
        _dialogData.value = Pair(0, 0)
        dialogCallback?.invoke(true)
        dialogCallback = null
    }

    fun onDialogDismiss() {
        _showDialog.value = false
        _dialogData.value = Pair(0, 0)
        dialogCallback?.invoke(false)
        dialogCallback = null
    }
}