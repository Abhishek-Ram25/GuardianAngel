package com.example.myapplication
import androidx.lifecycle.LiveData

class UserDataRepository(private val dataDao: UserDataDao) {
    val readAllData: LiveData<List<UserData>> = dataDao.readAllData()


    suspend fun insert(data: UserData) {
        dataDao.insert(data)
    }
}