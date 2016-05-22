package com.zsk.androtweet.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsk.androtweet.AndroTweetApp;
import com.zsk.androtweet.Main;
import com.zsk.androtweet.Models.Tweet;
import com.zsk.androtweet.R;

import java.util.List;

public class TweetAdapter
        extends ArrayAdapter<Tweet> {
    private static final String TAG = "ANDROTWEET";
    private List<Tweet> TwitterStatus;
    public boolean[] isSelectedPos;
    private LayoutInflater mInflater;
    private LayoutInflater mInflater2;
    private int selectedCount = 0;

    public TweetAdapter(Context paramContext, int paramInt, List<Tweet> paramList) {
        super(paramContext, paramInt, paramList);
        this.mInflater = LayoutInflater.from(paramContext);
        this.TwitterStatus = paramList;
        this.isSelectedPos = new boolean[500];
    }

    public int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < isSelectedPos.length; i++) {
            if (isSelectedPos[i])
                count += 1;
        }
        selectedCount = count;
        return this.selectedCount;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View row = convertView;

        TweetViewHolder tweetVH;
        if (row == null) {
            row = mInflater.inflate(R.layout.tweets_layout, null);

            tweetVH = new TweetViewHolder();

            tweetVH.txtTweet = (TextView) row.findViewById(R.id.tweet_on_tweets);
            tweetVH.rtTweet = (TextView) row.findViewById(R.id.rtCount_on_tweets);
            tweetVH.favTweet = (TextView) row.findViewById(R.id.favCount_on_tweets);
            tweetVH.timeTweet = (TextView) row.findViewById(R.id.time_on_tweets);
            tweetVH.chkTweet = (CheckBox) row.findViewById(R.id.chkTweet);

            row.setTag(tweetVH);
        } else {
            tweetVH = (TweetViewHolder) row.getTag();
        }
        Tweet tweet = TwitterStatus.get(position);

        tweetVH.chkTweet.setOnClickListener(new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View view) {
                CheckBox c = (CheckBox) view;
                boolean isChecked = (c.isChecked());
                isSelectedPos[position] = isChecked;
            }

        });

        tweetVH.chkTweet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSelectedPos[position] = isChecked;
                Main.selectedCountChange(getSelectedCount());
            }
        });

//        tweetVH.chkTweet.setText(String.valueOf(tweet.getId()));
        tweetVH.txtTweet.setText(tweet.getTweetText());
        tweetVH.rtTweet.setText(Integer.toString(tweet.getRTcount()));
        tweetVH.favTweet.setText(Integer.toString(tweet.getFAVcount()));
        tweetVH.timeTweet.setText(DateUtils.getRelativeTimeSpanString(tweet.getTime()));
        tweetVH.chkTweet.setChecked(isSelectedPos[position]);

        if (AndroTweetApp.getTweetId()!=null && String.valueOf(tweet.getId()).equals(AndroTweetApp.getTweetId()) && AndroTweetApp.getDaysAgo()<4) {
            tweetVH.chkTweet.setVisibility(View.INVISIBLE);
            isSelectedPos[position]=false;
        }

        return row;
    }

    public class TweetViewHolder {
        CheckBox chkTweet;
        ImageView favImage;
        TextView favTweet;
        ImageView rtImage;
        TextView rtTweet;
        TextView timeTweet;
        TextView txtTweet;

        public TweetViewHolder() {
        }
    }
}
