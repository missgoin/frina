//GojekScreen.kt
package frena.id.manager.ui.gojek

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GojekScreen(
    navController: NavController,
    gojekViewModel: GojekViewModel = viewModel ()
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val gojek = listOf<GojekData>(
        // Gojek bypass reguler
        DoubleGojekData(
            title = "Gojek Bypass Reguler",
            useValueState = gojekViewModel.useGojekBypassReg.collectAsState(),
            valueState = gojekViewModel.goBypassReg.collectAsState(),
            setUseValue = gojekViewModel::setUseGojekBypassReg,
            setValue = gojekViewModel::setGoBypassReg
            
        )
        // Gojek bypass reguler
//        FloatGojekData(
//            title = "Gojek Bypass Reguler",
//            useValueState = gojekViewModel.useSpeed.collectAsState(),
//            valueState = gojekViewModel.speed.collectAsState(),
//            setUseValue = gojekViewModel::setUseSpeed
//        ),
        // Custom Speed Accuracy
//        FloatGojekData(
//            title = "Custom Speed Accuracy",
//            useValueState = gojekViewModel.useSpeedAccuracy.collectAsState(),
//            valueState = gojekViewModel.speedAccuracy.collectAsState(),
//            setUseValue = gojekViewModel::setUseSpeedAccuracy,
//            setValue = gojekViewModel::setSpeedAccuracy,
//            label = "Speed Accuracy (m/s)",
//             minValue = 0f,
//              maxValue = 100f,
//            step = 1f
//        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GOJEK") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                gojek.forEach { gojek ->
                    when (gojek) {
                        is DoubleGojekData -> {
                            DoubleGojekComposable(gojek)
                        }
                        is FloatGojekData -> {
                            FloatGojekComposable(gojek)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DoubleGojekItem(
    title: String,
    useValue: Boolean,
    onUseValueChange: (Boolean),
    value: Boolean,
    onValueChange: (Boolean)    
) {
    GojekItem(
        title = title,
        useValue = useValue,
        onUseValueChange = onUseValueChange,
        value = value,
        onValueChange = onValueChange        
    )
}

@Composable
fun FloatGojekItem(
    title: String,
    useValue: Boolean,
    onUseValueChange: (Boolean),
    value: Boolean,
    onValueChange: (Boolean)
) {
    GojekItem(
        title = title,
        useValue = useValue,
        onUseValueChange = onUseValueChange,
        value = value,
        onValueChange = onValueChange        
    )
}

@Composable
private fun GojekItem(
    title: String,
    useValue: Boolean,
    onUseValueChange: (Boolean),
    value: (Boolean),
    onValueChange: (Boolean)
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = useValue,
                onCheckedChange = onUseValueChange
            )
        }

    }
}


sealed class GojekData {
    abstract val title: String
    abstract val useValueState: State<Boolean>
    abstract val setUseValue: (Boolean)
}

data class DoubleGojekData(
    override val title: String,
    override val useValueState: State<Boolean>,
    val valueState: State<Boolean>,
    override val setUseValue: (Boolean),
    val setValue: (Boolean)
) : GojekData()

data class FloatGojekData(
    override val title: String,
    override val useValueState: State<Boolean>,
    val valueState: State<Boolean>,
    override val setUseValue: (Boolean),
    val setValue: (Boolean)
) : GojekData()

@Composable
fun DoubleGojekComposable(
    gojek: DoubleGojekData
) {
    DoubleGojekItem(
        title = gojek.title,
        useValue = gojek.useValueState.value,
        onUseValueChange = gojek.setUseValue,
        value = gojek.valueState.value,
        onValueChange = gojek.setValue        
    )
}

@Composable
fun FloatGojekComposable(
    gojek: FloatGojekData
) {
    FloatGojekItem(
        title = gojek.title,
        useValue = gojek.useValueState.value,
        onUseValueChange = gojek.setUseValue,
        value = gojek.valueState.value,
        onValueChange = gojek.setValue
    )
}