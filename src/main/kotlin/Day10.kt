import D2.nonDiagonalNeighborsIn

private fun score(current: D2.Position, grid: D2.Grid): Set<D2.Position> {
    val currentLevel = grid.charAt(current).digitToInt()
    if (currentLevel == 9) return setOf(current)
    val nextChar = (currentLevel+1).toString().first()
    val nextPos = current.nonDiagonalNeighborsIn(grid).filter { grid.charAt(it) == nextChar }
    if (nextPos.isEmpty()) return emptySet()
    return nextPos.flatMap { score(it, grid) }.toSet()
}

private fun part1(lines: List<String>) :Int {
    val grid = D2.Grid(lines)
    val trailHeads = grid.positions().filter { grid.charAt(it) == '0' }
    return trailHeads.sumOf { score(it, grid).size }
}

private fun rating(current: D2.Position, grid: D2.Grid): Int {
    val currentLevel = grid.charAt(current).digitToInt()
    if (currentLevel == 9) return 1
    val nextChar = (currentLevel+1).toString().first()
    val nextPos = current.nonDiagonalNeighborsIn(grid).filter { grid.charAt(it) == nextChar }
    if (nextPos.isEmpty()) return 0
    return nextPos.sumOf { rating(it, grid) }
}

private fun part2(lines: List<String>) :Int {
    val grid = D2.Grid(lines)
    val trailHeads = grid.positions().filter { grid.charAt(it) == '0' }
    return trailHeads.sumOf { rating(it, grid) }
}

fun main() {
    val testInput =
        """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
        """.trimIndent().lines()
    require(part1(testInput) == 36)
    require(part2(testInput) == 81)

    val realInput = inputOfDay(10)
    println(part1(realInput))
    println(part2(realInput))
}