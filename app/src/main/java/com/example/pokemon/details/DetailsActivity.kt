package com.example.pokemon.details

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.pokemon.MainViewModel
import com.example.pokemon.R
import com.example.pokemon.ui.theme.PokemonTheme

class DetailsActivity : ComponentActivity() {
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pokemonName = intent.getStringExtra("pokemon_name") ?: ""
        setContent {
            PokemonTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailsScreen(viewModel, pokemonName, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(viewModel: DetailsViewModel, pokemonName: String, modifier: Modifier = Modifier) {
    LaunchedEffect(pokemonName) {
        viewModel.loadPokemonDetail(pokemonName)
    }
    val pokemonDetail by viewModel.pokemonDetail.observeAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
    )
    {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(20.dp)
                .width(400.dp)
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))) {


            AsyncImage(
                model = pokemonDetail?.data?.frontImage,
                contentDescription = "To jest obrazek ${pokemonName}",
                modifier.size(200.dp),
                placeholder = painterResource(R.drawable.pikachu),
            )



            Text(text = "Name: ${pokemonName}", fontWeight = FontWeight.Bold)
            Row(

                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = "Height", fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${pokemonDetail?.data?.height} m"
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(
                        text = "Weight", fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${pokemonDetail?.data?.weight} kg"
                    )
                }
            }
            Text(text = "Stats:", fontWeight = FontWeight.Bold, color = Color.Red)
            Row(
                modifier = modifier.width(200.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text(text = "Hp", fontWeight = FontWeight.Bold)
                Text(text = "${pokemonDetail?.data?.hp}")
            }
            Row(
                modifier = modifier.width(200.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Attack:", fontWeight = FontWeight.Bold)
                Text(text = "${pokemonDetail?.data?.attack}")
            }
            Row(
                modifier = modifier.width(200.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Defensive:", fontWeight = FontWeight.Bold)
                Text(text = "${pokemonDetail?.data?.defense}")
            }
            Row(
                modifier = modifier
                    .width(200.dp)
                    .padding(bottom = 5.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Speed:", fontWeight = FontWeight.Bold)
                Text(text = "${pokemonDetail?.data?.speed}")
            }
        }
    }
}
