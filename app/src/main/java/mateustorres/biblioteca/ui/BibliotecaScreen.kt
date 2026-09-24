package mateustorres.biblioteca.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mateustorres.biblioteca.viewmodel.BibliotecaViewModel
import mateustorres.biblioteca.viewmodel.BuscaState

@Composable
fun BibliotecaScreen(viewModel: BibliotecaViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val livros by viewModel.livros.collectAsState()
    val buscaState = viewModel.buscaState

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar livro") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = { viewModel.buscar(query) }) { Text("Buscar") }
        }

        AnimatedVisibility(visible = buscaState is BuscaState.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        }

        AnimatedVisibility(visible = buscaState is BuscaState.Error) {
            Text(
                text = (buscaState as? BuscaState.Error)?.message.orEmpty(),
                color = Color(0xFFB32205),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(livros, key = { it.id }) { livro ->
                ListItem(
                    headlineContent = { Text(livro.titulo) },
                    supportingContent = { Text("${livro.autor} · ${livro.ano}") }
                )
            }
        }
    }
}