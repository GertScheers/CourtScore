package com.gitje.courtscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gitje.courtscore.ui.theme.CourtScoreTheme
import com.intuit.sdp.R
import java.time.LocalDate
import java.time.LocalDateTime
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

@Composable
fun Overview(modifier: Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = Sports.entries.toTypedArray()
    val tennisDummyScores =
        remember { getTennisScoresDummyData().groupBy { it.date.toLocalDate() } }
    val paddleDummyScores =
        remember { getPaddleScoresDummyData().groupBy { it.date.toLocalDate() } }
    val badmintonDummyScores =
        remember { getBadmintonScoresDummyData().groupBy { it.date.toLocalDate() } }
    val games =
        remember(selectedIndex) {
            when (selectedIndex) {
                Sports.Tennis.ordinal -> tennisDummyScores
                Sports.Paddle.ordinal -> paddleDummyScores
                else -> badmintonDummyScores
            }
        }

    Column(modifier.padding(dimensionResource(R.dimen._5sdp))) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex
                ) {
                    Text(label.name)
                }
            }
        }

        Spacer(Modifier.height(dimensionResource(R.dimen._10sdp)))

        LazyColumn {
            games.forEach { (date, gamesForDate) ->
                item {
                    CollapsibleHeader(date)
                }

                items(gamesForDate, key = { it.id }) { game ->
                    if (LocalExpandedState.isExpanded(date)) {
                        ScoreCard(game)
                    }
                }
            }
        }
    }
}

@Composable
fun CollapsibleHeader(date: LocalDate) {
    val expanded = LocalExpandedState.isExpanded(date)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { LocalExpandedState.toggle(date) }
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

    fun toggle(date: LocalDate) {
        val current = expandedState[date] ?: true
        expandedState[date] = !current
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
    fun winOrLoss(): String {
        return if (winner == PlayerId.P1) "W" else "L"
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
fun ScoreCard(game: Game) {
    Card(onClick = { }) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen._5sdp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(game.date.format(gameTimeFormatter))

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

            Text(
                game.winOrLoss(), modifier = Modifier
                    .drawBehind {
                        drawCircle(if (game.winOrLoss() == "W") Color.Green else Color.Red)
                    }
                    .padding(dimensionResource(R.dimen._5sdp)))
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