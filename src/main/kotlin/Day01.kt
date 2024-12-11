import kotlin.math.absoluteValue

private fun part1(lines: List<String>) =
    toIntListPair(lines)
        .let { lists -> lists.first.sorted() to lists.second.sorted() }
        .let { lists -> lists.first.zip(lists.second) }
        .sumOf { (it.first - it.second).absoluteValue }

private fun part2(lines: List<String>) =
    toIntListPair(lines)
        .let { lists -> lists.first.sumOf { first -> first * lists.second.count { second -> second == first } } }

private fun toIntListPair(lines: List<String>) =
    lines.map { line -> line.split("   ").let { it[0].toInt() to it[1].toInt() } }
        .fold(emptyList<Int>() to emptyList<Int>()) { lists, integers -> lists.first + integers.first to lists.second + integers.second }

fun main() {
    val testInput =
        """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent().lines()
    require(part1(testInput) == 11)
    require(part2(testInput) == 31)

    val realInput = inputOfDay(1)
    println(part1(realInput))
    println(part2(realInput))
}