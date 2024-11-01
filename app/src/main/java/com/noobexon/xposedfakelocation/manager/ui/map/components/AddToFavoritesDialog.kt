package com.noobexon.xposedfakelocation.manager.ui.map.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.noobexon.xposedfakelocation.manager.ui.map.MapViewModel

@Composable
fun AddToFavoritesDialog(
    mapViewModel: MapViewModel,
    onDismissRequest: () -> Unit,
    onAddFavorite: (name: String, latitude: Double, longitude: Double) -> Unit
) {
    // Access the new FavoritesInputState
    val addToFavoritesState by mapViewModel.addToFavoritesState
    val favoriteNameInput = addToFavoritesState.name.value
    val favoriteLatitudeInput = addToFavoritesState.latitude.value
    val favoriteLongitudeInput = addToFavoritesState.longitude.value
    val favoriteNameError = addToFavoritesState.name.errorMessage
    val favoriteLatitudeError = addToFavoritesState.latitude.errorMessage
    val favoriteLongitudeError = addToFavoritesState.longitude.errorMessage

    AlertDialog(
        onDismissRequest = {
            mapViewModel.clearAddToFavoritesInputs()
            onDismissRequest()
        },
        title = { Text("Add to Favorites") },
        text = {
            Column {
                OutlinedTextField(
                    value = favoriteNameInput,
                    onValueChange = { mapViewModel.updateAddToFavoritesField("name", it) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = favoriteNameError != null
                )
                if (favoriteNameError != null) {
                    Text(
                        text = favoriteNameError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = favoriteLatitudeInput,
                    onValueChange = { mapViewModel.updateAddToFavoritesField("latitude", it) },
                    label = { Text("Latitude") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = favoriteLatitudeError != null
                )
                if (favoriteLatitudeError != null) {
                    Text(
                        text = favoriteLatitudeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = favoriteLongitudeInput,
                    onValueChange = { mapViewModel.updateAddToFavoritesField("longitude", it) },
                    label = { Text("Longitude") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = favoriteLongitudeError != null
                )
                if (favoriteLongitudeError != null) {
                    Text(
                        text = favoriteLongitudeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    mapViewModel.validateAndAddFavorite { name, latitude, longitude ->
                        onAddFavorite(name, latitude, longitude)
                        mapViewModel.clearAddToFavoritesInputs()
                        onDismissRequest()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    mapViewModel.clearAddToFavoritesInputs()
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}
