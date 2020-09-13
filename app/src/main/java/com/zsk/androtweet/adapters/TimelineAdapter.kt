package com.zsk.androtweet.adapters

import androidx.databinding.ViewDataBinding
import com.kaloglu.library.databinding4vm.adapter.DataBoundListAdapter
import com.zsk.androtweet.BR
import com.zsk.androtweet.models.TweetFromDao

class TimelineAdapter : DataBoundListAdapter<TweetFromDao>() {

    override fun setBindVariable(viewDataBinding: ViewDataBinding, recylerItem: TweetFromDao) {
        viewDataBinding.setVariable(BR.tweet, recylerItem)
    }

}