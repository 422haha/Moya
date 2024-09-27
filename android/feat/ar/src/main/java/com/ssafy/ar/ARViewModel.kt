package com.ssafy.ar

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.filament.Engine
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.ssafy.ar.data.NPCInfo
import com.ssafy.ar.data.QuestInfo
import com.ssafy.ar.data.NearestNPCInfo
import com.ssafy.ar.data.QuestState
import com.ssafy.ar.dummy.models
import com.ssafy.ar.dummy.npcs
import com.ssafy.ar.dummy.quests
import com.ssafy.ar.manager.ARLocationManager
import com.ssafy.ar.manager.ARNodeManager
import com.ssafy.network.repository.ExplorationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
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
import javax.inject.Inject

@HiltViewModel
class ARViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val explorationRepository: ExplorationRepository
) : ViewModel() {

    private lateinit var nodeManager: ARNodeManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationManager: ARLocationManager

    init {
        viewModelScope.launch {
            nodeManager = ARNodeManager()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            locationManager = ARLocationManager(context, fusedLocationClient)

            locationManager.currentLocation.collectLatest { location ->
                location?.let {
                    val nearestNPCInfo = locationManager.operateNearestNPC(location, questInfos.value)

                    updateNearestNPC(nearestNPCInfo)

                    locationManager.setFusedLocationClient(nearestNPCInfo.distance ?: 100f)
                }
            }
        }
    }

    private val placingNodes = Collections.synchronizedSet(mutableSetOf<Long>())

    // 모든 퀘스트 정보
    private val _questInfos = MutableStateFlow<Map<Long, QuestInfo>>(emptyMap())
    val questInfos = _questInfos.asStateFlow()

    // 모든 NPC 스크립트 정보
    private val _npcInfos = MutableStateFlow<Map<Long, NPCInfo>>(emptyMap())
    val npcInfos = _npcInfos.asStateFlow()

    // 가장 가까운 노드
    private val _nearestQuestInfo = MutableStateFlow(NearestNPCInfo())
    val nearestQuestInfo = _nearestQuestInfo.asStateFlow()

    // Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    // Dialog Data
    private val _dialogData = MutableStateFlow<Pair<Long, QuestState>>(Pair(0, QuestState.WAIT))
    val dialogData: StateFlow<Pair<Long, QuestState>> = _dialogData
    private var dialogCallback: ((Boolean) -> Unit)? = null

    fun getQuestState(id: Long): Boolean? {
        return _questInfos.value[id]?.isPlace
    }

    fun getAllNPCs() {
        _npcInfos.value = npcs

        viewModelScope.launch {
            // TODO. 퀘스트 정보 불러오기
        }
    }

    fun getAllQuests() {
        _questInfos.value = quests
        
        viewModelScope.launch { 
            // TODO. 퀘스트 정보 불러오기
        }
    }

    fun updateQuestState(id: Long, state: QuestState) {
        _questInfos.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[id]?.let { npcLocation ->
                    this[id] = npcLocation.copy(isComplete = state)
                }
            }
        }
    }

    fun updateNearestNPC(nearestNPCInfo: NearestNPCInfo) {
        _nearestQuestInfo.value = nearestNPCInfo
    }

    fun addAnchorNode(plane:
                      Plane,
                      pose: Pose,
                      questInfo: QuestInfo,
                      childNodes: SnapshotStateList<Node>,
                      engine: Engine,
                      modelLoader: ModelLoader,
                      materialLoader: MaterialLoader) {
        if (getQuestState(questInfo.npcId) == true || !placingNodes.add(questInfo.npcId)) return

        viewModelScope.launch {
            nodeManager.placeNode(
                plane = plane,
                pose = pose,
                questInfo = questInfo,
                childNodes = childNodes,
                engine = engine,
                modelLoader = modelLoader,
                materialLoader = materialLoader,
                onSuccess = {
                    updateQuestState(questInfo.npcId, QuestState.WAIT)

                    locationManager.currentLocation.value?.let {
                        val nearestNPCInfo = locationManager.operateNearestNPC(it, questInfos.value)

                        updateNearestNPC(nearestNPCInfo)
                    }
                }
            )

            placingNodes.remove(questInfo.npcId)
        }
    }

    fun updateAnchorNode(node: Node,
                         quest: QuestInfo,
                         parentAnchorNode: AnchorNode,
                         modelLoader: ModelLoader,
                         materialLoader: MaterialLoader) {
        viewModelScope.launch {
            nodeManager.updateAnchorNode(
                prevNode = node,
                parentAnchor = parentAnchorNode,
                questId = quest.id,
                questModel = models[quest.npcId]?.modelUrl ?: "models/quest.glb",
                modelLoader = modelLoader,
                materialLoader = materialLoader
            )
        }
    }

    fun updateModelNode(childNode: ImageNode,
                        parentNode: ModelNode,
                        materialLoader: MaterialLoader
    ) {
        viewModelScope.launch {
            nodeManager.updateModelNode(
                childNode = childNode,
                parentNode = parentNode,
                materialLoader = materialLoader
            )
        }
    }

    fun showQuestDialog(index: Long, state: QuestState, callback: (Boolean) -> Unit) {
        _dialogData.value = Pair(index, state)
        _showDialog.value = true
        dialogCallback = callback
    }

    fun onDialogConfirm() {
        _showDialog.value = false
        _dialogData.value = Pair(0, QuestState.WAIT)
        dialogCallback?.invoke(true)
        dialogCallback = null
    }

    fun onDialogDismiss() {
        _showDialog.value = false
        _dialogData.value = Pair(0, QuestState.WAIT)
        dialogCallback?.invoke(false)
        dialogCallback = null
    }

    override fun onCleared() {
        super.onCleared()

        locationManager.stopLocationUpdates()
    }
}