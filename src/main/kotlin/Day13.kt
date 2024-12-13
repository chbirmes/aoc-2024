private data class LinearEquation(val result: Long, val a: Long, val b: Long) {
    operator fun times(factor: Long) = LinearEquation(result * factor, a * factor, b * factor)
    operator fun minus(other: LinearEquation) = LinearEquation(result - other.result, a - other.a, b - other.b)
}

private fun Pair<LinearEquation, LinearEquation>.solve(): Pair<Long, Long>? {
    val bOnly = (first * second.a) - (second * first.a)
    if (bOnly.result % bOnly.b != 0L) return null
    val b = bOnly.result / bOnly.b
    val aOnly = LinearEquation(first.result - first.b * b, first.a, 0)
    if (aOnly.result % aOnly.a != 0L) return null
    val a = aOnly.result / aOnly.a
    return a to b
}

private fun parseEquationPair(lines: List<String>): Pair<LinearEquation, LinearEquation> {
    val eqX = LinearEquation(
        lines[2].substringAfter("X=").substringBefore(',').toLong(),
        lines[0].substringAfter("X+").substringBefore(',').toLong(),
        lines[1].substringAfter("X+").substringBefore(',').toLong()
    )
    val eqY = LinearEquation(
        lines[2].substringAfter("Y=").toLong(),
        lines[0].substringAfter("Y+").toLong(),
        lines[1].substringAfter("Y+").toLong()
    )
    return eqX to eqY
}

private fun part1(lines: List<String>) =
    lines.asSequence().filter { it.isNotEmpty() }
        .chunked(3)
        .mapNotNull { chunk -> parseEquationPair(chunk).solve() }
        .sumOf { it.first * 3 + it.second }

private fun part2(lines: List<String>) =
    lines.asSequence().filter { it.isNotEmpty() }
        .chunked(3)
        .mapNotNull { chunk ->
            val reallyBig = 10_000_000_000_000
            parseEquationPair(chunk).let { (eqX, eqY) ->
                eqX.copy(result = reallyBig + eqX.result) to eqY.copy(result = reallyBig + eqY.result)
            }
                .solve()
        }
        .sumOf { it.first * 3 + it.second }

fun main() {
    val testInput =
        """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
        """.trimIndent().lines()
    require(part1(testInput) == 480L)

    val realInput = inputOfDay(13)
    println(part1(realInput))
    println(part2(realInput))
}