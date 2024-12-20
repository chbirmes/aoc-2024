import D2.neighborsIn
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

private typealias Cheat = Pair<D2.Position, D2.Position>

private fun pathThrough(grid: D2.Grid): List<D2.Position> {
    val start = grid.find('S')!!
    val end = grid.find('E')!!
    val set = linkedSetOf(start)
    var current = start
    while (current != end) {
        val next = current.neighborsIn(grid)
            .filterNot { grid[it] == '#' }
            .filterNot { it in set }
            .first()
        set.add(next)
        current = next
    }
    return set.toList()
}

private fun manhattanDistance(p1: D2.Position, p2: D2.Position) =
    (p1 - p2).let { it.x.absoluteValue + it.y.absoluteValue }

private fun IntRange.constrainTo(other: IntRange) = max(first, other.first)..min(last, other.last)

private fun filterEndIndexCandidates(
    interval: IntRange,
    start: D2.Position,
    path: List<D2.Position>,
    maxDistance: Int,
    result: MutableList<IntRange> = mutableListOf()
): MutableList<IntRange> {
    if (interval.isEmpty()) return result
    val pivot = (interval.first + interval.last) / 2
    val buffer = maxDistance - manhattanDistance(start, path[pivot])

    val (leftMax, rightMin) =
        if (buffer >= 0)
            (pivot - buffer - 1) to (pivot + buffer + 1)
        else
            (pivot + buffer) to (pivot - buffer)

    if (buffer >= 0) {
        result.add(((leftMax + 1)..<rightMin).constrainTo(interval))
    }
    val left = filterEndIndexCandidates(interval.first..leftMax, start, path, maxDistance, result)
    val right = filterEndIndexCandidates(rightMin..interval.last, start, path, maxDistance)
    left.addAll(right)
    return left
}

private fun findCheats(path: List<D2.Position>, minSaving: Int, cheatLength: Int): Map<Cheat, Int> {
    val result = mutableMapOf<Cheat, Int>()
    val startIndexCandidates = 0..(path.indices.last - 2 - minSaving)
    startIndexCandidates.forEach { startIndex ->
        val startCandidate = path[startIndex]
        val endIndexCandidates = (startIndex + 2 + minSaving)..path.indices.last
        filterEndIndexCandidates(endIndexCandidates, startCandidate, path, cheatLength)
            .asSequence()
            .flatten()
            .forEach { endIndex ->
                val endCandidate = path[endIndex]
                val saving = endIndex - startIndex - manhattanDistance(startCandidate, endCandidate)
                val cheat = startCandidate to endCandidate
                saving.takeIf { it >= minSaving }?.let { result[cheat] = it }
            }
    }
    return result
}

private fun solve(lines: List<String>, cheatLength: Int, minSaving: Int = 2): Map<Int, Int> {
    val grid = D2.Grid(lines)
    val path = pathThrough(grid)
    val cheats = findCheats(path, minSaving, cheatLength)
    return cheats.asIterable()
        .groupBy { it.value }
        .map { it.key to it.value.size }
        .toMap()
}

private fun part1(lines: List<String>, minSaving: Int = 2) = solve(lines, 2, minSaving)

private fun part2(lines: List<String>, minSaving: Int = 50) = solve(lines, 20, minSaving)

fun main() {
    val testInput =
        """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
        """.trimIndent().lines()

    val expected = mapOf(
        2 to 14,
        4 to 14,
        6 to 2,
        8 to 4,
        10 to 2,
        12 to 3,
        20 to 1,
        36 to 1,
        38 to 1,
        40 to 1,
        64 to 1
    )
    require(part1(testInput) == expected)

    val expected2 = mapOf(
        50 to 32,
        52 to 31,
        54 to 29,
        56 to 39,
        58 to 25,
        60 to 23,
        62 to 20,
        64 to 19,
        66 to 12,
        68 to 14,
        70 to 12,
        72 to 22,
        74 to 4,
        76 to 3
    )
    require(part2(testInput) == expected2)

    val realInput = inputOfDay(20)
    println(part1(realInput, 100).values.sum())
    println(part2(realInput, 100).values.sum())
}