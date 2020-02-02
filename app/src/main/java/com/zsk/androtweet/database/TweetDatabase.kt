package com.zsk.androtweet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zsk.androtweet.models.TimelineDao
import com.zsk.androtweet.models.Tweet

@Database(entities = [(Tweet::class)], version = 11)
abstract class TweetDatabase : RoomDatabase() {

    abstract fun timelineDao(): TimelineDao
}