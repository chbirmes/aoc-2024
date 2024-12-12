private data class Equation(val testValue: Long, val numbers: List<Long>) {
    companion object {
        fun parse(s: String) =
            Equation(
                s.substringBefore(':').toLong(),
                s.substringAfter(": ").split(" ").map { it.toLong() }
            )
    }
}

private fun solvable(equation: Equation): Boolean {
    val combinationCount = 1 shl (equation.numbers.size - 1)
    return (0..<combinationCount).any { key ->
        val result = equation.numbers.drop(1).foldIndexed(equation.numbers.first()) { index, acc, number ->
            val operationIndex = equation.numbers.size - index - 2
            if (key and (1 shl operationIndex) == 0)
                acc + number
            else
                acc * number
        }
        result == equation.testValue
    }
}

private fun Int.exp3() = (0..<this).fold(1) { acc, _ -> acc * 3 }

private fun solvable2(equation: Equation): Boolean {
    val combinationCount = (equation.numbers.size - 1).exp3()
    return (0..<combinationCount).any { key ->
        val result = equation.numbers.drop(1).foldIndexed(equation.numbers.first()) { index, acc, number ->
            val operationIndex = equation.numbers.size - index - 2
            when ((key / operationIndex.exp3()) % 3) {
                0 -> acc + number
                1 -> acc * number
                else -> (acc.toString() + number.toString()).toLong()
            }

        }
        result == equation.testValue
    }
}

private fun part1(lines: List<String>) =
    lines.map { Equation.parse(it) }
        .filter { solvable(it) }
        .sumOf { it.testValue }

private fun part2(lines: List<String>) =
    lines.map { Equation.parse(it) }
        .filter { solvable2(it) }
        .sumOf { it.testValue }

fun main() {
    val testInput =
        """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """.trimIndent().lines()
    require(part1(testInput) == 3749L)
    require(part2(testInput) == 11387L)

    val realInput = inputOfDay(7)
    println(part1(realInput))
    println(part2(realInput))
}