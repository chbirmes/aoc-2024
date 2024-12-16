object D2 {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(other: Position) = Position(x + other.x, y + other.y)
        operator fun times(scalar: Int) = Position(x * scalar, y * scalar)
        operator fun minus(other: Position) = plus(other * -1)
        operator fun unaryMinus(): Position = Position(-x, -y)

        fun turnLeft() = Position(y, -x)
        fun turnRight() = Position(-y, x)
    }

    data class Grid(val lines: List<String>) {
        val width: Int get() = lines[0].length
        val maxX: Int get() = width - 1
        val height: Int get() = lines.size
        val maxY: Int get() = height - 1

        operator fun get(position: Position) = lines[position.y][position.x]

        operator fun contains(position: Position) =
            position.x in lines[0].indices && position.y in lines.indices

        fun positions() =
            lines[0].indices.flatMap { x ->
                lines.indices.map { y -> Position(x, y) }
            }

        fun find(char: Char) = positions().find { get(it) == char }

        fun filter(char: Char) = positions().filter { get(it) == char }

    }

    val left = Position(-1, 0)
    val right = Position(1, 0)
    val down = Position(0, 1)
    val up = Position(0, -1)

    private val nonDiagonalDirections = listOf(up, right, down, left)

    val upLeft = Position(-1, -1)
    val downLeft = Position(-1, 1)
    val downRight = Position(1, -1)
    val upRight = Position(1, 1)

    private val diagonalDirections = listOf(
        upLeft,
        downLeft,
        downRight,
        upRight
    )
    private val allDirections = nonDiagonalDirections + diagonalDirections

    fun directions(includeDiagonal: Boolean = false) = if (includeDiagonal) allDirections else nonDiagonalDirections

    fun Position.neighbors(includeDiagonal: Boolean = false) = directions(includeDiagonal).map { this + it }

    fun Position.neighborsIn(grid: Grid, includeDiagonal: Boolean = false) =
        neighbors(includeDiagonal).filter { it in grid }

}