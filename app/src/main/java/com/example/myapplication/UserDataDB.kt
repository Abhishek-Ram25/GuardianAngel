package com.example.myapplication
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserData::class], version = 1, exportSchema = false)
abstract class UserDataDB: RoomDatabase() {
    abstract fun dataDao(): UserDataDao

    companion object{
        @Volatile
        private var INSTANCE: UserDataDB? = null

        fun getDatabase(context: Context): UserDataDB{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDataDB::class.java,
                    "health_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


    }
}