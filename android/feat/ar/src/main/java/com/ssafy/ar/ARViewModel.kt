package com.ssafy.ar

import android.content.Context
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.filament.Engine
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.ssafy.ar.data.ModelType
import com.ssafy.ar.data.ScriptInfo
import com.ssafy.ar.data.QuestInfo
import com.ssafy.ar.data.NearestNPCInfo
import com.ssafy.ar.data.QuestState
import com.ssafy.ar.dummy.scripts
import com.ssafy.ar.dummy.quests
import com.ssafy.ar.manager.ARLocationManager
import com.ssafy.ar.manager.ARNodeManager
import com.ssafy.network.ApiResponse
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
                    val nearestNPCInfo =
                        locationManager.operateNearestNPC(location, questInfos.value)

                    updateNearestNPC(nearestNPCInfo)

                    locationManager.setFusedLocationClient(nearestNPCInfo.distance ?: 100f)
                }
            }
        }
    }

    private val placingNodes = Collections.synchronizedSet(mutableSetOf<Long>())

    // 모든 Quest 정보
    private val _questInfos = MutableStateFlow<Map<Long, QuestInfo>>(emptyMap())
    val questInfos = _questInfos.asStateFlow()

    // 모든 Script 정보
    private val _scriptInfos = MutableStateFlow<Map<Int, ScriptInfo>>(emptyMap())
    val scriptInfos = _scriptInfos.asStateFlow()

    // 가장 가까운 NPC
    private val _nearestQuestInfo = MutableStateFlow(NearestNPCInfo())
    val nearestQuestInfo = _nearestQuestInfo.asStateFlow()

    // Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    // Dialog Data
    private val _dialogData = MutableStateFlow(Pair(ScriptInfo(), QuestState.WAIT))
    val dialogData: StateFlow<Pair<ScriptInfo, QuestState>> = _dialogData
    private var dialogCallback: ((Boolean) -> Unit)? = null

    fun getIsPlaceQuest(id: Long): Boolean? {
        return _questInfos.value[id]?.isPlace
    }

    // TODO
    fun getAllQuests(explorationId: Long) {
        viewModelScope.launch {
            explorationRepository.getQuestList(explorationId).collectLatest { response ->
                when(response) {
                    is ApiResponse.Success -> {
                        response.body?.let { body ->
                            _questInfos.value = body.quest.associate { quest ->
                                quest.questId to QuestInfo(
                                    id = quest.questId,
                                    npcId = quest.npcId,
                                    npcPosId = quest.npcPosId,
                                    questType = quest.questType,
                                    latitude = quest.latitude,
                                    longitude = quest.longitude,
                                    speciesId = quest.speciesId.toString(),
                                    speciesName = quest.speciesName,
                                    isComplete = when (quest.completed) {
                                        0 -> QuestState.WAIT
                                        1 -> QuestState.PROGRESS
                                        2 -> QuestState.COMPLETE
                                        else -> QuestState.WAIT
                                    },
                                    isPlace = false
                                )
                            }
                        } ?: "Failed to load initial data"
                    }
                    is ApiResponse.Error -> {
                        response.errorMessage?: "Unknown error"
                    }
                }
            }
        }
        _questInfos.value = quests
    }

    // TODO
    fun getAllScripts() {
        _scriptInfos.value = scripts
    }

    private fun updateIsPlaceQuest(id: Long, state: Boolean) {
        _questInfos.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[id]?.let { npcLocation ->
                    this[id] = npcLocation.copy(isPlace = state)
                }
            }
        }
    }

    // TODO
    fun updateQuestState(id: Long, state: QuestState) {
        _questInfos.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[id]?.let { npcLocation ->
                    this[id] = npcLocation.copy(isComplete = state)
                }
            }
        }
    }

    private fun updateNearestNPC(nearestNPCInfo: NearestNPCInfo) {
        _nearestQuestInfo.value = nearestNPCInfo
    }

    fun placeNode(
        plane: Plane,
        pose: Pose,
        questInfo: QuestInfo,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        childNodes: SnapshotStateList<Node>
    ) {
        if (getIsPlaceQuest(questInfo.id) == true || !placingNodes.add(questInfo.id)) return

        viewModelScope.launch {
            nodeManager.placeNode(
                plane = plane,
                pose = pose,
                questInfo = questInfo,
                engine = engine,
                modelLoader = modelLoader,
                materialLoader = materialLoader,
                childNodes = childNodes,
                onSuccess = {
                    updateIsPlaceQuest(questInfo.id, true)

                    locationManager.currentLocation.value?.let {
                        val nearestNPCInfo = locationManager.operateNearestNPC(it, questInfos.value)

                        updateNearestNPC(nearestNPCInfo)
                    }
                }
            )

            placingNodes.remove(questInfo.id)
        }
    }

    private fun getModelUrl(id: Long): String {
        return ModelType.fromId(id).modelUrl
    }

    fun updateAnchorNode(
        questInfo: QuestInfo,
        childNode: ModelNode,
        parentNode: AnchorNode,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader
    ) {
        viewModelScope.launch {
            nodeManager.updateAnchorNode(
                questId = questInfo.id,
                questModel = getModelUrl(questInfo.id),
                childNode = childNode,
                parentNode = parentNode,
                modelLoader = modelLoader,
                materialLoader = materialLoader
            )
        }
    }

    fun updateModelNode(
        childNode: ImageNode,
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

    fun showQuestDialog(script: ScriptInfo?, state: QuestState, callback: (Boolean) -> Unit) {
        _dialogData.value = Pair(script ?: ScriptInfo(), state)
        _showDialog.value = true
        dialogCallback = callback
    }

    fun onDialogConfirm() {
        _showDialog.value = false
        _dialogData.value = Pair(ScriptInfo(), QuestState.WAIT)
        dialogCallback?.invoke(true)
        dialogCallback = null
    }

    fun onDialogDismiss() {
        _showDialog.value = false
        _dialogData.value = Pair(ScriptInfo(), QuestState.WAIT)
        dialogCallback?.invoke(false)
        dialogCallback = null
    }

    override fun onCleared() {
        super.onCleared()

        locationManager.stopLocationUpdates()
    }
}