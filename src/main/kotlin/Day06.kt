private data class Guard(val position: D2.Position, val direction: D2.Position)

private data class WalkResult(val guardStates: Set<Guard>, val loop: Boolean) {
    fun visitedPositions() = guardStates.map { it.position }.toSet()
}

private tailrec fun walk(
    guard: Guard,
    grid: D2.Grid,
    obstacles: Set<D2.Position>,
    guardStates: MutableSet<Guard> = mutableSetOf(guard)
): WalkResult {
    val positionAhead = guard.position + guard.direction
    return if (positionAhead !in grid)
        WalkResult(guardStates, false)
    else {
        val newGuard =
            if (positionAhead in obstacles)
                guard.copy(direction = guard.direction.turnRight())
            else
                guard.copy(position = positionAhead)
        if (!guardStates.add(newGuard))
            WalkResult(guardStates, true)
        else
            walk(newGuard, grid, obstacles, guardStates)
    }
}

private fun part1(lines: List<String>) =
    D2.Grid(lines).let { grid ->
        val guard = Guard(
            grid.positions().find { grid.charAt(it) == '^' }!!,
            D2.Position(0, -1)
        )
        val obstacles = grid.positions().filter { grid.charAt(it) == '#' }.toSet()
        walk(guard, grid, obstacles).visitedPositions().size
    }

private fun part2(lines: List<String>) =
    D2.Grid(lines).let { grid ->
        val guard = Guard(
            grid.positions().find { grid.charAt(it) == '^' }!!,
            D2.Position(0, -1)
        )
        val obstacles = grid.positions().filter { grid.charAt(it) == '#' }.toSet()
        val candidates = walk(guard, grid, obstacles).visitedPositions() - guard.position
        candidates.count { candidate ->
            walk(guard, grid, obstacles + candidate).loop
        }
    }

fun main() {
    val testInput =
        """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
        """.trimIndent().lines()
    require(part1(testInput) == 41)
    require(part2(testInput) == 6)

    val realInput = inputOfDay(6)
    println(part1(realInput))
    println(part2(realInput))
}