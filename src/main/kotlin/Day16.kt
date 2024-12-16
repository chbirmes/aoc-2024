private fun neighborsFunction(grid: D2.Grid): (Pair<D2.Position, D2.Position>) -> List<Pair<D2.Position, D2.Position>> =
    { (position, direction) ->
        val list = listOf(
            position to direction.turnRight(),
            position to direction.turnLeft(),
        )
        val ahead = position + direction
        if (ahead in grid && grid[ahead] != '#') list + (ahead to direction)
        else list
    }

private fun performTracingAStar(lines: List<String>): Pair<Int, List<Pair<D2.Position, D2.Position>>> {
    val grid = D2.Grid(lines)
    val finish = grid.find('E')!!
    return tracingAStar(
        start = grid.find('S')!! to D2.right,
        neighborsFunction = neighborsFunction(grid),
        costFunction = { current, next -> if (current.first == next.first) 1000 else 1 },
        stopPredicate = { it.first == finish }
    )
}

private fun part1(lines: List<String>) = performTracingAStar(lines).first

private fun part2(lines: List<String>) = performTracingAStar(lines).second.map { it.first }.toSet().size + 1

fun main() {
    val testInput =
        """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
        """.trimIndent().lines()
    require(part1(testInput) == 11048)
    require(part2(testInput) == 64)

    val realInput = inputOfDay(16)
    println(part1(realInput))
    println(part2(realInput))
}