private fun combinations(pattern: String, towels: Set<String>, cache: MutableMap<String, Long> = mutableMapOf()): Long =
    if (pattern.isEmpty()) 1L
    else cache.getOrPut(pattern) {
        towels.sumOf { towel ->
            if (pattern.startsWith(towel)) combinations(pattern.substring(towel.length), towels, cache) else 0L
        }
    }

private fun part1(lines: List<String>): Int {
    val towels = lines.first().split(", ").toSet()
    val patterns = lines.drop(2)
    return patterns.count { combinations(it, towels) > 0 }
}

private fun part2(lines: List<String>): Long {
    val towels = lines.first().split(", ").toSet()
    val patterns = lines.drop(2)
    return patterns.sumOf { combinations(it, towels) }
}

fun main() {
    val testInput =
        """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """.trimIndent().lines()
    require(part1(testInput) == 6)
    require(part2(testInput) == 16L)

    val realInput = inputOfDay(19)
    println(part1(realInput))
    println(part2(realInput))
}