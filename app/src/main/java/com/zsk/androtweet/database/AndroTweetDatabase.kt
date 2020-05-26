package com.zsk.androtweet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zsk.androtweet.database.dao.TweetListDao
import com.zsk.androtweet.database.dao.UserDao
import com.zsk.androtweet.models.Tweet
import com.zsk.androtweet.models.User
import com.zsk.androtweet.utils.Converters

@TypeConverters(Converters::class)
@Database(entities = [User::class, Tweet::class], version = 24)
abstract class AndroTweetDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tweetListDao(): TweetListDao
}