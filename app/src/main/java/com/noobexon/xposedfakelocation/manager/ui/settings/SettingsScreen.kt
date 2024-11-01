//SettingsScreen.kt
package com.noobexon.xposedfakelocation.manager.ui.settings

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
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel ()
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val settings = listOf<SettingData>(
        // Randomize Nearby Location
        DoubleSettingData(
            title = "Randomize Nearby Location",
            useValueState = settingsViewModel.useRandomize.collectAsState(),
            valueState = settingsViewModel.randomizeRadius.collectAsState(),
            setUseValue = settingsViewModel::setUseRandomize,
            setValue = settingsViewModel::setRandomizeRadius,
            label = "Randomization Radius (m)",
            minValue = 0f,
            maxValue = 50f,
            step = 0.1f
        ),
        // Custom Horizontal Accuracy
        DoubleSettingData(
            title = "Custom Horizontal Accuracy",
            useValueState = settingsViewModel.useAccuracy.collectAsState(),
            valueState = settingsViewModel.accuracy.collectAsState(),
            setUseValue = settingsViewModel::setUseAccuracy,
            setValue = settingsViewModel::setAccuracy,
            label = "Horizontal Accuracy (m)",
            minValue = 0f,
            maxValue = 100f,
            step = 1f
        ),
        // Custom Vertical Accuracy
        FloatSettingData(
            title = "Custom Vertical Accuracy",
            useValueState = settingsViewModel.useVerticalAccuracy.collectAsState(),
            valueState = settingsViewModel.verticalAccuracy.collectAsState(),
            setUseValue = settingsViewModel::setUseVerticalAccuracy,
            setValue = settingsViewModel::setVerticalAccuracy,
            label = "Vertical Accuracy (m)",
            minValue = 0f,
            maxValue = 100f,
            step = 1f
        ),
        // Custom Altitude
        DoubleSettingData(
            title = "Custom Altitude",
            useValueState = settingsViewModel.useAltitude.collectAsState(),
            valueState = settingsViewModel.altitude.collectAsState(),
            setUseValue = settingsViewModel::setUseAltitude,
            setValue = settingsViewModel::setAltitude,
            label = "Altitude (m)",
            minValue = 0f,
            maxValue = 2000f,
            step = 0.5f
        ),
        // Custom MSL
        DoubleSettingData(
            title = "Custom MSL",
            useValueState = settingsViewModel.useMeanSeaLevel.collectAsState(),
            valueState = settingsViewModel.meanSeaLevel.collectAsState(),
            setUseValue = settingsViewModel::setUseMeanSeaLevel,
            setValue = settingsViewModel::setMeanSeaLevel,
            label = "MSL (m)",
            minValue = -400f,
            maxValue = 2000f,
            step = 0.5f
        ),
        // Custom MSL Accuracy
        FloatSettingData(
            title = "Custom MSL Accuracy",
            useValueState = settingsViewModel.useMeanSeaLevelAccuracy.collectAsState(),
            valueState = settingsViewModel.meanSeaLevelAccuracy.collectAsState(),
            setUseValue = settingsViewModel::setUseMeanSeaLevelAccuracy,
            setValue = settingsViewModel::setMeanSeaLevelAccuracy,
            label = "MSL Accuracy (m)",
            minValue = 0f,
            maxValue = 100f,
            step = 1f
        ),
        // Custom Speed
        FloatSettingData(
            title = "Custom Speed",
            useValueState = settingsViewModel.useSpeed.collectAsState(),
            valueState = settingsViewModel.speed.collectAsState(),
            setUseValue = settingsViewModel::setUseSpeed,
            setValue = settingsViewModel::setSpeed,
            label = "Speed (m/s)",
            minValue = 0f,
            maxValue = 30f,
            step = 0.1f
        ),
        // Custom Speed Accuracy
        FloatSettingData(
            title = "Custom Speed Accuracy",
            useValueState = settingsViewModel.useSpeedAccuracy.collectAsState(),
            valueState = settingsViewModel.speedAccuracy.collectAsState(),
            setUseValue = settingsViewModel::setUseSpeedAccuracy,
            setValue = settingsViewModel::setSpeedAccuracy,
            label = "Speed Accuracy (m/s)",
            minValue = 0f,
            maxValue = 100f,
            step = 1f
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                settings.forEach { setting ->
                    when (setting) {
                        is DoubleSettingData -> {
                            DoubleSettingComposable(setting)
                        }
                        is FloatSettingData -> {
                            FloatSettingComposable(setting)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DoubleSettingItem(
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
    SettingItem(
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
fun FloatSettingItem(
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
    SettingItem(
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
private fun <T : Number> SettingItem(
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

sealed class SettingData {
    abstract val title: String
    abstract val useValueState: State<Boolean>
    abstract val setUseValue: (Boolean) -> Unit
    abstract val label: String
    abstract val minValue: Float
    abstract val maxValue: Float
    abstract val step: Float
}

data class DoubleSettingData(
    override val title: String,
    override val useValueState: State<Boolean>,
    val valueState: State<Double>,
    override val setUseValue: (Boolean) -> Unit,
    val setValue: (Double) -> Unit,
    override val label: String,
    override val minValue: Float,
    override val maxValue: Float,
    override val step: Float
) : SettingData()

data class FloatSettingData(
    override val title: String,
    override val useValueState: State<Boolean>,
    val valueState: State<Float>,
    override val setUseValue: (Boolean) -> Unit,
    val setValue: (Float) -> Unit,
    override val label: String,
    override val minValue: Float,
    override val maxValue: Float,
    override val step: Float
) : SettingData()

@Composable
fun DoubleSettingComposable(
    setting: DoubleSettingData
) {
    DoubleSettingItem(
        title = setting.title,
        useValue = setting.useValueState.value,
        onUseValueChange = setting.setUseValue,
        value = setting.valueState.value,
        onValueChange = setting.setValue,
        label = setting.label,
        minValue = setting.minValue,
        maxValue = setting.maxValue,
        step = setting.step
    )
}

@Composable
fun FloatSettingComposable(
    setting: FloatSettingData
) {
    FloatSettingItem(
        title = setting.title,
        useValue = setting.useValueState.value,
        onUseValueChange = setting.setUseValue,
        value = setting.valueState.value,
        onValueChange = setting.setValue,
        label = setting.label,
        minValue = setting.minValue,
        maxValue = setting.maxValue,
        step = setting.step
    )
}