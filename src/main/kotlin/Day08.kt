private fun antennaGroups(grid: D2.Grid) = grid.positions()
    .filterNot { grid.charAt(it) == '.' }
    .groupBy { grid.charAt(it) }.values

private fun <T> List<T>.pairs() =
    (0..<lastIndex).flatMap { i ->
        ((i + 1)..lastIndex).map { j -> this[i] to this[j] }
    }

private fun antinodes(antennaPair: Pair<D2.Position, D2.Position>): List<D2.Position> {
    val d = antennaPair.first - antennaPair.second
    return listOf(antennaPair.first + d, antennaPair.second - d)
}

private fun part1(lines: List<String>): Int {
    val grid = D2.Grid(lines)
    return antennaGroups(grid).flatMap { group ->
        group.pairs().flatMap { pair -> antinodes(pair) }
    }
        .filter { it in grid }
        .toSet().size
}

private fun antinodes2(antennaPair: Pair<D2.Position, D2.Position>, grid: D2.Grid): List<D2.Position> {
    val d = antennaPair.first - antennaPair.second
    return walk(antennaPair.first, d, grid) + walk(antennaPair.second, -d, grid)
}

private tailrec fun walk(
    position: D2.Position,
    step: D2.Position,
    grid: D2.Grid,
    collected: MutableList<D2.Position> = mutableListOf(position)
): List<D2.Position> {
    val next = position + step
    if (next !in grid) return collected
    collected.add(next)
    return walk(next, step, grid, collected)
}

private fun part2(lines: List<String>): Int {
    val grid = D2.Grid(lines)
    return antennaGroups(grid).flatMap { group ->
        group.pairs().flatMap { pair -> antinodes2(pair, grid) }
    }
        .toSet().size
}

fun main() {
    val testInput =
        """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
        """.trimIndent().lines()
    require(part1(testInput) == 14)
    require(part2(testInput) == 34)

    val realInput = inputOfDay(8)
    println(part1(realInput))
    println(part2(realInput))
}