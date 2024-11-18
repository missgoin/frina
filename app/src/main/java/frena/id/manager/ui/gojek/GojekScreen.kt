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
        // Randomize Nearby Location
        DoubleGojekData(
            title = "Randomize Nearby Location",
            useValueState = gojekViewModel.useRandomize.collectAsState(),
            valueState = gojekViewModel.randomizeRadius.collectAsState(),
            setUseValue = gojekViewModel::setUseRandomize,
            setValue = gojekViewModel::setRandomizeRadius,
            label = "Randomization Radius (m)",
            minValue = 0f,
            maxValue = 5f,
            step = 0.01f
        ),
        // Custom Horizontal Accuracy
        DoubleGojekData(
            title = "Custom Horizontal Accuracy",
            useValueState = gojekViewModel.useAccuracy.collectAsState(),
            valueState = gojekViewModel.accuracy.collectAsState(),
            setUseValue = gojekViewModel::setUseAccuracy,
            setValue = gojekViewModel::setAccuracy,
            label = "Horizontal Accuracy (m)",
            minValue = 0f,
            maxValue = 100f,
            step = 1f
        ),
        // Custom Vertical Accuracy
        FloatGojekData(
            title = "Custom Vertical Accuracy",
            useValueState = gojekViewModel.useVerticalAccuracy.collectAsState(),
            valueState = gojekViewModel.verticalAccuracy.collectAsState(),
            setUseValue = gojekViewModel::setUseVerticalAccuracy,
            setValue = gojekViewModel::setVerticalAccuracy,
            label = "Vertical Accuracy (m)",
            minValue = 0f,
            maxValue = 100f,
            step = 1f
        ),
        // Custom Altitude
        DoubleGojekData(
            title = "Custom Altitude",
            useValueState = gojekViewModel.useAltitude.collectAsState(),
            valueState = gojekViewModel.altitude.collectAsState(),
            setUseValue = gojekViewModel::setUseAltitude,
            setValue = gojekViewModel::setAltitude,
            label = "Altitude (m)",
            minValue = 0f,
            maxValue = 2000f,
            step = 0.5f
        ),
        // Custom MSL
        DoubleGojekData(
            title = "Custom MSL",
            useValueState = gojekViewModel.useMeanSeaLevel.collectAsState(),
            valueState = gojekViewModel.meanSeaLevel.collectAsState(),
            setUseValue = gojekViewModel::setUseMeanSeaLevel,
            setValue = gojekViewModel::setMeanSeaLevel,
            label = "MSL (m)",
            minValue = -400f,
            maxValue = 2000f,
            step = 0.5f
        ),
        // Custom MSL Accuracy
        FloatGojekData(
            title = "Custom MSL Accuracy",
            useValueState = gojekViewModel.useMeanSeaLevelAccuracy.collectAsState(),
            valueState = gojekViewModel.meanSeaLevelAccuracy.collectAsState(),
            setUseValue = gojekViewModel::setUseMeanSeaLevelAccuracy,
            setValue = gojekViewModel::setMeanSeaLevelAccuracy,
            label = "MSL Accuracy (m)",
            minValue = 0f,
            maxValue = 100f,
            step = 1f
        ),
        // Custom Speed
        FloatGojekData(
            title = "Custom Speed",
            useValueState = gojekViewModel.useSpeed.collectAsState(),
            valueState = gojekViewModel.speed.collectAsState(),
            setUseValue = gojekViewModel::setUseSpeed,
            setValue = gojekViewModel::setSpeed,
            label = "Speed (m/s)",
            minValue = 0f,
            maxValue = 30f,
            step = 0.1f
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
    onUseValueChange: (Boolean) -> Unit,
    value: Double,
    onValueChange: (Double) -> Unit,
    label: String,
    minValue: Float,
    maxValue: Float,
    step: Float
) {
    GojekItem(
        title = title,
        useValue = useValue,
        onUseValueChange = onUseValueChange,
        value = value,
        onValueChange = onValueChange,
        label = label,
        minValue = minValue,
        maxValue = maxValue,
        step = step,
        valueFormatter = { "%.2f".format(it) },
        parseValue = { it.toDouble() }
    )
}

@Composable
fun FloatGojekItem(
    title: String,
    useValue: Boolean,
    onUseValueChange: (Boolean) -> Unit,
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    minValue: Float,
    maxValue: Float,
    step: Float
) {
    GojekItem(
        title = title,
        useValue = useValue,
        onUseValueChange = onUseValueChange,
        value = value,
        onValueChange = onValueChange,
        label = label,
        minValue = minValue,
        maxValue = maxValue,
        step = step,
        valueFormatter = { "%.2f".format(it) },
        parseValue = { it }
    )
}

@Composable
private fun <T : Number> GojekItem(
    title: String,
    useValue: Boolean,
    onUseValueChange: (Boolean) -> Unit,
    value: T,
    onValueChange: (T) -> Unit,
    label: String,
    minValue: Float,
    maxValue: Float,
    step: Float,
    valueFormatter: (T) -> String,
    parseValue: (Float) -> T
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

        if (useValue) {
            Spacer(modifier = Modifier.height(8.dp))

            var sliderValue by remember { mutableFloatStateOf(value.toFloat()) }

            LaunchedEffect(value) {
                if (sliderValue != value.toFloat()) {
                    sliderValue = value.toFloat()
                }
            }

            Text(
                text = "$label: ${valueFormatter(parseValue(sliderValue))}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                },
                onValueChangeFinished = {
                    onValueChange(parseValue(sliderValue))
                },
                valueRange = minValue..maxValue,
                steps = ((maxValue - minValue) / step).toInt() - 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

sealed class GojekData {
    abstract val title: String
    abstract val useValueState: State<Boolean>
    abstract val setUseValue: (Boolean) -> Unit
    abstract val label: String
    abstract val minValue: Float
    abstract val maxValue: Float
    abstract val step: Float
}

data class DoubleGojekData(
    override val title: String,
    override val useValueState: State<Boolean>,
    val valueState: State<Double>,
    override val setUseValue: (Boolean) -> Unit,
    val setValue: (Double) -> Unit,
    override val label: String,
    override val minValue: Float,
    override val maxValue: Float,
    override val step: Float
) : GojekData()

data class FloatGojekData(
    override val title: String,
    override val useValueState: State<Boolean>,
    val valueState: State<Float>,
    override val setUseValue: (Boolean) -> Unit,
    val setValue: (Float) -> Unit,
    override val label: String,
    override val minValue: Float,
    override val maxValue: Float,
    override val step: Float
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
        onValueChange = gojek.setValue,
        label = gojek.label,
        minValue = gojek.minValue,
        maxValue = gojek.maxValue,
        step = gojek.step
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
        onValueChange = gojek.setValue,
        label = gojek.label,
        minValue = gojek.minValue,
        maxValue = gojek.maxValue,
        step = gojek.step
    )
}