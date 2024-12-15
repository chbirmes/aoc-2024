private fun parseDirection(char: Char): D2.Position {
    val direction = when (char) {
        '^' -> D2.up
        '<' -> D2.left
        '>' -> D2.right
        else -> D2.down
    }
    return direction
}

private data class Warehouse(
    val robot: D2.Position, val boxes: Set<D2.Position>, val walls: Set<D2.Position>
) {
    companion object {
        fun parse(lines: List<String>) = D2.Grid(lines).let {
            Warehouse(it.find('@')!!, it.filter('O').toSet(), it.filter('#').toSet())
        }
    }

    fun move(char: Char): Warehouse {
        val direction = parseDirection(char)
        val (canMove, movingBoxes) = calculateMovements(direction)
        return if (!canMove) this
        else Warehouse(
            robot + direction,
            (boxes - movingBoxes.toSet()) + movingBoxes.map { it + direction },
            walls
        )
    }

    private tailrec fun calculateMovements(
        direction: D2.Position, movingBoxes: List<D2.Position> = emptyList()
    ): Pair<Boolean, List<D2.Position>> {
        val nextPosition = (movingBoxes.lastOrNull() ?: robot) + direction
        return when (nextPosition) {
            in walls -> false to emptyList()
            in boxes -> calculateMovements(direction, movingBoxes + nextPosition)
            else -> true to movingBoxes
        }
    }

}

private data class WideBox(val left: D2.Position) {
    fun positions() = setOf(left, left + D2.right)
    operator fun plus(direction: D2.Position) = WideBox(left + direction)
}

private data class Warehouse2(
    val robot: D2.Position, val boxes: Set<WideBox>, val walls: Set<D2.Position>
) {
    companion object {
        fun parse(lines: List<String>): Warehouse2 {
            val wideLines = lines.map {
                it.replace("#", "##")
                    .replace("O", "[]")
                    .replace(".", "..")
                    .replace("@", "@.")
            }
            return D2.Grid(wideLines).let {
                Warehouse2(it.find('@')!!, it.filter('[').map(::WideBox).toSet(), it.filter('#').toSet())
            }
        }
    }

    fun move(char: Char): Warehouse2 {
        val direction = parseDirection(char)
        val (canMove, movingBoxes) = calculateMovements(direction)
        return if (!canMove) this
        else Warehouse2(
            robot + direction,
            (boxes - movingBoxes.toSet()) + movingBoxes.map { it + direction },
            walls
        )
    }

    private tailrec fun calculateMovements(
        direction: D2.Position,
        movingBoxes: Set<WideBox> = emptySet()
    ): Pair<Boolean, Set<WideBox>> {
        val alreadyOccupied = movingBoxes.flatMap { it.positions() }.toSet()
        val next = ((alreadyOccupied + robot).map { it + direction }) - alreadyOccupied
        if (next.any { it in walls }) return false to emptySet()
        else {
            val newBoxes = boxes.filter { box -> box.positions().any { it in next } }
            return if (newBoxes.isEmpty()) true to movingBoxes
            else calculateMovements(direction, movingBoxes + newBoxes)
        }
    }

}


private fun part1(lines: List<String>): Int {
    val initialWarehouse = Warehouse.parse(lines.takeWhile { it.isNotEmpty() })
    val moves = lines.takeLastWhile { it.isNotEmpty() }.joinToString(separator = "")
    val finalWarehouse = moves.fold(initialWarehouse, Warehouse::move)
    return finalWarehouse.boxes.sumOf { it.y * 100 + it.x }
}

private fun part2(lines: List<String>): Int {
    val initialWarehouse = Warehouse2.parse(lines.takeWhile { it.isNotEmpty() })
    val moves = lines.takeLastWhile { it.isNotEmpty() }.joinToString(separator = "")
    val finalWarehouse = moves.fold(initialWarehouse, Warehouse2::move)
    return finalWarehouse.boxes.sumOf { it.left.y * 100 + it.left.x }
}

fun main() {
    val testInput = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent().lines()
    require(part1(testInput) == 10092)
    require(part2(testInput) == 9021)

    val realInput = inputOfDay(15)
    println(part1(realInput))
    println(part2(realInput))
}