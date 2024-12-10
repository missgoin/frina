package frena.id.manager.ui.map

import frena.id.manager.MainActivity
import frena.id.service.HEREBackgroundPositioningService

import android.os.Bundle
import android.util.Log
import android.content.Context
import android.content.Intent
import android.os.Build
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.graphics.Color
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import frena.id.data.model.FavoriteLocation
import frena.id.manager.ui.drawer.DrawerContent
import frena.id.manager.ui.map.components.AddToFavoritesDialog
import frena.id.manager.ui.map.components.GoToPointDialog
import frena.id.manager.ui.map.components.MapViewContainer
import frena.id.manager.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    mapViewModel: MapViewModel
) {
    val context = LocalContext.current

    val isPlaying by mapViewModel.isPlaying
    val isFabClickable by remember { derivedStateOf { mapViewModel.isFabClickable } }
    val showGoToPointDialog by mapViewModel.showGoToPointDialog
    val showAddToFavoritesDialog by mapViewModel.showAddToFavoritesDialog
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showOptionsMenu by remember { mutableStateOf(false) }

    // BackHandler to close the drawer when open
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }        

    // Scaffold with drawer
    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                },
                navController = navController
            )
        },
        scrimColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.32f), // Custom scrim color
        drawerState = drawerState,
        gesturesEnabled = false,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("FRina") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                mapViewModel.triggerCenterMapEvent()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Center")
                        }
                        IconButton(
                            onClick = {
                                showOptionsMenu = true
                            }
                        ) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(
                            expanded = showOptionsMenu,
                            onDismissRequest = { showOptionsMenu = false }
                        ) {
                            DropdownMenuItem(
                                leadingIcon = { Icon(imageVector = Icons.Default.LocationSearching, contentDescription = "Go to Point") },
                                text = { Text("Go to Point") },
                                onClick = {
                                    showOptionsMenu = false
                                    mapViewModel.showGoToPointDialog()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Add to Favorites") },
                                text = { Text("Add to Favorites") },
                                onClick = {
                                    showOptionsMenu = false
                                    mapViewModel.showAddToFavoritesDialog()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(imageVector = Icons.Default.Star, contentDescription = "Favorites") },
                                text = { Text("Favorites") },
                                onClick = {
                                    showOptionsMenu = false
                                    navController.navigate(Screen.Favorites.route)
                                }
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (isFabClickable) {
                            mapViewModel.togglePlaying()
                            if (mapViewModel.isPlaying.value) {
                                Toast.makeText(context, "Location Start", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(context, "Location Stop", Toast.LENGTH_SHORT).show()
                                    
                            }
                        }
                        
                        

                              
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(16.dp),
                    containerColor = if (isFabClickable) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    },
                    contentColor = if (isFabClickable) {
                        contentColorFor(MaterialTheme.colorScheme.primary)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = if (isFabClickable) 6.dp else 0.dp,
                        pressedElevation = if (isFabClickable) 12.dp else 0.dp
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Stop" else "Play"
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                MapViewContainer(mapViewModel)
            }
        }

        if (showGoToPointDialog) {
            GoToPointDialog(
                onDismissRequest = { mapViewModel.hideGoToPointDialog() },
                onGoToPoint = { latitude, longitude ->
                    mapViewModel.goToPoint(latitude, longitude)
                    mapViewModel.hideGoToPointDialog()
                },
                mapViewModel = mapViewModel
            )
        }

        if (showAddToFavoritesDialog) {
            // Prefill coordinates from the last clicked location (marker)
            val lastClickedLocation = mapViewModel.lastClickedLocation.value

            LaunchedEffect(Unit) {
                mapViewModel.prefillCoordinatesFromMarker(
                    lastClickedLocation?.latitude,
                    lastClickedLocation?.longitude
                )
            }

            AddToFavoritesDialog(
                mapViewModel = mapViewModel,
                onDismissRequest = { mapViewModel.hideAddToFavoritesDialog() },
                onAddFavorite = { name, latitude, longitude ->
                    val favorite = FavoriteLocation(name, latitude, longitude)
                    mapViewModel.addFavoriteLocation(favorite)
                    mapViewModel.hideAddToFavoritesDialog()
                }
            )
        }
    }
}

