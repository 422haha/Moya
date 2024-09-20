package com.ssafy.ar

import android.location.Location
import android.system.Os.remove
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.ssafy.ar.ArData.CurrentLocation
import com.ssafy.ar.ArData.NPCLocation
import com.ssafy.ar.ArData.QuestData
import com.ssafy.ar.ArData.QuestStatus
import com.ssafy.ar.dummy.npcs
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Size
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ImageNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val kMaxModelInstances = 1

class ARViewModel : ViewModel() {
    // AR AnchorNode
    private val _anchorNodes = MutableStateFlow<Map<String, QuestStatus>>(emptyMap())
    val anchorNodes: StateFlow<Map<String, QuestStatus>> = _anchorNodes.asStateFlow()

    // AR AnchorNode
    private val _npcMarketNodes = MutableStateFlow<Map<String, NPCLocation>>(emptyMap())
    val npcMarketNodes: StateFlow<Map<String, NPCLocation>> = _npcMarketNodes.asStateFlow()

    // Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    // Dialog Data
    private val _dialogData = MutableStateFlow<Pair<Int, QuestStatus> >(Pair(0, QuestStatus.WAIT))
    val dialogData: StateFlow<Pair<Int, QuestStatus> > = _dialogData
    private var dialogCallback: ((Boolean) -> Unit)? = null

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    fun updateLocation(location: Location) {
        viewModelScope.launch {
            _currentLocation.emit(location)
        }
    }

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

    fun getAllNpcMarker() {
        _npcMarketNodes.value = npcs
    }

    fun updateNPCLocationIsPlace(id: String, newIsPlaceValue: Boolean) {
        _npcMarketNodes.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[id]?.let { npcLocation ->
                    this[id] = npcLocation.copy(isPlace = newIsPlaceValue)
                }
            }
        }
    }

    fun isNPCPlaced(id: String): Boolean {
        return _npcMarketNodes.value[id]?.isPlace ?: false
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

    suspend fun updateAnchorNode(
        prevNode: Node,
        questId: String,
        questModel: String,
        parentAnchor: AnchorNode,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstances: MutableMap<String, ModelInstance>,
    ) = withContext(Dispatchers.Main) {
        parentAnchor.removeChildNode(prevNode).also {
            val newModelInstance = modelInstances.getOrPut(questId) {
                modelLoader.createInstancedModel(
                    questModel,
                    kMaxModelInstances
                ).first()
            }

            val newModelNode = ModelNode(
                modelInstance = newModelInstance,
                scaleToUnits = 0.5f
            ).apply {
                name = questId
                position = prevNode.worldPosition
                rotation = prevNode.worldRotation
            }

            val imageNode = ImageNode(
                materialLoader = materialLoader,
                imageFileLocation = "picture/progress.png",
                size = Size(0.35f, 0.35f),
                center = Position(0f, 0.67f, 0f)
            )

            newModelNode.addChildNode(imageNode)

            parentAnchor.addChildNode(newModelNode)
        }
    }

    // 특정 위치에 3D 모델을 배치
    suspend fun createAnchorNode(
        node: QuestData,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstances: MutableMap<String, ModelInstance>,
        anchor: Anchor
    ): AnchorNode {
        val idx = (1..4).random()

        val anchorNode = AnchorNode(engine = engine, anchor = anchor).apply {
            isPositionEditable = false
            isRotationEditable = false
            isScaleEditable = false
        }

        val modelInstance = modelInstances.getOrPut(node.id) {
            modelLoader.createInstancedModel(
                node.model,
                kMaxModelInstances
            ).first()
        }

        val modelNode = ModelNode(
            modelInstance = modelInstance,
            scaleToUnits = 0.5f
        ).apply {
            name = idx.toString()
            rotation = Rotation(0f, 180f, 0f)
        }

        val imageNode = ImageNode(
            materialLoader = materialLoader,
            imageFileLocation = "picture/wait.png",
            size = Size(0.35f, 0.35f),
            center = Position(0f, 0.65f, 0f)
        )

        modelNode.addChildNode(imageNode)

        anchorNode.addChildNode(modelNode)

        return anchorNode
    }
}