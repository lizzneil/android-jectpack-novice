package com.gabe.navigateapplication.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gabe.navigateapplication.network.RetroService
import dagger.Module
import javax.inject.Inject


class RecyclerViewModelFactory (private val retroService: RetroService ,private val stateHandle: SavedStateHandle) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecyclerViewViewModel(retroService,stateHandle) as T
    }

}