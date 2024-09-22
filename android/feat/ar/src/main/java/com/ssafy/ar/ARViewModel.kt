package com.ssafy.ar

import android.location.Location
import android.system.Os.remove
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.ar.data.NPCLocation
import com.ssafy.ar.data.QuestStatus
import com.ssafy.ar.dummy.npcs
import com.ssafy.ar.manager.ARLocationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ARViewModel(
    private val locationManager: ARLocationManager,
) : ViewModel() {
    private val mutex = Mutex()
    var isPlacingNode = false

    // AR AnchorNode
    private val _anchorNodes = MutableStateFlow<Map<String, QuestStatus>>(emptyMap())
    val anchorNodes: StateFlow<Map<String, QuestStatus>> = _anchorNodes.asStateFlow()

    // AR NPC
    private val _npcMarketNodes = MutableStateFlow<Map<String, NPCLocation>>(emptyMap())
    val npcMarketNodes: StateFlow<Map<String, NPCLocation>> = _npcMarketNodes.asStateFlow()

    // 가장 가까운 NPC
    private val _nearestNPC = MutableStateFlow<NPCLocation?>(null)
    val nearestNPC: StateFlow<NPCLocation?> = _nearestNPC.asStateFlow()

    // 가장 가까운 NPC와의 거리(m)
    private val _nearestNPCDistance = MutableStateFlow<Float?>(0f)
    val nearestNPCDistance: StateFlow<Float?> = _nearestNPCDistance.asStateFlow()

    // 배치 가능한 노드
    private val _shouldPlaceNode = MutableStateFlow<String?>(null)
    val shouldPlaceNode: StateFlow<String?> = _shouldPlaceNode.asStateFlow()

    // Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    // Dialog Data
    private val _dialogData = MutableStateFlow<Pair<Int, QuestStatus> >(Pair(0, QuestStatus.WAIT))
    val dialogData: StateFlow<Pair<Int, QuestStatus> > = _dialogData
    private var dialogCallback: ((Boolean) -> Unit)? = null

    init {
        viewModelScope.launch {
            locationManager.startLocationUpdates().collect { location ->
                updateNearestNPC(location)
            }
        }
    }

    fun addQuest(id: String, state: QuestStatus) {
        val updatedMap = _anchorNodes.value.toMutableMap().apply {
            put(id, state)
        }
        _anchorNodes.value = updatedMap
    }

    fun updateQuestState(id: String, newState: QuestStatus) {
        viewModelScope.launch {
            val updatedMap = _anchorNodes.value.toMutableMap().apply {
                this[id] = newState
            }
            _anchorNodes.value = updatedMap
        }
    }

    fun removeQuest(id: String) {
        viewModelScope.launch {
            val updatedMap = _anchorNodes.value.toMutableMap().apply {
                remove(id)
            }
            _anchorNodes.value = updatedMap
        }
    }

    fun getQuestState(id: String): QuestStatus? {
        return _anchorNodes.value[id]
    }

    fun getAllNpcMarker() {
        _npcMarketNodes.value = npcs
    }

    fun removeNpcMarker(id: String) {
        viewModelScope.launch {
            val updatedMap = _npcMarketNodes.value.toMutableMap().apply {
                remove(id)
            }
            _npcMarketNodes.value = updatedMap
        }
    }

    fun updateIsPlaceNPC(id: String, newIsPlaceValue: Boolean) {
        _npcMarketNodes.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[id]?.let { npcLocation ->
                    this[id] = npcLocation.copy(isPlace = newIsPlaceValue)
                }
            }
        }
    }

    fun getIsPlaceNPC(id: String): Boolean {
        return _npcMarketNodes.value[id]?.isPlace ?: false
    }

    private fun updateNearestNPC(location: Location) {
        viewModelScope.launch {
            val nearestNPC = locationManager.findNearestNPC(location, npcMarketNodes.value)
            _nearestNPC.value = nearestNPC

            val nearestDistance =
                nearestNPC?.let { locationManager.measureNearestNpcDistance(location, it) }

            _nearestNPCDistance.value = nearestDistance

            processLocation()
        }
    }

    private fun processLocation() {
        val npcId = nearestNPC.value?.id ?: ""
        val isPlaceNPC = getIsPlaceNPC(npcId)

        if (npcId.isNotBlank() &&
            !isPlaceNPC &&
            (locationManager.currentLocation.value?.accuracy ?: 100.0f) <= 100.0f &&
            (nearestNPCDistance.value ?: 100f) <= 100.0f
        ) {
            _shouldPlaceNode.value = npcId
        } else {
            _shouldPlaceNode.value = null
        }
    }

    fun updateShouldPlaceNode(state: String?) {
        _shouldPlaceNode.value = state
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

    override fun onCleared() {
        super.onCleared()

        locationManager.stopLocationUpdates()
    }
}