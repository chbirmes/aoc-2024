import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun parseRobots(lines: List<String>) =
    lines.map { line ->
        val position = line.substringAfter("p=")
            .substringBefore(" ")
            .split(',')
            .map { it.toInt() }
            .let { D2.Position(it[0], it[1]) }
        val velocity = line.substringAfter("v=")
            .split(',')
            .map { it.toInt() }
            .let { D2.Position(it[0], it[1]) }
        position to velocity
    }

fun Pair<D2.Position, D2.Position>.pacmanMove(width: Int, height: Int) =
    (first + second).let { D2.Position(it.x.mod(width), it.y.mod(height)) } to second

fun Pair<D2.Position, D2.Position>.pacmanMoveBackwards(width: Int, height: Int) =
    (first - second).let { D2.Position(it.x.mod(width), it.y.mod(height)) } to second

private fun part1(lines: List<String>, width: Int, height: Int): Int {
    val initialRobots = parseRobots(lines)

    return (1..100).fold(initialRobots) { robots, _ ->
        robots.map { it.pacmanMove(width, height) }
    }
        .filterNot { (position, _) -> position.x == width / 2 || position.y == height / 2 }
        .groupBy { (position, _) -> quadrant(position, width / 2, height / 2) }
        .values.map { it.size }
        .fold(1, Int::times)
}

fun quadrant(position: D2.Position, halfWidth: Int, halfHeight: Int) =
    if (position.x < halfWidth && position.y < halfHeight) 0
    else if (position.x > halfWidth && position.y < halfHeight) 1
    else if (position.x > halfWidth && position.y > halfHeight) 2
    else 3

fun main() {
    val testInput =
        """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
        """.trimIndent().lines()
    require(part1(testInput, 11, 7) == 12)

    val realInput = inputOfDay(14)
    println(part1(realInput, 101, 103))
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(size = DpSize(1600.dp, 1200.dp)),
            title = "Advent of Code 2024 - Day 14 - Restroom Robots"
        ) {
            Day14App()
        }
    }
}