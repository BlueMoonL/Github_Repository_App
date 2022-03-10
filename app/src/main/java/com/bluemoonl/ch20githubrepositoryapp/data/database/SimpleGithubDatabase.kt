package com.bluemoonl.ch20githubrepositoryapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bluemoonl.ch20githubrepositoryapp.data.dao.SearchHistoryDao
import com.bluemoonl.ch20githubrepositoryapp.data.entity.GithubRepoEntity

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao

}