private fun doAStar(areaSize: Int, corrupted: Set<D2.Position>): Int {
    val start = D2.Position(0, 0)
    val goal = D2.Position(areaSize - 1, areaSize - 1)
    return aStar(
        start = start,
        neighborsFunction = { current ->
            D2.directions().map { current + it }
                .filter { it.x in 0..<areaSize && it.y in 0..<areaSize }
                .filter { it !in corrupted }
        },
        costFunction = { _, _ -> 1 },
        stopPredicate = { it == goal }
    )
}

private fun parsePosition(s: String) = D2.Position(s.substringBefore(',').toInt(), s.substringAfter(',').toInt())

private fun part1(areaSize: Int, lines: List<String>): Int {
    val corrupted = lines.map { parsePosition(it) }.toSet()
    return doAStar(areaSize, corrupted)
}

private fun part2(areaSize: Int, lines: List<String>, skipped: Int): D2.Position {
    val corruptedList = lines.map { parsePosition(it) }
    val corruptedListSequence = ((skipped + 1)..corruptedList.size).asSequence()
        .map { corruptedList.take(it) }

    return corruptedListSequence.first { doAStar(areaSize, it.toSet()) == Int.MAX_VALUE }.last()
}

fun main() {
    val testInput =
        """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
        """.trimIndent().lines()
    require(part1(7, testInput.take(12)) == 22)
    require(part2(7, testInput, 12) == D2.Position(6, 1))

    val realInput = inputOfDay(18)
    println(part1(71, realInput.take(1024)))
    println(part2(71, realInput, 1024))
}