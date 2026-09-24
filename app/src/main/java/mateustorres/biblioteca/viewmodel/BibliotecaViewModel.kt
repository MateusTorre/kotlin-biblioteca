package mateustorres.biblioteca.viewmodel


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mateustorres.biblioteca.database.BibliotecaDatabase
import mateustorres.biblioteca.model.Livro
import mateustorres.biblioteca.repository.LivroRepository

sealed interface BuscaState {

    data object Idle : BuscaState

    data object Loading : BuscaState

    data class Error(
        val message: String
    ) : BuscaState
}

class BibliotecaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = LivroRepository(
        dao = BibliotecaDatabase
            .getInstance(application)
            .livroDao()
    )

    val livros: StateFlow<List<Livro>> =
        repository
            .observeLivros()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    var buscaState: BuscaState by mutableStateOf(
        BuscaState.Idle
    )
        private set

    fun buscar(query: String) {

        if (query.isBlank()) {
            return
        }

        viewModelScope.launch {

            buscaState = BuscaState.Loading

            buscaState = try {

                repository.buscarEArmazenar(query)

                BuscaState.Idle

            } catch (e: Exception) {

                BuscaState.Error(
                    "Não foi possível buscar agora. " +
                            "Mostrando livros salvos."
                )
            }
        }
    }
}