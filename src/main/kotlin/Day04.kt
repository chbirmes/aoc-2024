private fun part1(lines: List<String>) =
    D2.Grid(lines).let { grid ->
        grid.positions().flatMap { start ->
            D2.directions(includeDiagonal = true).map { direction -> start to direction }
        }
            .count { (start, direction) ->
                "XMAS".withIndex().all { indexedChar ->
                    val pos = start + (direction * indexedChar.index)
                    pos in grid && grid[pos] == indexedChar.value
                }
            }
    }

private fun part2(lines: List<String>) =
    D2.Grid(lines).let { grid ->
        grid.positions()
            .filter { (x, y) -> x != 0 && x != grid.maxX && y != 0 && y != grid.maxY }
            .filter { grid[it] == 'A' }
            .count { position ->
                val upperLeft = grid[position + D2.upLeft]
                val upperRight = grid[position + D2.upRight]
                val lowerLeft = grid[position + D2.downLeft]
                val lowerRight = grid[position + D2.downRight]
                ((upperLeft == 'M' && lowerRight == 'S') || (upperLeft == 'S' && lowerRight == 'M')) &&
                        ((lowerLeft == 'M' && upperRight == 'S') || (lowerLeft == 'S' && upperRight == 'M'))
            }
    }

fun main() {
    val testInput =
        """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent().lines()
    require(part1(testInput) == 18)
    require(part2(testInput) == 9)

    val realInput = inputOfDay(4)
    println(part1(realInput))
    println(part2(realInput))
}