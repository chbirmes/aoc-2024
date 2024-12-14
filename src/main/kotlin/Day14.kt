import java.awt.Canvas
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame

private fun parseRobots(lines: List<String>) =
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

private fun Pair<D2.Position, D2.Position>.pacmanMove(width: Int, height: Int) =
    (first + second).let { D2.Position(it.x.mod(width), it.y.mod(height)) } to second

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

private fun part2(lines: List<String>) {
    val initialRobots = parseRobots(lines)

    val finalRobots = (1..6620).fold(initialRobots) { robots, _ ->
        robots.map { it.pacmanMove(101, 103) }
    }

    val img = BufferedImage(101, 103, BufferedImage.TYPE_BYTE_BINARY).apply {
        finalRobots.forEach { (position, _) -> setRGB(position.x, position.y, Color.WHITE.rgb) }
    }

    val canvas = object : Canvas() {
        override fun paint(g: Graphics?) {
            g?.drawImage(img, 0, 0, null)
        }
    }

    JFrame("AOC 2014 - Day 14").apply {
        size = Dimension(150, 150)
        add(canvas)
        isVisible = true
    }
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
    part2(realInput)
}