private class StoneNode {
    val children: MutableList<StoneNode> = mutableListOf()
    val depthCache: MutableMap<Int, Long> = mutableMapOf()

    fun countChildren(depth: Int): Long {
        if (depthCache.containsKey(depth)) return depthCache[depth]!!
        return if (depth == 1) children.size.toLong()
        else children.sumOf { it.countChildren(depth - 1) }
            .also { depthCache[depth] = it }
    }

}

private fun singleBlink(number: Long): List<Long> {
    if (number == 0L) return listOf(1L)
    else {
        val numberString = number.toString()
        return if (numberString.length % 2 == 0) listOf(
            numberString.substring(0, numberString.length / 2).toLong(),
            numberString.substring(numberString.length / 2).toLong()
        )
        else listOf(number * 2024)
    }
}

private fun multiBlink(input: String, times: Int): Long {
    val initialStones = input.split(" ").map { it.toLong() }
    val nodeMap = initialStones.associateWith { StoneNode() }.toMutableMap()
    var currentStones = initialStones

    repeat(times) {
        val nextStones = mutableListOf<Long>()
        currentStones.forEach { stone ->
            val newStones = singleBlink(stone)
            newStones.forEach { newStone ->
                if (nodeMap.containsKey(newStone)) {
                    nodeMap[stone]!!.children.add(nodeMap[newStone]!!)
                } else {
                    val newNode = StoneNode()
                    nodeMap[newStone] = newNode
                    nodeMap[stone]!!.children.add(newNode)
                    nextStones.add(newStone)
                }
            }
        }
        currentStones = nextStones
    }

    return initialStones.map { nodeMap[it]!! }.sumOf { it.countChildren(times) }
}

private fun part1(input: String) = multiBlink(input, 25)

private fun part2(input: String): Long = multiBlink(input, 75)

fun main() {
    val testInput = "125 17"
    require(part1(testInput) == 55312L)

    val realInput = inputOfDay(11)[0]
    println(part1(realInput))
    println(part2(realInput))
}