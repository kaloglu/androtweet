package com.zsk.androtweet.adapters

import androidx.databinding.ViewDataBinding
import com.kaloglu.library.databinding4vm.adapter.BoundViewHolder
import com.kaloglu.library.databinding4vm.adapter.DataBoundRecyclerAdapter
import com.zsk.androtweet.BR
import com.zsk.androtweet.models.TweetWithUser

class TimelineAdapter : DataBoundRecyclerAdapter<TweetWithUser>() {

    override fun setBindVariable(viewDataBinding: ViewDataBinding, recylerItem: TweetWithUser) {
        viewDataBinding.setVariable(BR.tweetWithUser, recylerItem)
    }

    fun selectAll() {

    }

}