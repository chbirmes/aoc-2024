import D2.neighbors

private data class GardenRegion(val plots: Set<D2.Position>, val perimeter: Set<Pair<D2.Position, D2.Position>>) {
    operator fun plus(other: GardenRegion) = GardenRegion(plots + other.plots, perimeter + other.perimeter)
}

private fun createRegion(
    start: D2.Position,
    grid: D2.Grid,
    freePositions: MutableSet<D2.Position>,
    plots: MutableSet<D2.Position> = mutableSetOf(start),
    perimeter: MutableSet<Pair<D2.Position, D2.Position>> = mutableSetOf()
): GardenRegion {
    val neighbors = start.neighbors()
    val currentChar = grid[start]
    val newPlots = neighbors.filter { it in grid && grid[it] == currentChar && it !in plots }.toSet()
    val newPerimeters = neighbors.filter { it !in grid || grid[it] != currentChar }.map { start to it }
    perimeter.addAll(newPerimeters)
    if (newPlots.isEmpty()) {
        return GardenRegion(plots, perimeter)
    } else {
        plots.addAll(newPlots)
        freePositions.removeAll(newPlots)
        return newPlots.map { createRegion(it, grid, freePositions, plots, perimeter) }
            .fold(GardenRegion(emptySet(), emptySet()), GardenRegion::plus)
    }
}

private fun createRegions(lines: List<String>): MutableList<GardenRegion> {
    val grid = D2.Grid(lines)
    val freePositions = grid.positions().toMutableSet()
    val regions = mutableListOf<GardenRegion>()
    while (freePositions.isNotEmpty()) {
        val start = freePositions.first()
        freePositions.remove(start)
        regions.add(createRegion(start, grid, freePositions))
    }
    return regions
}

private fun part1(lines: List<String>): Int {
    val regions = createRegions(lines)
    return regions.sumOf { it.plots.size * it.perimeter.size }
}

private operator fun Pair<D2.Position, D2.Position>.plus(other: D2.Position) = (first + other) to (second + other)

private fun countSides(perimeter: Set<Pair<D2.Position, D2.Position>>): Int {
    val free = perimeter.toMutableSet()
    var count = 0
    while (free.isNotEmpty()) {
        val start = free.first()
        free.remove(start)
        val diff = start.first - start.second
        val vertical = diff.y == 0

        var direction = if (vertical) D2.up else D2.left
        var next = start + direction
        while (next in free) {
            free.remove(next)
            next += direction
        }

        direction = if (vertical) D2.down else D2.right
        next = start + direction
        while (next in free) {
            free.remove(next)
            next += direction
        }
        count++
    }
    return count
}

private fun part2(lines: List<String>): Int {
    val regions = createRegions(lines)
    return regions.sumOf { it.plots.size * countSides(it.perimeter) }
}

fun main() {
    val testInput =
        """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
        """.trimIndent().lines()
    require(part1(testInput) == 1930)
    require(part2(testInput) == 1206)

    val realInput = inputOfDay(12)
    println(part1(realInput))
    println(part2(realInput))
}
