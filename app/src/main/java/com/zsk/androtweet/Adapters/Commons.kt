package com.zsk.androtweet.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ListView
import android.widget.Toast
import com.zsk.androtweet.Database.DB_Model
import com.zsk.androtweet.Dialog.CustomDialog
import com.zsk.androtweet.Models.Tweet
import com.zsk.androtweet.R
import twitter4j.Paging
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

object Commons {
    private const val TAG = "ANDROTWEET"
    var auth_dialog: Dialog? = null
    var oauth_url: String? = null
    var oauth_verifier: String? = null
    var pref_AndroTweet: SharedPreferences? = null
    val twitterObject: Twitter by lazy {
        val localConfigurationBuilder = ConfigurationBuilder()
        localConfigurationBuilder.setOAuthConsumerKey(pref_AndroTweet!!.getString("CONSUMER_KEY", ""))
        localConfigurationBuilder.setOAuthConsumerSecret(pref_AndroTweet!!.getString("CONSUMER_SECRET", ""))
        localConfigurationBuilder.setDebugEnabled(true).setJSONStoreEnabled(true)
        val localAccessToken = AccessToken(pref_AndroTweet!!.getString("ACCESS_TOKEN", ""), pref_AndroTweet!!.getString("ACCESS_TOKEN_SECRET", ""))
        return@lazy TwitterFactory(localConfigurationBuilder.build()).getInstance(localAccessToken)
    }
    var web: WebView? = null

    @JvmStatic
    @JvmOverloads
    fun showInfo(context: Context?, msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(context, msg, duration)
        toast.duration = duration
        toast.setGravity(17, 0, 0)
        toast.show()
    }

    fun deleteSelected(context: Context, theAdapter: TweetAdapter?) {
        if (theAdapter == null || theAdapter.selectedCount == 0) {
            Toast.makeText(context, "Please select tweets less one or more...", Toast.LENGTH_SHORT).show()
            return
        }
        DeleteTweets(theAdapter, context).execute()
    }

    private fun getProgress(
            context: Context?,
            title: String? = "Progressing...",
            maxProgress: Int = 1,
            progressStyle: Int = ProgressDialog.STYLE_HORIZONTAL,
            message: String? = null
    ): ProgressDialog {
        val progressdialog = ProgressDialog(context)
        progressdialog.setTitle(title)
        progressdialog.max = maxProgress
        progressdialog.setProgressStyle(progressStyle)
        progressdialog.isIndeterminate = false
        progressdialog.setCanceledOnTouchOutside(false)
        if (message != null && message != "") progressdialog.setMessage(message)
        return progressdialog
    }

    fun isLogon(context: Context) {
        if (pref_AndroTweet == null) {
            pref_AndroTweet = context.getSharedPreferences(TAG, 0)
        }
        val localEditor = pref_AndroTweet!!.edit()
        localEditor.putString("CONSUMER_KEY", "d2XSeF6fXPtVClaG3DALLjMT4")
        localEditor.putString("CONSUMER_SECRET", "e9UJ6mQuLK0pkFl4OTghjjHp1NR6PqkbdqwIiwgEIR9zEXTbQU")
        localEditor.apply()
        if ("" != pref_AndroTweet!!.getString("ACCESS_TOKEN", "")) {
            return
        }
        DB_Model(context).deleteOldTweets()
        TokenGet(context).execute()
    }

    fun logOut(context: Context) {
        val localEditor = pref_AndroTweet!!.edit()
        localEditor.putString("ACCESS_TOKEN", "")
        localEditor.putString("ACCESS_TOKEN_SECRET", "")
        localEditor.apply()
        DB_Model(context).drop()
        isLogon(context)
    }

    fun refreshL(context: Context?, db_model: DB_Model, timeLine: ListView) {
        val tweetList = db_model.tweetList
        if (tweetList.size > 0) {
            val TheAdapter = TweetAdapter(context!!, R.layout.tweets_layout, tweetList)
            timeLine.adapter = TheAdapter
        } else {
            timeLine.adapter = null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun refreshTweetList(activity: Activity, timeLine: ListView = activity.findViewById<View>(R.id.tweetList_on_home) as ListView, showProgressDialog: Boolean = true) {
        LoadTweets(activity, showProgressDialog, timeLine).execute()
        Toast.makeText(activity, "Refreshed...", Toast.LENGTH_SHORT).show()
    }

    internal class AccessTokenGet(private val context: Context) : AsyncTask<String, String, Boolean>() {
        private var progress: ProgressDialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progress = getProgress(context)
            progress!!.show()
        }

        override fun doInBackground(vararg args: String): Boolean {
            try {
                val accessToken = twitterObject.getOAuthAccessToken(twitterObject.oAuthRequestToken, oauth_verifier)
                val edit = pref_AndroTweet!!.edit()
                edit.putString("ACCESS_TOKEN", accessToken.token)
                edit.putString("ACCESS_TOKEN_SECRET", accessToken.tokenSecret)
                edit.putString("userName", twitterObject.screenName).apply()
                edit.apply()
            } catch (e: TwitterException) {
                e.printStackTrace()
            }
            return true
        }

        override fun onPostExecute(response: Boolean) {
            if (response) {
                refreshTweetList(context as Activity)
                progress!!.hide()
            }
        }

    }

    private class DeleteTweets(private val theAdapter: TweetAdapter, private val context: Context) : AsyncTask<Void, Tweet, Boolean>() {
        private val DB: DB_Model = DB_Model(context)
        private val selectedItems: Int = theAdapter.selectedCount
        private var progress: ProgressDialog? = null
        override fun onPreExecute() {
            if (selectedItems > 0) {
                progress = getProgress(context, "Deleting Selected Tweets...", selectedItems)
                progress!!.show()
            }
            super.onPreExecute()
        }

        override fun doInBackground(vararg voids: Void): Boolean {
            var isDone = false
            var r = 0
            if (selectedItems < 1) return false
            for (i in theAdapter.count - 1 downTo 0) {
                if (theAdapter.isSelectedPos[i]) {
                    r++
                    try {
                        val tweet = theAdapter.getItem(i)
                        val finalR = r
                        (context as Activity).runOnUiThread {
                            progress!!.progress = finalR
                            theAdapter.remove(tweet)
                            theAdapter.isSelectedPos[i] = false
                        }
                        //                        twitterObject.getScreenName();
                        DB.deleteTweet(tweet)
                        twitterObject.destroyStatus(tweet!!.id)
                        isDone = true
                        Thread.sleep(500)
                    } catch (e: TwitterException) {
                        Log.e(TAG, e.message)
                        e.printStackTrace()
                        return checkRateLimit()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            return isDone
        }

        private fun checkRateLimit(): Boolean {
            try {
                val rateLimitStatus = twitterObject.rateLimitStatus
                for (endpoint in rateLimitStatus.keys) {
                    val status = rateLimitStatus[endpoint]
                    //                                log("Endpoint: " + endpoint);
                    if (status!!.remaining == 0) (context as Activity).runOnUiThread {
                        showInfo(context, endpoint + " limit exceeded! (" + status.remaining + "/" + status.limit + ") \n"
                                + "Please wait for " + status.secondsUntilReset / 60 + " minute(s)")
                    }
                    //                                log(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
//                                log(" SecondsUntilReset: " + status.getSecondsUntilReset());
                }
            } catch (e1: TwitterException) {
                e1.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(aBoolean: Boolean) { //            DB.deleteOldTweets();
            if (selectedItems > 0) progress!!.dismiss()
            if (aBoolean) {
                theAdapter.notifyDataSetChanged()
                val customDialog = CustomDialog(context, R.string.deleteTweets_title, R.string.deleteTweets_desc, R.string.twitter_clear_cache, 0)
                customDialog.initOkButtonClickListener { customDialog.dismiss() }
                customDialog.initActionButtonClickListener { customDialog.dismiss() }
                //                refreshTweetList((Activity) context,false);
            }
            super.onPostExecute(aBoolean)
            DB.close()
        }

        init {
            Log.e(TAG, selectedItems.toString())
        }
    }

    class LoadTweets(private val context: Context, private val showProgressDialog: Boolean, private val timeLine: ListView) : AsyncTask<Void, Void, Boolean>() {
        private val dbModel: DB_Model = DB_Model(context)
        private val p = Paging()
        private var progress: ProgressDialog? = null
        override fun onPreExecute() {
            p.count = DB_Model.HOLDED_REC_COUNT
            if (showProgressDialog) {
                progress = getProgress(context, "Loading Tweets...", p.count, ProgressDialog.STYLE_SPINNER)
                progress!!.show()
            }
            super.onPreExecute()
        }

        override fun doInBackground(vararg voids: Void): Boolean {
            try {
                dbModel.insertTweetList(twitterObject.getUserTimeline(p))
            } catch (e: Exception) {
                Log.e(TAG, e.message)
                e.printStackTrace()
            }
            return true
        }

        override fun onPostExecute(aBoolean: Boolean) { //            DB.close();
            if (showProgressDialog) progress!!.dismiss()
            refreshL(context, dbModel, timeLine)
            super.onPostExecute(aBoolean)
        }

    }

    internal class TokenGet(private val context: Context) : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg args: String): String? {
            try {
                oauth_url = twitterObject.oAuthRequestToken.authorizationURL
            } catch (e: TwitterException) {
                e.printStackTrace()
            }
            return oauth_url
        }

        @SuppressLint("SetJavaScriptEnabled")
        override fun onPostExecute(oauth_url: String?) {
            if (oauth_url != null) {
                Log.e("URL", oauth_url)
                auth_dialog = Dialog(context)
                auth_dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                auth_dialog!!.setContentView(R.layout.auth_dialog)
                web = auth_dialog!!.findViewById<View>(R.id.webv) as WebView
                web!!.settings.javaScriptEnabled = true
                web!!.loadUrl(oauth_url)
                web!!.webViewClient = object : WebViewClient() {
                    var authComplete = false

                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        if (url.contains("oauth_verifier") && !authComplete) {
                            authComplete = true
                            Log.e("Url", url)
                            val uri = Uri.parse(url)
                            oauth_verifier = uri.getQueryParameter("oauth_verifier")
                            auth_dialog!!.dismiss()
                            AccessTokenGet(context).execute()
                        } else if (url.contains("denied")) {
                            auth_dialog!!.dismiss()
                            showInfo(context, "Sorry !, Permission Denied")
                        }
                    }
                }
                auth_dialog!!.show()
                auth_dialog!!.setCancelable(true)
            } else {
                showInfo(context, "Sorry !, Network Error or Invalid Credentials")
            }
        }

    }
}