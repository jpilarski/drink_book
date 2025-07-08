package com.example.drinkbook

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.drinkbook.ui.theme.DrinkBookTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private lateinit var logo: ImageView
    private lateinit var waveView: WaveView
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private var lastShakeTime = 0L
    private val start = System.currentTimeMillis()
    private var fromDrawer = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromDrawer = intent.getBooleanExtra("fromDrawer", false)
        setContentView(R.layout.activity_splash_screen)
        logo = findViewById(R.id.logoImageView)
        waveView = findViewById(R.id.waveView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        logo.translationX = 0f
        logo.translationY = 0f
        logo.scaleX = 0f
        logo.scaleY = 0f
        logo.rotation = 0f
        val scaleUpX = ObjectAnimator.ofFloat(logo, "scaleX", 1.2f)
        val scaleUpY = ObjectAnimator.ofFloat(logo, "scaleY", 1.2f)
        val rotateCW = ObjectAnimator.ofFloat(logo, "rotation", 0f, 1080f)
        val scaleDownX = ObjectAnimator.ofFloat(logo, "scaleX", 1f)
        val scaleDownY = ObjectAnimator.ofFloat(logo, "scaleY", 1f)
        val rotateCCW = ObjectAnimator.ofFloat(logo, "rotation", 1080f, 720f)
        scaleUpX.duration = 3000
        scaleUpY.duration = 3000
        rotateCW.duration = 3000
        scaleDownX.duration = 1000
        scaleDownY.duration = 1000
        rotateCCW.duration = 1000
        val pause = ValueAnimator.ofFloat(0f, 0f).apply {
            duration = 1500
        }
        val animator1 = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY, rotateCW)
        }
        val animator2 = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY, rotateCCW)
        }
        val animator2WithPause = AnimatorSet().apply {
            playSequentially(animator2, pause)
        }
        animator1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animator2WithPause.start()
            }
        })
        animator2WithPause.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                if (fromDrawer) {
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }
        })
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        animator1.start()
        waveLevelAnimator.start()
        waveShiftAnimator.start()
        waveColorAnimator.start()
    }

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
            if (acceleration > 2.5f && System.currentTimeMillis() - start < 4000) {
                val now = System.currentTimeMillis()
                if (now - lastShakeTime > 300) {
                    val intensity = ((acceleration - 2.5f) * 10).toInt().coerceAtMost(100)
                    waveView.addBubbles(intensity)
                    lastShakeTime = now
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }
    private val waveLevelAnimator: ValueAnimator = ValueAnimator.ofFloat(0.1f, 0.9f).apply {
        duration = 4000L
        addUpdateListener { animation ->
            val level = animation.animatedValue as Float
            waveView.setWaveLevel(level)
        }
    }
    private val waveShiftAnimator: ValueAnimator =
        ValueAnimator.ofFloat(0f, (6 * Math.PI).toFloat()).apply {
            duration = 4000L
            addUpdateListener { animation ->
                val shift = animation.animatedValue as Float
                waveView.setWaveShift(shift)
            }
        }
    private val waveColorAnimator: ValueAnimator = ValueAnimator.ofObject(
        ArgbEvaluator(), "#B39DDB".toColorInt(), "#512DA8".toColorInt()
    ).apply {
        duration = 4000L
        addUpdateListener { animation ->
            val color = animation.animatedValue as Int
            waveView.setWaveColor(color)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }
}

class WaveView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#B39DDB".toColorInt()
        style = Paint.Style.FILL
    }
    private var waveShift = 0f
    fun setWaveShift(shift: Float) {
        waveShift = shift
        invalidate()
    }

    private var waveLevel = 0f
    fun setWaveLevel(level: Float) {
        waveLevel = level
        invalidate()
    }

    fun setWaveColor(color: Int) {
        wavePaint.color = color
        invalidate()
    }

    data class Bubble(
        var x: Float,
        var y: Float,
        var radius: Float = Random.nextFloat() * 6f + 4f,
        var alpha: Int = 180,
        var scale: Float = 1f,
        var lifetime: Long = Random.nextLong(750L, 1500L),
        var startTime: Long = System.currentTimeMillis()
    )

    private val bubbles = mutableListOf<Bubble>()
    private val bubblePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#FFFFFF".toColorInt()
        alpha = 180
    }
    private val bubbleAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = Long.MAX_VALUE
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            val now = System.currentTimeMillis()
            val iterator = bubbles.iterator()
            while (iterator.hasNext()) {
                val bubble = iterator.next()
                val progress = (now - bubble.startTime).toFloat() / bubble.lifetime
                if (progress >= 1f) {
                    iterator.remove()
                } else {
                    bubble.scale = 1f + 0.5f * progress
                    bubble.alpha = (180 * (1f - progress)).toInt()
                    bubble.y -= progress
                }
            }
            invalidate()
        }
    }

    fun addBubbles(amount: Int) {
        val baseLevel = height * (1f - waveLevel.coerceIn(0f, 1f))
        for (i in 0 until amount) {
            val x = Random.nextFloat() * width
            val y = baseLevel + Random.nextFloat() * (height - baseLevel) + 100f
            bubbles.add(Bubble(x, y))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bubbleAnimator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bubbleAnimator.cancel()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val path = Path()
        val width = width.toFloat()
        val height = height.toFloat()
        val amplitude = 40f
        val frequency = 2 * Math.PI / width
        path.moveTo(0f, height)
        val baseLevel = height * (1f - waveLevel.coerceIn(0f, 1f))
        for (x in 0..width.toInt()) {
            val y = (amplitude * sin(frequency * x - waveShift)).toFloat()
            path.lineTo(x.toFloat(), baseLevel + y)
        }
        path.lineTo(width, height)
        path.close()
        canvas.drawPath(path, wavePaint)
        for (bubble in bubbles) {
            bubblePaint.alpha = bubble.alpha
            canvas.drawCircle(bubble.x, bubble.y, bubble.radius * bubble.scale, bubblePaint)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            DrinkBookTheme {
                DrinkBookApp(context = context)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkBookApp(context: Context) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: DrinksViewModel = viewModel()

    val colors = MaterialTheme.colorScheme
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colors.surface
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                    keyboardController?.hide()
                                }
                                navController.navigate("drinkTabs") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                            .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Home, contentDescription = "Home", tint = colors.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Home", color = colors.onSurface)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                    keyboardController?.hide()
                                }
                                context.startActivity(
                                    Intent(context, SplashActivity::class.java).apply {
                                        putExtra("fromDrawer", true)
                                    })
                            }
                            .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "See Animation",
                            tint = colors.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("See Animation", color = colors.onSurface)
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Filter by name",
                                tint = colors.primary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear search",
                                        tint = colors.primary
                                    )
                                }
                            }
                        },
                        label = { Text("Filter by name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colors.onSurface,
                            unfocusedTextColor = colors.onSurface,
                            focusedLabelColor = colors.primary,
                            unfocusedLabelColor = colors.onSurface,
                            focusedLeadingIconColor = colors.primary,
                            unfocusedLeadingIconColor = colors.onSurface,
                            focusedTrailingIconColor = colors.primary,
                            cursorColor = colors.primary,
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.secondary
                        )
                    )
                }
            }
        }) {
        Scaffold(
            containerColor = colors.background, topBar = {
                val currentEntry = navController.currentBackStackEntryAsState().value
                val route = currentEntry?.destination?.route

                TopAppBar(
                    title = {
                        if (route == "drinkTabs") Text("DrinkBook", color = colors.onPrimary)
                    }, navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = colors.onPrimary
                            )
                        }
                    }, actions = {
                        if (route?.startsWith("drinkDetails") == true) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = colors.onPrimary
                                )
                            }
                        }
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.primary,
                        navigationIconContentColor = colors.onPrimary,
                        titleContentColor = colors.onPrimary,
                        actionIconContentColor = colors.onPrimary
                    )
                )
            }) { padding ->
            NavHost(
                navController = navController,
                startDestination = "drinkTabs",
                modifier = Modifier.padding(padding)
            ) {
                composable("drinkTabs") {
                    viewModel.loadAllDrinks()
                    DrinkTabsScreen(
                        context = context, searchQuery = searchQuery, onDrinkClick = { drink ->
                            navController.navigate("drinkDetails/${drink.idDrink}")
                        })
                }
                composable("drinkDetails/{drinkId}") { backStackEntry ->
                    val drinkId = backStackEntry.arguments?.getString("drinkId")?.toIntOrNull()
                    LaunchedEffect(drinkId) {
                        drinkId?.let { viewModel.loadOneDrink(it) }
                    }
                    val drink by viewModel.selectedDrink.observeAsState()
                    drink?.let {
                        DrinkDetailsScreen(
                            context = context, drink = it
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrinkTabsScreen(
    context: Context, searchQuery: String, onDrinkClick: (Drink) -> Unit
) {
    val viewModel: DrinksViewModel = viewModel()
    val allDrinks by viewModel.allDrinks.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadAllDrinks()
    }
    val filteredDrinks = allDrinks.filter { it.strDrink.contains(searchQuery, ignoreCase = true) }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedTab = page
        }
    }
    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }
    val colors = MaterialTheme.colorScheme
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = colors.secondary
                    )
                }) {
                val tabTexts = listOf("Home", "Alcoholic", "Non alcoholic")
                tabTexts.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) colors.secondary else colors.onSecondaryContainer.copy(
                                    alpha = 0.7f
                                ),
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        })
                }
            }
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> DrinkListScreen(
                        context = context, drinks = emptyList(), onDrinkClick = onDrinkClick
                    )

                    1 -> DrinkListScreen(
                        context = context,
                        drinks = filteredDrinks.filter { it.strAlcoholic == "Alcoholic" },
                        onDrinkClick = onDrinkClick
                    )

                    2 -> DrinkListScreen(
                        context = context,
                        drinks = filteredDrinks.filter { it.strAlcoholic != "Alcoholic" },
                        onDrinkClick = onDrinkClick
                    )
                }
            }
        }
    }
}

@Composable
fun DrinkListScreen(
    context: Context, drinks: List<Drink>, onDrinkClick: (Drink) -> Unit
) {
    Scaffold { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            if (drinks.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon_round),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(240.dp)
                )
            } else {
                val itemMinWidth = 180.dp
                val columns = max(2, (maxWidth / itemMinWidth).toInt())

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(drinks) { drink ->
                        DrinkGridItem(
                            context = context, drink = drink, onItemClick = onDrinkClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrinkGridItem(
    context: Context, drink: Drink, onItemClick: (Drink) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val imageRes = getDrinkImage(context = context, drink.strImg)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.secondaryContainer)
            .clickable { onItemClick(drink) }
            .fillMaxWidth()
            .height(220.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = drink.strDrink,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = drink.strDrink,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSecondaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight", "QueryPermissionsNeeded")
@Composable
fun DrinkDetailsScreen(
    context: Context, drink: Drink
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.screenHeightDp > configuration.screenWidthDp
    val imageSize = with(LocalDensity.current) {
        val screenWidth = configuration.screenWidthDp.dp.toPx()
        val screenHeight = configuration.screenHeightDp.dp.toPx()
        min(screenWidth * 0.4f, screenHeight * 0.6f).toDp()
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val drinkText = formatRecipe(drink)
                    val smsIntent = Intent(Intent.ACTION_VIEW).apply {
                        data = "sms:".toUri()
                        putExtra("sms_body", drinkText)
                    }

                    val packageManager = context.packageManager
                    if (smsIntent.resolveActivity(packageManager) != null) {
                        try {
                            context.startActivity(smsIntent)
                        } catch (e: Exception) {
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Error with SMS app",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    } else {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "No SMS app",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                containerColor = colors.primaryContainer,
                contentColor = colors.onPrimaryContainer
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sms),
                    contentDescription = "Send SMS"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        if (isPortrait) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = getDrinkImage(context, drink.strImg)),
                    contentDescription = drink.strDrink,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                DrinkDetailsContent(drink = drink, colors = colors)
                Spacer(modifier = Modifier.height(120.dp))
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(imageSize)
                            .clip(RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = getDrinkImage(context, drink.strImg)),
                            contentDescription = drink.strDrink,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DrinkDetailsContent(drink = drink, colors = colors)
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
    }
}


@Composable
private fun DrinkDetailsContent(drink: Drink, colors: ColorScheme) {
    Text(
        text = drink.strDrink,
        style = MaterialTheme.typography.headlineLarge,
        color = colors.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp)
    )

    Text(
        text = "Use ${drink.strGlass.lowercase()}",
        style = MaterialTheme.typography.titleMedium,
        color = colors.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )

    Text(
        text = "Ingredients",
        style = MaterialTheme.typography.titleMedium,
        color = colors.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    val ingredients = listOf(
        drink.strIngredient1 to drink.strMeasure1,
        drink.strIngredient2 to drink.strMeasure2,
        drink.strIngredient3 to drink.strMeasure3,
        drink.strIngredient4 to drink.strMeasure4,
        drink.strIngredient5 to drink.strMeasure5,
        drink.strIngredient6 to drink.strMeasure6,
    ).filter { it.first != null && it.first!!.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.outline, RoundedCornerShape(8.dp))
            .background(colors.surfaceVariant)
            .clip(RoundedCornerShape(8.dp))
    ) {
        ingredients.forEachIndexed { index, (ingredient, measure) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (index == 0) 0.dp else 1.dp,
                        color = colors.outline
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = ingredient!!.trim(),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
                Text(
                    text = measure?.trim() ?: "",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Instructions:",
        style = MaterialTheme.typography.titleMedium,
        color = colors.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    Text(
        text = drink.strInstructions,
        style = MaterialTheme.typography.bodyMedium,
        color = colors.onBackground,
        textAlign = TextAlign.Justify,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    TimerSection()
}


@Composable
fun TimerSection() {
    val colors = MaterialTheme.colorScheme

    var minutesSelected by rememberSaveable { mutableIntStateOf(0) }
    var secondsSelected by rememberSaveable { mutableIntStateOf(0) }
    var timeLeft by rememberSaveable { mutableIntStateOf(0) }
    var isRunning by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        val startTime = System.currentTimeMillis()
        var lastTick = startTime
        while (isRunning && timeLeft > 0) {
            val now = System.currentTimeMillis()
            val elapsed = (now - lastTick) / 1000
            if (elapsed > 0) {
                timeLeft = (timeLeft - elapsed.toInt()).coerceAtLeast(0)
                lastTick = now
            }
            delay(100L)
        }
        if (timeLeft == 0) {
            isRunning = false
        }
    }

    LaunchedEffect(minutesSelected, secondsSelected) {
        if (!isRunning) {
            timeLeft = minutesSelected * 60 + secondsSelected
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Timer",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = formatTime(timeLeft),
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = colors.onBackground,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberInput(
                label = "Min",
                value = minutesSelected,
                range = 0..59,
                onValueChange = { if (!isRunning) minutesSelected = it }
            )
            Spacer(modifier = Modifier.width(24.dp))
            NumberInput(
                label = "Sek",
                value = secondsSelected,
                range = 0..59,
                onValueChange = { if (!isRunning) secondsSelected = it }
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {
                    if (timeLeft > 0) isRunning = true
                },
                enabled = !isRunning && timeLeft > 0,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (!isRunning && timeLeft > 0) Color(0xFF4CAF50) else Color.Gray,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = { isRunning = false },
                enabled = isRunning,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (isRunning) Color.Gray else Color.LightGray,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Stop",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = {
                    isRunning = false
                    timeLeft = 0
                    minutesSelected = 0
                    secondsSelected = 0
                },
                enabled = timeLeft > 0 || isRunning,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (timeLeft > 0 || isRunning) Color(0xFFF44336) else Color.LightGray,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun NumberInput(
    label: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember(value) { mutableStateOf(value.toString()) }

    LaunchedEffect(value) {
        if (text != value.toString()) {
            text = value.toString()
        }
    }

    TextField(
        value = text,
        onValueChange = { newText ->
            if (newText.all { it.isDigit() } || newText.isEmpty()) {
                text = newText

                val intValue = newText.toIntOrNull()
                if (intValue != null) {
                    val coerced = intValue.coerceIn(range)
                    if (coerced != value) {
                        onValueChange(coerced)
                    }
                }
            }
        },
        label = { Text(label) },
        singleLine = true,
        modifier = modifier.width(80.dp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}



@SuppressLint("DefaultLocale")
private fun formatTime(totalSeconds: Int): String {
    val minutes = (totalSeconds / 60).coerceAtLeast(0)
    val seconds = (totalSeconds % 60).coerceAtLeast(0)
    return "%02d:%02d".format(minutes, seconds)
}


@SuppressLint("DiscouragedApi")
fun getDrinkImage(
    context: Context, imageName: String?
): Int {
    return imageName?.let {
        context.resources.getIdentifier(it, "drawable", context.packageName)
    } ?: R.drawable.app_icon
}




fun formatRecipe(drink: Drink): String {
    val ingredients = listOf(
        drink.strIngredient1 to drink.strMeasure1,
        drink.strIngredient2 to drink.strMeasure2,
        drink.strIngredient3 to drink.strMeasure3,
        drink.strIngredient4 to drink.strMeasure4,
        drink.strIngredient5 to drink.strMeasure5,
        drink.strIngredient6 to drink.strMeasure6
    ).filter { it.first != null && it.first!!.isNotBlank() }

    val ingredientsText = ingredients.mapIndexed { index, (ingredient, measure) ->
        val comma = if (index == ingredients.lastIndex) "" else ","
        "- ${ingredient!!.trim()}: ${measure?.trim() ?: ""}$comma"
    }.joinToString("\n")

    return buildString {
        append("Recipe for ${drink.strDrink}:\n\n")
        append("use ${drink.strGlass.lowercase()}\n")
        append("ingredients:\n")
        append(ingredientsText)
        append("\n\n")
        append(drink.strInstructions.trim())
    }
}
