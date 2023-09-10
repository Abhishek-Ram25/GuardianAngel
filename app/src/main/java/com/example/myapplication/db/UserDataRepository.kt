package com.example.myapplication.db
import androidx.lifecycle.LiveData
import com.example.myapplication.db.UserData
import com.example.myapplication.db.UserDataDao

class UserDataRepository(private val dataDao: UserDataDao) {
    val readAllData: LiveData<List<UserData>> = dataDao.readAllData()


    suspend fun insert(data: UserData) {
        dataDao.insert(data)
    }
}