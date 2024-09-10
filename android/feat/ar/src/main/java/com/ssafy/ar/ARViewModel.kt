package com.example.arcoretest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ARViewModel : ViewModel() {
    private val _arNodes = MutableStateFlow<List<ARNode>>(emptyList())
    val arNodes: StateFlow<List<ARNode>> = _arNodes.asStateFlow()

    fun setARNodes(nodes: List<ARNode>) {
        _arNodes.value = nodes
    }

    fun updateNodeStatus(id: String, isActive: Boolean) {
        _arNodes.update { nodes ->
            nodes.map { node ->
                if (node.id == id) node.copy(isActive = isActive) else node
            }
        }
    }
}