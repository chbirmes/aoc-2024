import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

private const val pageSize = 100

@Composable
@Preview
fun Day14App() {

    val robots = parseRobots(inputOfDay(14))

    val (state, setState) = remember { mutableStateOf(AppState(robots)) }

    Column {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 120.dp)) {
            items(pageSize) { index ->
                Column {
                    Text("${state.index + 1 + index}")
                    Image(
                        painter = MyPainter(Size(120f, 120f), state.robots[index].map { it.first }),
                        contentDescription = index.toString()
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { setState(state.previousPage()) },
                enabled = state.index > 0
            ) {
                Text("Previous")
            }
            Button(onClick = { setState(state.nextPage()) }) {
                Text("Next")
            }
        }
    }
}

class MyPainter(override val intrinsicSize: Size, private val positions: List<D2.Position>) : Painter() {
    override fun DrawScope.onDraw() {
        val offsets = positions.map { Offset(it.x.toFloat(), it.y.toFloat()) }
        drawPoints(offsets, PointMode.Points, Color.Blue, strokeWidth = 1f)
    }
}

private data class AppState(val index: Int, val robots: List<List<Pair<D2.Position, D2.Position>>>) {
    constructor(initialRobots: List<Pair<D2.Position, D2.Position>>) : this(0, nextRobots(initialRobots))

    fun nextPage() = AppState(
        index + pageSize,
        nextRobots(robots.last())
    )

    fun previousPage() = AppState(
        index - pageSize,
        (1..pageSize).runningFold(robots.first()) { acc, _ -> acc.map { it.pacmanMoveBackwards(101, 103) } }.drop(1).reversed()
    )

    companion object {
        private fun nextRobots(last: List<Pair<D2.Position, D2.Position>>) =
            (1..pageSize).runningFold(last) { acc, _ -> acc.map { it.pacmanMove(101, 103) } }.drop(1)

    }
}
