private fun nextSecretNumber(secretNumber: Long): Long {
    val step1 = ((secretNumber shl 6) xor secretNumber) % 16777216L
    val step2 = ((step1 shr 5) xor step1) % 16777216L
    return ((step2 shl 11) xor step2) % 16777216L
}

private fun daySecrets(initial: Long) =
    (1..2000).asSequence().runningFold(initial) { acc, _ -> nextSecretNumber(acc) }

private fun part1(lines: List<String>): Long {
    return lines.map { it.toLong() }
        .sumOf { secretNumber ->
            (1..2000).fold(secretNumber) { acc, _ -> nextSecretNumber(acc) }
        }
}

private fun part2(lines: List<String>): Long {
    val maps = lines.map { line ->
        val resultMap = mutableMapOf<List<Long>, Long>()
        val daySecrets = daySecrets(line.toLong())
        val dayPrices = daySecrets.map { it % 10 }
        val changes = dayPrices.windowed(2).map { it[1] - it[0] }
        val dayPricesList = dayPrices.toList()
        changes.windowed(4).withIndex().forEach { indexedWindow ->
            val priceIndex = indexedWindow.index + 4
            resultMap.putIfAbsent(indexedWindow.value, dayPricesList[priceIndex])
        }
        resultMap
    }
    val completeMap = mutableMapOf<List<Long>, Long>()
    maps.forEach { map ->
        map.forEach { (k, v) -> completeMap[k] = (completeMap[k] ?: 0) + v }
    }
    return completeMap.values.max()
}

fun main() {
    val testInput =
        """
            1
            10
            100
            2024
        """.trimIndent().lines()
    require(part1(testInput) == 37327623L)

    val testInput2 = """
            1
            2
            3
            2024
    """.trimIndent().lines()
    require(part2(testInput2).also { println(it) } == 23L)

    val realInput = inputOfDay(22)
    println(part1(realInput))
    println(part2(realInput))
}