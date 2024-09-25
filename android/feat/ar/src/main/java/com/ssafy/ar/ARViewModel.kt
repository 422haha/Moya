package com.ssafy.ar

import android.location.Location
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ar.core.Frame
import com.ssafy.ar.data.NPCLocation
import com.ssafy.ar.data.NearestNPCInfo
import com.ssafy.ar.data.QuestData
import com.ssafy.ar.data.QuestStatus
import com.ssafy.ar.dummy.npcs
import com.ssafy.ar.manager.ARLocationManager
import com.ssafy.ar.manager.ARNodeManager
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.node.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections

class ARViewModel(
    private val nodeManager: ARNodeManager,
    private val locationManager: ARLocationManager,
) : ViewModel() {

    init {
        viewModelScope.launch {
            locationManager.currentLocation.collectLatest { location ->
                location?.let {
                    operateNearestNPC(location)
                }
            }
        }
    }

    private val placingNodes = Collections.synchronizedSet(mutableSetOf<String>())

    // 모든 NPC 정보
    private val _npcMarkets = MutableStateFlow<Map<String, NPCLocation>>(emptyMap())
    val npcMarkets = _npcMarkets.asStateFlow()

    // 현재 배치된 퀘스트
    private val _questNodes = MutableStateFlow<Map<String, QuestStatus>>(emptyMap())
    val questNodes = _questNodes.asStateFlow()

    // 가장 가까운 노드
    private val _nearestNPCInfo = MutableStateFlow(NearestNPCInfo())
    val nearestNPCInfo = _nearestNPCInfo.asStateFlow()

    // Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    // Dialog Data
    private val _dialogData = MutableStateFlow<Pair<Int, QuestStatus>>(Pair(0, QuestStatus.WAIT))
    val dialogData: StateFlow<Pair<Int, QuestStatus>> = _dialogData
    private var dialogCallback: ((Boolean) -> Unit)? = null

    fun getQuest(id: String): QuestStatus? {
        return _questNodes.value[id]
    }

    private fun addQuest(id: String, state: QuestStatus) {
        val updatedMap = _questNodes.value.toMutableMap().apply {
            put(id, state)
        }
        _questNodes.value = updatedMap
    }

    fun updateQuest(id: String, newState: QuestStatus) {
        viewModelScope.launch {
            val updatedMap = _questNodes.value.toMutableMap().apply {
                this[id] = newState
            }
            _questNodes.value = updatedMap
        }
    }

    fun removeQuest(id: String) {
        viewModelScope.launch {
            val updatedMap = _questNodes.value.toMutableMap().apply {
                remove(id)
            }
            _questNodes.value = updatedMap
        }
    }

    private fun getNpcMarker(id: String): Boolean {
        return _npcMarkets.value[id]?.isPlace ?: false
    }

    fun getAllNpcMarker() {
        _npcMarkets.value = npcs
    }

    private fun updateNpcMarker(id: String) {
        _npcMarkets.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[id]?.let { npcLocation ->
                    this[id] = npcLocation.copy(isPlace = true)
                }
            }
        }
    }

    private fun removeNpcMarker(id: String) {
        viewModelScope.launch {
            val updatedMap = _npcMarkets.value.toMutableMap().apply {
                remove(id)
            }
            _npcMarkets.value = updatedMap
        }
    }

    private fun operateNearestNPC(location: Location) {
        val npc = locationManager.findNearestNPC(location, npcMarkets.value)
        val distance = npc?.let { locationManager.measureNearestNpcDistance(location, it) }
        val isAvailable = locationManager.isAvailableNearestNPC(distance, location)

        updateNearestNPC(NearestNPCInfo(npc, distance, isAvailable))
    }

    private fun updateNearestNPC(nearestNPCInfo: NearestNPCInfo) {
        _nearestNPCInfo.value = nearestNPCInfo
    }

    fun addAnchorNode(npcId: String, frame: Frame, childNodes: SnapshotStateList<Node>) {
        if (getNpcMarker(npcId) || !placingNodes.add(npcId)) return

        viewModelScope.launch {
            nodeManager.placeNode(
                npcId,
                frame,
                childNodes = childNodes,
                onSuccess = {
                    addQuest(npcId, QuestStatus.WAIT)

                    updateNpcMarker(npcId)

                    updateNearestNPC(NearestNPCInfo())
                }
            )

            placingNodes.remove(npcId)
        }
    }

    fun updateAnchorNode(node: Node,
                         quest: QuestData,
                         parentAnchorNode: AnchorNode) {
        viewModelScope.launch {
            nodeManager.updateAnchorNode(
                prevNode = node,
                questId = quest.id,
                questModel = quest.model,
                parentAnchor = parentAnchorNode,
            )
        }
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