package com.zsk.androtweet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zsk.androtweet.database.dao.TimelineDao
import com.zsk.androtweet.database.dao.UserDao
import com.zsk.androtweet.models.Tweet
import com.zsk.androtweet.models.User

@Database(entities = [User::class, Tweet::class], version = 12)
abstract class AndroTweetDatabase : RoomDatabase() {

    abstract fun timelineDao(): TimelineDao

    abstract fun userDao(): UserDao
}