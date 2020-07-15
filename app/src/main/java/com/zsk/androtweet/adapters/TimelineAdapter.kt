package com.zsk.androtweet.adapters

import androidx.databinding.ViewDataBinding
import com.kaloglu.library.databinding4vm.adapter.DataBoundPagedListAdapter
import com.zsk.androtweet.BR
import com.zsk.androtweet.models.SelectableTweet

class TimelineAdapter : DataBoundPagedListAdapter<SelectableTweet>() {

    override fun setBindVariable(viewDataBinding: ViewDataBinding, recylerItem: SelectableTweet) {
        viewDataBinding.setVariable(BR.selectableTweet, recylerItem)
    }

}