package mateustorres.biblioteca.repository

import mateustorres.biblioteca.network.RetrofitClient
import mateustorres.biblioteca.database.LivroDao
import mateustorres.biblioteca.database.LivroEntity
import mateustorres.biblioteca.model.Livro
import mateustorres.biblioteca.network.BookDoc
import mateustorres.biblioteca.network.OpenLibraryService
import mateustorres.coroutines.flow.map
import java.util.concurrent.Flow

class LivroRepository(
    private val dao: LivroDao,
    private val api: OpenLibraryService = RetrofitClient.api
) {

    fun observeLivros(): Flow<List<Livro>> =
        dao.observeAll().map { entidades -> entidades.map { it.toDomain() } }


    suspend fun buscarEArmazenar(query: String) {
        val resposta = api.buscarLivros(query)
        dao.insertAll(resposta.docs.map { it.toEntity() })
    }
}

private fun LivroEntity.toDomain() = Livro(id = id, titulo = titulo, autor = autor, ano = ano)

private fun BookDoc.toEntity() = LivroEntity(
    titulo = title,
    autor = authorName?.firstOrNull() ?: "Autor desconhecido",
    ano = firstPublishYear ?: 0
)

