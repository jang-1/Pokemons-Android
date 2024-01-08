package com.example.pokemon

import com.example.pokemon.details.DetailsActivity
import UiState
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getData()

        setContent {
            PokemonTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(topBar = {
                        MyTopView(viewModel = viewModel)
                    }){scaffoldPaddings ->
                        PokemonList(viewModel = viewModel, onClick = { name -> navigateToDetails(name) },modifier = Modifier
                            .padding(scaffoldPaddings))
                    }
                }
            }
        }
    }
    private fun navigateToDetails(name: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("pokemon_name", name)
        startActivity(intent)
    }
}

@Composable
fun PokemonList(viewModel: MainViewModel, onClick: (String) -> Unit, modifier: Modifier) {
    val uiState by viewModel.immutablePokemonData.observeAsState(UiState())
    val query by viewModel.filterQuery.observeAsState("")
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when {
            uiState?.isLoading == true -> {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
            uiState?.error != null -> {
                item {
                    Toast.makeText(LocalContext.current, "${uiState.error}", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                uiState?.data?.let { restPokemons ->
                    restPokemons.filter { it.name.contains(query, true) }.let { pokemons ->
                        items(pokemons) { pokemon ->
                            PokemonCard(
                                name = pokemon.name,
                                frontImage = pokemon.details.frontImage,
                                onClick = { onClick(pokemon.name) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonCard(name: String, frontImage: String, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
        ) {
            AsyncImage(
                model = frontImage,
                contentDescription = "To jest obrazek $name",
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.pikachu)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, fontWeight = FontWeight.Bold)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopView(viewModel: MainViewModel) {
    var searchText by remember { mutableStateOf("") }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = searchText,
        onQueryChange = { wpisywanyTekst -> searchText = wpisywanyTekst },
        onSearch = { viewModel.updateFilterQuery(it) },
        placeholder = { Text(text = "Wyszukaj...") },
        active = false,
        onActiveChange = { },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            Image(
                modifier = Modifier.clickable {
                    searchText = ""
                    viewModel.updateFilterQuery("")
                },
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear"
            )
        }
    ) {

    }
}


