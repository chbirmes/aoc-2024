import kotlin.math.absoluteValue
import kotlin.math.sign

private fun part1(lines: List<String>) =
    lines.count { line ->
        val steps = toSteps(line)
        isSafe(steps)
    }

private fun part2(lines: List<String>) =
    lines.count { line ->
        val steps = toSteps(line)
        isSafe(steps) || contractions(steps).any { isSafe(it) }
    }

private fun toSteps(line: String) = line.split(" ").map { it.toInt() }
    .windowed(2)
    .map { window -> window[1] - window[0] }

private fun contractions(steps: List<Int>): Sequence<List<Int>> =
    sequenceOf(steps.drop(1), steps.dropLast(1)) +
            (0..<steps.lastIndex).asSequence()
                .map { index ->
                    steps.take(index) + (steps[index] + steps[index + 1]) + steps.drop(index + 2)
                }

private fun isSafe(steps: List<Int>): Boolean {
    val direction = steps[0].sign
    return direction != 0 && steps.all { it.sign == direction && it.absoluteValue <= 3 }
}

fun main() {
    val testInput =
        """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """.trimIndent().lines()
    require(part1(testInput) == 2)
    require(part2(testInput) == 4)

    val realInput = inputOfDay(2)
    println(part1(realInput))
    println(part2(realInput))
}