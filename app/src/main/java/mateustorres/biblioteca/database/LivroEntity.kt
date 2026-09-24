package mateustorres.biblioteca.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "livros")
data class LivroEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val titulo: String,

    val autor: String,

    val ano: Int
)