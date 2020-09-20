package com.zsk.androtweet.utils.extensions

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.navGraphViewModels
import com.zsk.androtweet.R
import com.zsk.androtweet.viewmodels.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@MainThread
inline fun <reified VM : ViewModel> Fragment.navGraphViewModels(
        noinline factoryProducer: (() -> ViewModelFactory)? = null
): Lazy<VM> = navGraphViewModels(R.id.nav_graph, factoryProducer)
