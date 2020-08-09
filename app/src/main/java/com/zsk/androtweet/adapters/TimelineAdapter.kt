package com.zsk.androtweet.adapters

import androidx.databinding.ViewDataBinding
import com.kaloglu.library.databinding4vm.adapter.DataBoundPagingDataAdapter
import com.zsk.androtweet.BR
import com.zsk.androtweet.models.TweetFromDao

class TimelineAdapter : DataBoundPagingDataAdapter<TweetFromDao>() {

    override fun setBindVariable(viewDataBinding: ViewDataBinding, recylerItem: TweetFromDao) {
        viewDataBinding.setVariable(BR.tweet, recylerItem)
    }

}