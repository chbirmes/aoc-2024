private data class Input(val rules: List<Pair<Int, Int>>, val updates: List<List<Int>>) {

    fun correctlyOrderedUpdates() =
        updates.filter { update ->
            rulesFor(update).all { rule -> update.indexOf(rule.first) < update.indexOf(rule.second) }
        }

    fun incorrectlyOrderedUpdates() =
        updates.filterNot { update ->
            rulesFor(update).all { rule -> update.indexOf(rule.first) < update.indexOf(rule.second) }
        }

    private fun rulesFor(update: List<Int>) =
        rules.filter { rule -> update.contains(rule.first) && update.contains(rule.second) }

    fun comparatorFor(update: List<Int>) =
        rulesFor(update).let { relevantRules ->
            Comparator<Int> { a, b -> if (isInOrder(a, b, relevantRules)) -1 else 1 }
        }

    companion object {
        fun parse(lines: List<String>) =
            Input(
                lines.takeWhile { it.isNotEmpty() }
                    .map { it.split('|') }
                    .map { it[0].toInt() to it[1].toInt() },
                lines.takeLastWhile { it.isNotEmpty() }
                    .map { it.split(',') }
                    .map { parts -> parts.map { it.toInt() } }
            )

        private fun isInOrder(a: Int, b: Int, rules: List<Pair<Int, Int>>): Boolean =
            if (rules.contains(a to b))
                true
            else if (rules.contains(b to a))
                false
            else {
                rules.filter { it.first == a }
                    .any { isInOrder(it.second, b, rules) }
            }
    }
}

private fun part1(lines: List<String>) =
    Input.parse(lines)
        .correctlyOrderedUpdates()
        .sumOf { update -> update[update.indices.last / 2] }

private fun part2(lines: List<String>) =
    Input.parse(lines).let { input ->
        input.incorrectlyOrderedUpdates()
            .map { it.sortedWith(input.comparatorFor(it)) }
            .sumOf { update -> update[update.indices.last / 2] }
    }

fun main() {
    val testInput =
        """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13

            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
        """.trimIndent().lines()
    require(part1(testInput) == 143)
    require(part2(testInput) == 123)

    val realInput = inputOfDay(5)
    println(part1(realInput))
    println(part2(realInput))
}