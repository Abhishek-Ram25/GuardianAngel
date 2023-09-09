package com.example.myapplication
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedicalDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(row: MedicalData)

    @Query(value = "SELECT * FROM healthApp_data ORDER BY ID DESC")
    fun readAllData(): LiveData<List<MedicalData>>

}