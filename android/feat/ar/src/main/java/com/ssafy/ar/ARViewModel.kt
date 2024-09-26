package com.ssafy.ar

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.ssafy.ar.data.NPCLocation
import com.ssafy.ar.data.NearestNPCInfo
import com.ssafy.ar.data.QuestData
import com.ssafy.ar.data.QuestStatus
import com.ssafy.ar.dummy.npcs
import com.ssafy.ar.manager.ARLocationManager
import com.ssafy.ar.manager.ARNodeManager
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.node.ImageNode
import io.github.sceneview.node.ModelNode
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
                    val nearestNPCInfo = locationManager.operateNearestNPC(location, npcMarkers.value)

                    updateNearestNPC(nearestNPCInfo)

                    locationManager.setFusedLocationClient(nearestNPCInfo.distance ?: 100f)
                }
            }
        }
    }

    private val placingNodes = Collections.synchronizedSet(mutableSetOf<String>())

    // 모든 NPC 정보
    private val _npcMarkers = MutableStateFlow<Map<String, NPCLocation>>(emptyMap())
    val npcMarkers = _npcMarkers.asStateFlow()

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

    fun getNpcMarker(id: String): Boolean {
        return _npcMarkers.value[id]?.isPlace ?: false
    }

    fun getAllNpcMarker() {
        _npcMarkers.value = npcs
    }

    private fun updateNpcMarker(id: String) {
        _npcMarkers.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[id]?.let { npcLocation ->
                    this[id] = npcLocation.copy(isPlace = true)
                }
            }
        }
    }

    private fun removeNpcMarker(id: String) {
        viewModelScope.launch {
            val updatedMap = _npcMarkers.value.toMutableMap().apply {
                remove(id)
            }
            _npcMarkers.value = updatedMap
        }
    }

    private fun updateNearestNPC(nearestNPCInfo: NearestNPCInfo) {
        _nearestNPCInfo.value = nearestNPCInfo
    }

    fun addAnchorNode(plane: Plane, pose: Pose, npcId: String, childNodes: SnapshotStateList<Node>) {
        if (getNpcMarker(npcId) || !placingNodes.add(npcId)) return

        viewModelScope.launch {
            nodeManager.placeNode(
                plane = plane,
                pose = pose,
                anchorId = npcId,
                childNodes = childNodes,
                onSuccess = {
                    addQuest(npcId, QuestStatus.WAIT)

                    updateNpcMarker(npcId)

                    locationManager.currentLocation.value?.let {
                        val nearestNPCInfo = locationManager.operateNearestNPC(it, npcMarkers.value)

                        updateNearestNPC(nearestNPCInfo)

                        locationManager.setFusedLocationClient(nearestNPCInfo.distance ?: 100f)
                    }
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
                parentAnchor = parentAnchorNode,
                questId = quest.id,
                questModel = quest.model,
            )
        }
    }

    fun updateModelNode(childNode: ImageNode,
                        parentNode: ModelNode
    ) {
        viewModelScope.launch {
            nodeManager.updateModelNode(
                childNode = childNode,
                parentNode = parentNode,
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