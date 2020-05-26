package com.zsk.androtweet.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import com.kaloglu.library.viewmodel.BaseViewModelFactory
import com.zsk.androtweet.R

@MainThread
inline fun <reified VM : ViewModel> Fragment.navGraphViewModels(
        noinline factoryProducer: (() -> BaseViewModelFactory<*>)? = null
): Lazy<VM> {
    val backStackEntry by lazy {
        findNavController().getBackStackEntry(R.id.nav_graph)
    }
    val storeProducer: () -> ViewModelStore = {
        backStackEntry.viewModelStore
    }
    return createViewModelLazy(VM::class, storeProducer, {
        factoryProducer?.invoke() ?: backStackEntry.defaultViewModelProviderFactory
    })
}