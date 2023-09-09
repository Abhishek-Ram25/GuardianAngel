package com.example.myapplication
import androidx.lifecycle.LiveData

class MedicalDataRepository(private val dataDao: MedicalDataDao) {
    val readAllData: LiveData<List<MedicalData>> = dataDao.readAllData()


    suspend fun insert(data: MedicalData) {
        dataDao.insert(data)
    }
}