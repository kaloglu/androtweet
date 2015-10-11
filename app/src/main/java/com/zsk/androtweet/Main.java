package com.zsk.androtweet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.zsk.androtweet.Adapters.Commons;
import com.zsk.androtweet.Adapters.TweetAdapter;
import com.zsk.androtweet.Database.DB_Model;
import com.zsk.androtweet.Models.Search;

import static com.zsk.androtweet.Adapters.Commons.deleteSelected;
import static com.zsk.androtweet.Adapters.Commons.refreshTweetList;

public class Main extends Activity {
    Search search;
    private static final String TAG ="ANDROTWEET" ;
    private ListView TweetList;
    private CheckBox chk_All,chk_MyTweets,chk_Mentions,chk_RTs;
    TweetAdapter TheAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        search=Search.getInstance();
		setContentView(R.layout.home_timeline);
        Commons.isLogon(this);

        init();
        init_Listeners();
	}

    private void init() {

        TweetList = (ListView) findViewById(R.id.tweetList_on_home);

        View header = getLayoutInflater().inflate(R.layout.timeline_header, null);
        View footer = getLayoutInflater().inflate(R.layout.timeline_footer, null);

        TweetList.addHeaderView(header);

        TweetList.addFooterView(footer);

//        TweetAdapter TheAdapter = new TweetAdapter(this,R.layout.tweets_layout,null);
        chk_All = (CheckBox) findViewById(R.id.chk_selectAll);
        chk_MyTweets = (CheckBox) findViewById(R.id.chk_MyTweets);
        chk_Mentions = (CheckBox) findViewById(R.id.chk_Mentions);
        chk_RTs = (CheckBox) findViewById(R.id.chk_RTs);

        refreshTweetList(this,TweetList);

    }

    private void init_Listeners() {
        chk_All.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TheAdapter = (TweetAdapter) TweetList.getAdapter();
                for (int i = 0; i < TheAdapter.getCount(); i++) {
                    ((CheckBox) TheAdapter.getView(i, null, null).findViewById(R.id.chkTweet)).setChecked(buttonView.isChecked());
                    TheAdapter.getView(i, null, null).findViewById(R.id.chkTweet).callOnClick();
                }
                TheAdapter.notifyDataSetChanged();
            }
        });

        chk_RTs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                search.setViewRTs(isChecked);
                Commons.refreshL(Main.this, new DB_Model(Main.this),TweetList);
            }
        });

        chk_Mentions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                search.setViewMentions(isChecked);
                Commons.refreshL(Main.this, new DB_Model(Main.this), TweetList);
            }
        });

        chk_MyTweets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                search.setViewMyTweets(isChecked);
                Commons.refreshL(Main.this, new DB_Model(Main.this), TweetList);
            }
        });

    }

    public void doThings(View view){
        switch (view.getId()){
            case R.id.deleteTweet:
                TheAdapter= (TweetAdapter) TweetList.getAdapter();
                deleteSelected(this, TheAdapter);
                break;
            case R.id.refreshTweet:
                refreshTweetList(this,TweetList);
                break;
            default:
                break;
        }
    }


}
