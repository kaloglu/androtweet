package com.zsk.androtweet.adapters.viewholders

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.kaloglu.library.BaseViewHolder
import com.kaloglu.library.show
import com.zsk.androtweet.models.Tweet
import kotlinx.android.synthetic.main.timeline_tweets.view.*

class TweetViewHolder(itemView: View) : BaseViewHolder<Tweet>(itemView) {
    @SuppressLint("SetJavaScriptEnabled")
    override fun bindItem(item: Tweet) {
        with(itemView) {
            tweet.text = item.tweetText
            check_button.show(item.isRemoved)
            check_button.setOnClickListener {
                itemView.context.openChrome("https://twitter.com/${item.username}")
            }
            chkTweet.show(!item.isRemoved)
            chkTweet.isChecked = item.isSelected
            chkTweet.setOnClickListener {
                item.isSelected = !item.isSelected
            }
        }
    }

}

fun Context.openBrowser(packageName: String?, url: String, onError: (() -> Unit)? = null) {
    try {
        startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    setPackage(packageName)
                }
        )
    } catch (e: ActivityNotFoundException) {
        onError?.invoke()
    }
}

fun Context.openChrome(url: String, onError: (() -> Unit)? = null) {
    openBrowser("com.android.chrome", url) {
        openBrowser("com.android.beta", url) {
            openBrowser("com.android.dev", url) {
                openBrowser("com.android.canary", url) {
                    onError?.invoke() ?: openBrowser(null, url)
                }
            }
        }
    }
}