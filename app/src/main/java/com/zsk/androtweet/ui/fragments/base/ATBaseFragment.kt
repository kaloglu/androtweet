package com.zsk.androtweet.ui.fragments.base

import androidx.databinding.ViewDataBinding
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.BindingFragment
import com.zsk.androtweet.ui.activities.base.ATBaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi

@FlowPreview
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class ATBaseFragment<VDB : ViewDataBinding, VM : BindableViewModel<*>>(resourceLayoutId: Int)
    : BindingFragment<VDB, VM>(resourceLayoutId) {

    override val activity by lazy { getActivity() as ATBaseActivity }
    override val application by lazy { activity.application }
    internal val navController by lazy { activity.navController }
//    internal val viewModelStoreOwner by lazy { activity.viewModelStoreOwner }
//    abstract val viewModelFactory: RepositoryViewModelFactory<AndroTweetApp>
}
