package com.zsk.androtweet.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.zsk.androtweet.AndroTweetApp.Companion.daysAgo
import com.zsk.androtweet.AndroTweetApp.Companion.tweetId
import com.zsk.androtweet.Main.Companion.selectedCountChange
import com.zsk.androtweet.Models.Tweet
import com.zsk.androtweet.R

class TweetAdapter(paramContext: Context, paramInt: Int, paramList: List<Tweet>)
    : ArrayAdapter<Tweet?>(paramContext, paramInt, paramList) {
    private val tweetList: List<Tweet> = paramList
    @JvmField
    var isSelectedPos: BooleanArray = BooleanArray(500)
    val selectedCount: Int
        get() {
            var count = 0
            for (i in isSelectedPos.indices) {
                if (isSelectedPos[i]) count += 1
            }
            return count
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var row = convertView
        val tweetVH: TweetViewHolder
        if (row == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            row = layoutInflater.inflate(R.layout.tweets_layout, null)
            tweetVH = TweetViewHolder()
            tweetVH.txtTweet = row.findViewById(R.id.tweet_on_tweets)
            tweetVH.rtTweet = row.findViewById(R.id.rtCount_on_tweets)
            tweetVH.favTweet = row.findViewById(R.id.favCount_on_tweets)
            tweetVH.timeTweet = row.findViewById(R.id.time_on_tweets)
            tweetVH.chkTweet = row.findViewById(R.id.chkTweet)
            row.tag = tweetVH
        } else {
            tweetVH = row.tag as TweetViewHolder
        }
        val tweet = tweetList[position]
        tweetVH.chkTweet!!.setOnClickListener { view ->
            val c = view as CheckBox
            val isChecked = c.isChecked
            isSelectedPos[position] = isChecked
        }
        tweetVH.chkTweet!!.setOnCheckedChangeListener { buttonView, isChecked ->
            isSelectedPos[position] = isChecked
            selectedCountChange(selectedCount)
        }
        //        tweetVH.chkTweet.setText(String.valueOf(tweet.getId()));
        tweetVH.txtTweet!!.text = tweet.tweetText
        tweetVH.rtTweet!!.text = tweet.rTcount.toString()
        tweetVH.favTweet!!.text = tweet.faVcount.toString()
        tweetVH.timeTweet!!.text = DateUtils.getRelativeTimeSpanString(tweet.time)
        tweetVH.chkTweet!!.isChecked = isSelectedPos[position]
        if (tweetId != null && tweet.id.toString() == tweetId && daysAgo < 4) {
            tweetVH.chkTweet!!.visibility = View.INVISIBLE
            isSelectedPos[position] = false
        }
        return row
    }

    inner class TweetViewHolder {
        var chkTweet: CheckBox? = null
        //        var favImage: ImageView? = null
        var favTweet: TextView? = null
        //        var rtImage: ImageView? = null
        var rtTweet: TextView? = null
        var timeTweet: TextView? = null
        var txtTweet: TextView? = null
    }

    companion object {
        private const val TAG = "ANDROTWEET"
    }

}