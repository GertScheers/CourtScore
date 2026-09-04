package com.gitje.courtscore.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gitje.courtscore.getBadmintonScoresDummyData
import com.gitje.courtscore.getPadelScoresDummyData
import com.gitje.courtscore.getTennisScoresDummyData
import com.gitje.courtscore.logic.HistoryViewModel
import com.gitje.courtscore.models.Game
import com.gitje.courtscore.models.PlayerId
import com.gitje.courtscore.ui.theme.CourtScoreTheme
import com.gitje.courtscore.utils.mergeGames
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.collections.component1
import kotlin.collections.component2
import androidx.compose.ui.platform.LocalLocale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(modifier: Modifier) {
    val viewModel: HistoryViewModel = koinViewModel()
    val tennisDummyScores =
        remember { (getTennisScoresDummyData() + getTennisScoresDummyData() + getTennisScoresDummyData() + getTennisScoresDummyData()).groupBy { it.date.toLocalDate() } }
    val padelDummyScores =
        remember { (getPadelScoresDummyData() + getPadelScoresDummyData() + getPadelScoresDummyData() + getPadelScoresDummyData()).groupBy { it.date.toLocalDate() } }
    val badmintonDummyScores =
        remember { (getBadmintonScoresDummyData() + getBadmintonScoresDummyData() + getBadmintonScoresDummyData() + getBadmintonScoresDummyData()).groupBy { it.date.toLocalDate() } }
    val showBadminton by viewModel.showBadminton.collectAsState()
    val showTennis by viewModel.showTennis.collectAsState()
    val showPadel by viewModel.showPadel.collectAsState()
    var showFilter by remember { mutableStateOf(false) }
    var showCalendar by remember { mutableStateOf(false) }
    val filteredGames = remember(showBadminton, showTennis, showPadel) {
        val gameHistory = mutableMapOf<LocalDate, List<Game>>()
        if (showBadminton) gameHistory += badmintonDummyScores
        if (showPadel) gameHistory.mergeGames(padelDummyScores)
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
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        if (datePickerState.selectedDateMillis != null) "Clear" else "Cancel",
                        modifier = Modifier
                            .padding(
                                end = 16.dp,
                                bottom = 8.dp
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
                                end = 16.dp,
                                bottom = 8.dp
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

    Column(modifier.padding(4.dp)) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Game history",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = MaterialTheme.typography.headlineLarge.fontFamily
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_filter),
                    contentDescription = "Filter",
                    modifier = Modifier.clickable(onClick = { showFilter = !showFilter })
                )

                Icon(
                    imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_calendar),
                    contentDescription = "Calendar",
                    modifier = Modifier.clickable(onClick = { showCalendar = true })
                )
            }
        }

        AnimatedVisibility(showFilter) {
            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ElevatedFilterChip(
                    selected = showBadminton,
                    onClick = { viewModel.setShowBadminton(!showBadminton) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_badminton),
                                contentDescription = "Show badminton"
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Badminton")
                        }
                    }
                )
                ElevatedFilterChip(
                    selected = showTennis,
                    onClick = { viewModel.setShowTennis(!showTennis) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_tennis),
                                contentDescription = "Show Tennis"
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Tennis")
                        }
                    }
                )
                ElevatedFilterChip(
                    selected = showPadel,
                    onClick = { viewModel.setShowPadel(!showPadel) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.vectorResource(com.gitje.courtscore.R.drawable.ic_padel),
                                contentDescription = "Show Padel"
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Padel")
                        }
                    }
                )
            }
        }

        var isDragging by remember { mutableStateOf(false) }
        var dragProgress by remember { mutableFloatStateOf(0f) }
        val gameListState = rememberLazyListState()
        val groups = displayGames.entries.toList()
        val groupCount = groups.size

        val coroutineScope = rememberCoroutineScope()

        Box(
            Modifier
                .fillMaxSize()
        ) {
            val scrollState = rememberScrollState(0)
            LazyColumn(state = gameListState) {
                displayGames.entries.forEach { (date, gamesForDate) ->
                    val maxSet =
                        gamesForDate.maxOf { it.scoreHistory.maxOf { e -> e.scoreAfter.set } }

                    stickyHeader {
                        Surface(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp, start = 12.dp, end = 12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(bottomStart = 20f, bottomEnd = 20f),
                            shadowElevation = 8.dp
                        ) {
                            GameDateHeader(
                                date = date,
                                wins = gamesForDate.count { g -> g.winner == PlayerId.P1 },
                                losses = gamesForDate.count { g -> g.winner == PlayerId.P2 }
                            )
                            Row(Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
                                Spacer(Modifier.weight(4f))
                                Row(Modifier.weight(6f).padding(end = 8.dp).horizontalScroll(scrollState)) {
                                    for (i in 0..maxSet) {
                                        Text(
                                            "Set ${i + 1}",
                                            modifier = Modifier.width(50.dp),
                                            textAlign = TextAlign.Center,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    }
                                }
                            }
                        }
                    }

                    itemsIndexed(gamesForDate, key = { _, game -> game.id }) { index, game ->
                        ScoreEntry(
                            game,
                            scrollState,
                            maxSets = maxSet
                        )
                        if (index != gamesForDate.size - 1)
                            HorizontalDivider(Modifier.padding(horizontal = 12.dp))
                    }
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .widthIn(min = 48.dp)
                    .pointerInput(displayGames.size) {
                        detectVerticalDragGestures(
                            onDragStart = { isDragging = true },
                            onDragEnd = { isDragging = false },
                            onDragCancel = { isDragging = false },
                            onVerticalDrag = { change, _ ->
                                change.consume()
                                val newProgress = (change.position.y / size.height).coerceIn(0f, 1f)
                                dragProgress = newProgress

                                if (groupCount == 0) return@detectVerticalDragGestures

                                val targetGroupIndex = (dragProgress * (groupCount - 1))
                                    .coerceIn(0f, (groupCount - 1).toFloat())
                                    .toInt()

                                // map group index -> lazy column item index (each group = header + N items)
                                var lazyColumnIndex = 0
                                for (i in 0 until targetGroupIndex) {
                                    lazyColumnIndex += 1 + groups[i].value.size
                                }

                                coroutineScope.launch {
                                    gameListState.scrollToItem(lazyColumnIndex)
                                }
                            }
                        )
                    }
            ) {
                val trackHeightPx = constraints.maxHeight.toFloat()

                // Sync thumb position with scroll state when NOT dragging
                val activeProgress = if (isDragging) {
                    dragProgress
                } else {
                    val firstIdx = gameListState.firstVisibleItemIndex
                    if (groupCount == 0) 0f else {
                        // find which group contains firstVisibleItemIndex
                        var cumulative = 0
                        var currentGroup = 0
                        for ((i, entry) in groups.withIndex()) {
                            // header
                            if (firstIdx == cumulative) {
                                currentGroup = i; break
                            }
                            cumulative += 1
                            // items
                            val itemsCount = entry.value.size
                            if (firstIdx < cumulative + itemsCount) {
                                currentGroup = i; break
                            }
                            cumulative += itemsCount
                        }
                        if (groupCount > 1) currentGroup.toFloat() / (groupCount - 1) else 0f
                    }
                }

                val thumbOffsetY = with(LocalDensity.current) {
                    (activeProgress * trackHeightPx).toDp()
                }

                // Visual Scrollbar Thumb
                androidx.compose.animation.AnimatedVisibility(
                    gameListState.isScrollInProgress || isDragging,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .offset(y = thumbOffsetY)
                        .align(Alignment.TopEnd)
                ) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        shadowElevation = 6.dp
                    ) {
                        Column {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                "scroll up",
                                modifier = Modifier.rotate(180f)
                            )
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                "scroll down"
                            )
                        }
                    }
                }

                val bubbleGroupIndex = if (groupCount == 0) null else {
                    if (isDragging) {
                        (dragProgress * (groupCount - 1)).coerceIn(0f, (groupCount - 1).toFloat())
                            .toInt()
                    } else {
                        // derive from firstVisibleItemIndex like above
                        val firstIdx = gameListState.firstVisibleItemIndex
                        var cumulative = 0
                        var currentGroup = 0
                        for ((i, entry) in groups.withIndex()) {
                            if (firstIdx == cumulative) {
                                currentGroup = i; break
                            }
                            cumulative += 1
                            val itemsCount = entry.value.size
                            if (firstIdx < cumulative + itemsCount) {
                                currentGroup = i; break
                            }
                            cumulative += itemsCount
                        }
                        currentGroup
                    }
                }

                // Date Bubble (Google Photos style indicator)
                androidx.compose.animation.AnimatedVisibility(
                    visible = isDragging && displayGames.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .offset(y = thumbOffsetY)
                        .align(Alignment.TopEnd)
                        .padding(end = 48.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(25),
                        color = MaterialTheme.colorScheme.background,
                        shadowElevation = 4.dp,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(
                            text = bubbleGroupIndex?.let { groups[it].key.toString() } ?: "",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameDateHeader(
    date: LocalDate,
    wins: Int,
    losses: Int
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE", LocalLocale.current.platformLocale)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = formatDateForHeader(date),
                textAlign = TextAlign.Start
            )

            Text(
                text = "${date.format(dayFormatter).take(3)}," +
                        " ${date.month.name.take(3)} ${date.dayOfMonth}",
                textAlign = TextAlign.Start
            )
        }
        Text(
            "${wins}W - ${losses}L",
            textAlign = TextAlign.End,
            fontSize = 18.sp
        )
    }
}

fun formatDateForHeader(date: LocalDate): String {
    val today = LocalDate.now()

    val daysAgo = ChronoUnit.DAYS.between(date, today)
    val monthsAgo = ChronoUnit.MONTHS.between(date, today)
    val yearsAgo = ChronoUnit.YEARS.between(date, today)

    return when {
        daysAgo == 0L -> "Today"
        daysAgo == 1L -> "Yesterday"
        daysAgo <= 7L -> "Last week"
        daysAgo <= 14L -> "2 weeks ago"
        daysAgo <= 21L -> "3 weeks ago"
        daysAgo <= 27L -> "4 weeks ago"
        monthsAgo < 1L -> "This month"
        monthsAgo < 12L -> "${monthsAgo + 1} Months ago" // or exact monthsAgo based on your needs
        yearsAgo == 1L -> "1 year ago"
        yearsAgo < 4L -> "$yearsAgo years ago"
        else -> "Long ago"
    }
}

@Composable
fun ScoreEntry(
    game: Game,
    scrollState: ScrollState,
    maxSets: Int
) {
    Box(Modifier.padding(horizontal = 12.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(Modifier.weight(4f), verticalAlignment = Alignment.CenterVertically) {
                Icon(game.getIcon(), contentDescription = game.sport.name)

                Spacer(Modifier.width(16.dp))

                Column(Modifier.padding(4.dp), horizontalAlignment = Alignment.End) {
                    Text("You", fontWeight = if (game.winner == PlayerId.P1) FontWeight.SemiBold else FontWeight.Thin)
                    HorizontalDivider(Modifier.width(48.dp))
                    Text("Opponent", fontWeight = if (game.winner == PlayerId.P2) FontWeight.SemiBold else FontWeight.Thin)
                }
            }

            Row(
                Modifier
                    .weight(6f)
                    .horizontalScroll(scrollState)
            ) {
                game.scoreHistory.sortedBy { it.scoreAfter.set }
                    .groupBy { it.scoreAfter.set }.toList().forEach { pair ->
                        Column(
                            Modifier
                                .padding(vertical = 4.dp)
                                .width(50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val myScore = pair.second.last().scoreAfter.points.first
                            val opponentScore = pair.second.last().scoreAfter.points.second
                            Text(
                                "$myScore",
                                fontWeight = if (myScore > opponentScore) FontWeight.Bold else FontWeight.Thin
                            )
                            HorizontalDivider(Modifier.width(8.dp))
                            Text(
                                "$opponentScore",
                                fontWeight = if (opponentScore > myScore) FontWeight.ExtraBold else FontWeight.Thin
                            )
                        }
                    }

                val gameSets = game.scoreHistory.maxOf { it.scoreAfter.set }
                if(maxSets > gameSets)
                    Spacer(Modifier.width((50*(maxSets-gameSets)).dp))
            }
        }

        Box(
            modifier = Modifier
                .width(4.dp)
                .align(Alignment.CenterStart)
                .background(if (game.winner == PlayerId.P1) Color.Green else Color.Red)
        ) {
            Text("")
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