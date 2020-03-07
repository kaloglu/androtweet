package com.zsk.androtweet.ui.activities.base

import androidx.navigation.findNavController
import com.kaloglu.library.ui.BaseActivity
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class ATBaseActivity(resourceLayoutId: Int) : BaseActivity(resourceLayoutId) {
    override val application by lazy { getApplication() as AndroTweetApp }

    internal val activeFragment by lazy { supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.primaryNavigationFragment }
    internal val navController by lazy { findNavController(navHostFragment) }
    internal val viewModelStoreOwner by lazy { navController.getViewModelStoreOwner(navGraph) }

    private val navHostFragment = R.id.nav_host_fragment
    private val navGraph = R.id.nav_graph

}
