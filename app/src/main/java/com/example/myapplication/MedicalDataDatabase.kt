package com.example.myapplication
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MedicalData::class], version = 1, exportSchema = false)
abstract class MedicalDataDatabase: RoomDatabase() {
    abstract fun dataDao(): MedicalDataDao

    companion object{
        @Volatile
        private var INSTANCE: MedicalDataDatabase? = null

        fun getDatabase(context: Context): MedicalDataDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicalDataDatabase::class.java,
                    "health_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


    }
}