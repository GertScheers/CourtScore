package com.gitje.courtscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gitje.courtscore.ui.theme.CourtScoreTheme
import com.intuit.sdp.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CourtScoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Overview(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun <K, V> MutableMap<K, List<V>>.mergeGames(other: Map<K, List<V>>) {
    for ((key, values) in other) {
        put(key, getOrDefault(key, emptyList()) + values)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(modifier: Modifier) {
    val tennisDummyScores =
        remember { getTennisScoresDummyData().groupBy { it.date.toLocalDate() } }
    val paddleDummyScores =
        remember { getPaddleScoresDummyData().groupBy { it.date.toLocalDate() } }
    val badmintonDummyScores =
        remember { getBadmintonScoresDummyData().groupBy { it.date.toLocalDate() } }
    var showBadminton by remember { mutableStateOf(true) }
    var showTennis by remember { mutableStateOf(true) }
    var showPaddle by remember { mutableStateOf(true) }
    var showFilter by remember { mutableStateOf(false) }
    var showCalendar by remember { mutableStateOf(false) }
    val filteredGames = remember(showBadminton, showTennis, showPaddle) {
        val gameHistory = mutableMapOf<LocalDate, List<Game>>()
        if (showBadminton) gameHistory += badmintonDummyScores
        if (showPaddle) gameHistory.mergeGames(paddleDummyScores)
        if (showTennis) gameHistory.mergeGames(tennisDummyScores)

        gameHistory.toSortedMap(reverseOrder())
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.of("UTC")) // DatePicker operates in UTC
                    .toLocalDate()
                return filteredGames.entries.map { it.key }.contains(date)
            }
        }
    )

    val displayGames = remember(datePickerState.selectedDateMillis, filteredGames) {
        datePickerState.selectedDateMillis?.let { dtm ->
            val date = Instant.ofEpochMilli(dtm)
                .atZone(ZoneId.of("UTC"))
                .toLocalDate()
            filteredGames.filter { it.key == date }
        } ?: filteredGames
    }

    if (showCalendar) {
        DatePickerDialog(
            onDismissRequest = { showCalendar = false },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen._10sdp))) {
                    Text(
                        if(datePickerState.selectedDateMillis != null) "Clear" else "Cancel",
                        modifier = Modifier
                            .padding(
                                end = dimensionResource(R.dimen._20sdp),
                                bottom = dimensionResource(R.dimen._10sdp)
                            )
                            .clickable(
                                onClick = {
                                    datePickerState.selectedDateMillis = null
                                    showCalendar = false
                                }
                            )
                    )
                    Text(
                        "Ok",
                        modifier = Modifier
                            .padding(
                                end = dimensionResource(R.dimen._20sdp),
                                bottom = dimensionResource(R.dimen._10sdp)
                            )
                            .clickable(onClick = { showCalendar = false })
                    )
                }
            }) {
            DatePicker(
                datePickerState,
                showModeToggle = false
            )
        }
    }

    Column(modifier.padding(dimensionResource(R.dimen._5sdp))) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Game history",
                fontSize = 28.sp,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen._5sdp))) {
                Icon(
                    imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_calendar),
                    contentDescription = "Calendar",
                    modifier = Modifier.clickable(onClick = { showCalendar = true })
                )
                Box {
                    Icon(
                        imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_filter),
                        contentDescription = "Filter",
                        modifier = Modifier.clickable(onClick = { showFilter = true })
                    )

                    DropdownMenu(
                        expanded = showFilter,
                        onDismissRequest = { showFilter = false }) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(showTennis, { showTennis = !showTennis })
                                    Text("Show tennis")
                                }
                            },
                            onClick = { showTennis = !showTennis },
                            trailingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_tennis),
                                    contentDescription = "Show tennis"
                                )
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(showPaddle, { showPaddle = !showPaddle })
                                    Text("Show paddle")
                                }
                            },
                            onClick = { showPaddle = !showPaddle },
                            trailingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_padel),
                                    contentDescription = "Show paddle"
                                )
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(showBadminton, { showBadminton = !showBadminton })
                                    Text("Show badminton")
                                }
                            },
                            onClick = { showBadminton = !showBadminton },
                            trailingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_badminton),
                                    contentDescription = "Show badminton"
                                )
                            },
                        )
                    }
                }
            }
        }

        LazyColumn {
            displayGames.entries.forEachIndexed { index, (date, gamesForDate) ->
                item {
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        LocalExpandedState.toggle(date, index == 0)
                    }
                    CollapsibleHeader(date)
                }

                itemsIndexed(gamesForDate, key = { _, game -> game.id }) { index, game ->
                    AnimatedVisibility(LocalExpandedState.isExpanded(date)) {
                        val cardShape = when (index) {
                            gamesForDate.size - 1 -> RoundedCornerShape(
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            )

                            else -> RectangleShape
                        }
                        ScoreCard(game, cardShape)
                        if (index != gamesForDate.size - 1)
                            HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun CollapsibleHeader(
    date: LocalDate
) {
    val expanded = LocalExpandedState.isExpanded(date)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable { LocalExpandedState.toggle(date) },
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatDateForHeader(date),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }
}

fun formatDateForHeader(date: LocalDate): String {
    val today = LocalDate.now()

    return when (date) {
        today -> "Today"
        today.minusDays(1) -> "Yesterday"
        else -> date.toString()   // “2025-11-28”
    }
}


object LocalExpandedState {
    private val expandedState = mutableStateMapOf<LocalDate, Boolean>()

    fun toggle(date: LocalDate, expanded: Boolean? = null) {
        val current = expandedState[date] ?: false
        expandedState[date] = expanded ?: !current
    }

    fun isExpanded(date: LocalDate): Boolean =
        expandedState[date] ?: true
}

enum class PlayerId { P1, P2 }
enum class Sports { Tennis, Paddle, Badminton }

data class Game(
    val date: LocalDateTime,
    val scoreHistory: List<ScoreEvent>,
    val winner: PlayerId,// sent from watch
    val sport: Sports
) {
    val id: UUID = UUID.randomUUID()

    @Composable
    fun getIcon(): ImageVector {
        return ImageVector.vectorResource(
            when (sport) {
                Sports.Tennis -> com.gitje.courtscore.R.drawable.ic_tennis
                Sports.Paddle -> com.gitje.courtscore.R.drawable.ic_padel
                else -> com.gitje.courtscore.R.drawable.ic_badminton
            }
        )
    }
}

class Player(val name: String) {}

data class ScoreEvent(
    val scoringPlayer: Player,
    val scoreAfter: ScoreSnapshot,
    val timestamp: Long = System.currentTimeMillis()
)

data class ScoreSnapshot(
    val set: Int,
    val points: Pair<Int, Int>
)

val gameDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val gameTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")


@Composable
fun ScoreCard(
    game: Game,
    cardShape: Shape
) {
    Card(onClick = { }, shape = cardShape) {
        Box {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen._5sdp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                    Text(game.date.format(gameTimeFormatter))
                    Icon(game.getIcon(), contentDescription = game.sport.name)
                }

                Spacer(Modifier.width(dimensionResource(R.dimen._20sdp)))

                Row(Modifier.weight(1f)) {
                    Column(Modifier.padding(5.dp), horizontalAlignment = Alignment.End) {
                        Text("You")
                        HorizontalDivider(Modifier.width(dimensionResource(R.dimen._50sdp)))
                        Text("Opponent")
                    }
                    game.scoreHistory.groupBy { it.scoreAfter.set }.forEach {
                        Column(
                            Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("${it.value.last().scoreAfter.points.first}")
                            HorizontalDivider(Modifier.width(dimensionResource(R.dimen._10sdp)))
                            Text("${it.value.last().scoreAfter.points.second}")
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .align(Alignment.CenterEnd)
                    .background(if (game.winner == PlayerId.P1) Color.Green else Color.Red)
            ) {
                Text("")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewPreview() {
    CourtScoreTheme {
        Overview(Modifier.fillMaxSize())
    }
}