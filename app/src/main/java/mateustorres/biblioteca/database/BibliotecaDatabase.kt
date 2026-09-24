package mateustorres.biblioteca.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LivroEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BibliotecaDatabase : RoomDatabase() {

    abstract fun livroDao(): LivroDao

    companion object {

        @Volatile
        private var INSTANCE: BibliotecaDatabase? = null

        fun getInstance(context: Context): BibliotecaDatabase {

            return INSTANCE ?: synchronized(this) {

                Room.databaseBuilder(
                    context.applicationContext,
                    BibliotecaDatabase::class.java,
                    "biblioteca.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}