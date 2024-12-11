import kotlin.math.absoluteValue
import kotlin.math.sign

private val mulRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

private fun part1(line: String) =
    mulRegex.findAll(line).sumOf { matchResult ->
        matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
    }

private fun part2(line: String) =
    (Regex("do\\(\\)").findAll(line).map { it.range.first }.associateWith { true } + Regex("don't\\(\\)").findAll(line)
        .map { it.range.first }.associateWith { false }).toSortedMap()
        .let { switches ->
            mulRegex.findAll(line).sumOf { matchResult ->
                val headMap = switches.headMap(matchResult.range.first)
                if (headMap.isEmpty() || headMap.values.last())
                    matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
                else
                    0
            }
        }

fun main() {
    val testInput = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    val testInput2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
    require(part1(testInput) == 161)
    require(part2(testInput2) == 48)

    val realInput = inputOfDay(3).joinToString()
    println(part1(realInput))
    println(part2(realInput))
}