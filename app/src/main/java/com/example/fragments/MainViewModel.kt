package com.example.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val actionsFlow = MutableSharedFlow<MainAction>()
    val actions: Flow<MainAction> = actionsFlow

    fun makeAction(action: MainAction) {
        viewModelScope.launch { actionsFlow.emit(action) }
    }
}

sealed class MainAction {
    class ShowDetailsFragment(val noteId: Long): MainAction()
    data object RemoveDetailsFragment: MainAction()
}