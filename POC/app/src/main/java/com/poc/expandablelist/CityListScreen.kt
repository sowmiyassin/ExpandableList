package com.poc.expandablelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poc.expandablelist.data.City
import com.poc.expandablelist.data.StateGroup
import com.poc.expandablelist.viewmodel.CityViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(viewModel: CityViewModel) {
    val cityGroups = viewModel.cityGroups.collectAsState().value
    var isDarkTheme by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    AppTheme(isDarkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("City Explorer") }, actions = {
                    IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Filled.Brightness7 else Icons.Filled.Brightness4,
                            contentDescription = "Toggle Theme"
                        )
                    }
                })
            }) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(cityGroups) { stateGroup ->
                        StateGroupItem(stateGroup, onToggle = {
                            coroutineScope.launch { viewModel.toggleStateExpanded(it) }
                        })
                    }
                }

                Button(
                    onClick = { coroutineScope.launch { viewModel.reverseOrder() } },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Reverse Order")
                }
            }
        }
    }
}
@Composable
fun StateGroupItem(stateGroup: StateGroup, onToggle: (String) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stateGroup.state,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .clickable { onToggle(stateGroup.state) }
                    .padding(8.dp))
            if (stateGroup.isExpanded) {
                stateGroup.cities.forEach { city ->
                    CityItem(city)
                }
            }
        }
    }
}

@Composable
fun CityItem(city: City) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "City: ${city.city}", color = MaterialTheme.colorScheme.onSurface)
            Text(text = "State: ${city.state}", color = MaterialTheme.colorScheme.onSurface)
            Text(text = "Country: ${city.country}", color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = "Population: ${city.population}", color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AppTheme(isDarkTheme: Boolean, content: @Composable () -> Unit) {
    val colors = if (isDarkTheme) {
        darkColorScheme(
            primary = Color(0xFFBB86FC), secondary = Color(0xFF03DAC5)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC5),
            background = Color.White,
            onBackground = Color.Black,
            surface = Color.White,
            onSurface = Color.Black
        )
    }

    MaterialTheme(
        colorScheme = colors, typography = Typography(), content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCityItem() {
    CityItem(City("Frankston", "Victoria", "Australia", 36097))
}
