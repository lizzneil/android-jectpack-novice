package com.gabe.navigateapplication.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gabe.navigateapplication.network.RetroService
import dagger.Module
import javax.inject.Inject

//@Module
//@Inject constructor
class RecyclerViewModelFactory (private val retroService: RetroService) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecyclerViewViewModel(retroService) as T
    }

}