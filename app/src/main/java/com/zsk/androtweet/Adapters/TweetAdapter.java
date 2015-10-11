package com.zsk.androtweet.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zsk.androtweet.Models.Tweet;
import com.zsk.androtweet.R;

import java.util.List;

import static android.view.LayoutInflater.from;

public class TweetAdapter extends ArrayAdapter<Tweet>{

    private LayoutInflater mInflater;
    private List<Tweet> TwitterStatus;
    public boolean[] isSelectedPos;
    private int selectedCount=0;

    public TweetAdapter(Context cntxt, int ResourceId, List<Tweet> tweetList) {
        super(cntxt, ResourceId, tweetList);
        mInflater = from(cntxt);
        TwitterStatus = tweetList;
        isSelectedPos=new boolean[500];
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View row=convertView;

        TweetViewHolder tweetVH;
        if (row==null) {
            row= mInflater.inflate(R.layout.tweets_layout, null);

            tweetVH = new TweetViewHolder();

            tweetVH.txtTweet = (TextView) row.findViewById(R.id.tweet_on_tweets);
            tweetVH.rtTweet = (TextView) row.findViewById(R.id.rtCount_on_tweets);
            tweetVH.favTweet = (TextView) row.findViewById(R.id.favCount_on_tweets);
            tweetVH.timeTweet = (TextView) row.findViewById(R.id.time_on_tweets);
            tweetVH.chkTweet = (CheckBox) row.findViewById(R.id.chkTweet);

            row.setTag(tweetVH);
        }else {
            tweetVH = (TweetViewHolder) row.getTag();
        }
        Tweet tweet = TwitterStatus.get(position);

        tweetVH.chkTweet.setOnClickListener(new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View view) {
                CheckBox c=(CheckBox)view;
                boolean isChecked=(c.isChecked());
                isSelectedPos[position] = isChecked;
            }

        });

        tweetVH.chkTweet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSelectedPos[position] = isChecked;
            }
        });

//        tweetVH.chkTweet.setText(String.valueOf(tweet.getId()));
        tweetVH.txtTweet.setText(tweet.getTweetText());
        tweetVH.rtTweet.setText(Integer.toString(tweet.getRTcount()));
        tweetVH.favTweet.setText(Integer.toString(tweet.getFAVcount()));
        tweetVH.timeTweet.setText(DateUtils.getRelativeTimeSpanString(tweet.getTime()));
        tweetVH.chkTweet.setChecked(isSelectedPos[position]);

        return row;
    }

    public int getSelectedCount() {
        for (int i = 0; i < isSelectedPos.length; i++) {
            if (isSelectedPos[i])
                selectedCount++;
        }
        return selectedCount;
    }

    public class TweetViewHolder {
        CheckBox chkTweet;
        TextView txtTweet;
        TextView rtTweet;
        TextView favTweet;
        TextView timeTweet;
    }
}