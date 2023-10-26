package com.example.myapplication.db
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.UserData
import com.example.myapplication.db.UserDataDB
import com.example.myapplication.db.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDataViewModal(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<UserData>>

    private val repository: UserDataRepository

    init {
        val keyValueStoreDao = UserDataDB.getDatabase(application).dataDao()
        repository = UserDataRepository(keyValueStoreDao)
        readAllData = repository.readAllData
    }

    fun insert(data: UserData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(data)
        }
    }

}