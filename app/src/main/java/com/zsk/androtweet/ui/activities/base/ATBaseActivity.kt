package com.zsk.androtweet.ui.activities.base

import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.kaloglu.library.ui.BaseActivity
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.R
import com.zsk.androtweet.viewmodels.LoginViewModel
import com.zsk.androtweet.viewmodels.LoginViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class ATBaseActivity(resourceLayoutId: Int) : BaseActivity(resourceLayoutId) {
    override val application by lazy { getApplication() as AndroTweetApp }

    internal val activeFragment by lazy {
        val fragmentManager = supportFragmentManager.primaryNavigationFragment?.childFragmentManager
        var fragmentSize = fragmentManager?.fragments?.size?.minus(1) ?: 0
        if (fragmentSize < 0)
            fragmentSize = 0

        return@lazy fragmentManager?.fragments?.get(fragmentSize)
    }

    internal val navController by lazy { findNavController(navHostFragment) }
    internal val viewModelStoreOwner by lazy { navController.getViewModelStoreOwner(navGraph) }

    val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(lifecycle) }

    private val navHostFragment = R.id.nav_host_fragment
    private val navGraph = R.id.nav_graph

}
