package mateustorres.biblioteca.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mateustorres.biblioteca.database.LivroDao
import mateustorres.biblioteca.database.LivroEntity
import mateustorres.biblioteca.model.Livro
import mateustorres.biblioteca.network.BookDoc
import mateustorres.biblioteca.network.OpenLibraryService
import mateustorres.biblioteca.network.RetrofitClient

class LivroRepository(
    private val dao: LivroDao,
    private val api: OpenLibraryService = RetrofitClient.api
) {

    fun observeLivros(): Flow<List<Livro>> {

        return dao.observeAll().map { entidades ->

            entidades.map { entidade ->

                Livro(
                    id = entidade.id,
                    titulo = entidade.titulo,
                    autor = entidade.autor,
                    ano = entidade.ano
                )
            }
        }
    }

    suspend fun buscarEArmazenar(query: String) {

        val resposta = api.buscarLivros(query)

        val livros = resposta.docs.map { livro ->

            LivroEntity(
                titulo = livro.title,
                autor = livro.authorName?.firstOrNull()
                    ?: "Autor desconhecido",
                ano = livro.firstPublishYear ?: 0
            )
        }

        dao.insertAll(livros)
    }
}