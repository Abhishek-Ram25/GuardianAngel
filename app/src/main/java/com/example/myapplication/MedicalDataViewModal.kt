package com.example.myapplication
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicalDataViewModal(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<MedicalData>>

    private val repository: MedicalDataRepository

    init {
        val keyValueStoreDao = MedicalDataDatabase.getDatabase(application).dataDao()
        repository = MedicalDataRepository(keyValueStoreDao)
        readAllData = repository.readAllData
    }

    fun insert(data: MedicalData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(data)
        }
    }

}