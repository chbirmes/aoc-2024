object D2 {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(other: Position) = Position(x + other.x, y + other.y)
        operator fun times(scalar: Int) = Position(x * scalar, y * scalar)
        operator fun minus(other: Position) = plus(other * -1)
        operator fun unaryMinus(): Position = Position(-x, -y)

        fun turnRight() = Position(-y, x)
    }

    data class Grid(val lines: List<String>) {
        val width: Int get() = lines[0].length
        val maxX: Int get() = width - 1
        val height: Int get() = lines.size
        val maxY: Int get() = height - 1

        fun charAt(position: Position) = lines[position.y][position.x]

        operator fun contains(position: Position) =
            position.x in lines[0].indices && position.y in lines.indices

        fun positions() =
            lines[0].indices.flatMap { x ->
                lines.indices.map { y -> Position(x, y) }
            }

    }

    val directions = listOf(
        Position(-1, -1),
        Position(-1, 0),
        Position(-1, 1),
        Position(0, 1),
        Position(0, -1),
        Position(1, -1),
        Position(1, 0),
        Position(1, 1)
    )

    fun Position.neighborsIn(grid: Grid): List<Position> {
        return directions
            .map { this + it }
            .filter { it in grid }
    }

}