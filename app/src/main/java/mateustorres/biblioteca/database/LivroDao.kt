package mateustorres.biblioteca.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface LivroDao {

    @Query("SELECT * FROM livros ORDER BY ano DESC")
    fun observeAll(): Flow<List<LivroEntity>>

    @Insert
    suspend fun insertAll(livros: List<LivroEntity>)
}