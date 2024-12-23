private fun part1(lines: List<String>): Int {
    val connections = lines.map { it.split('-').toSet() }.toSet()
    val nodes = connections.flatten().toSet()
    val triples = mutableSetOf<Set<Set<String>>>()
    connections.forEach { connection ->
        val otherNodes = nodes - connection
        otherNodes.forEach { node ->
            val partner1 = setOf(connection.elementAt(0), node)
            val partner2 = setOf(connection.elementAt(1), node)
            if (partner1 in connections && partner2 in connections) {
                triples.add(setOf(connection, partner1, partner2))
            }
        }
    }
    triples.removeIf { triple ->
        triple.none { connection ->
            connection.any { it.startsWith('t') }
        }
    }
    return triples.size
}

private tailrec fun cluster(members: Set<String>, connections: Set<Set<String>>, other: Set<String>): Set<String> {
    val next = other.firstOrNull { candidate -> members.all { setOf(candidate, it) in connections } }
    return if (next == null) members
    else cluster(members + next, connections, other - next)
}

private fun part2(lines: List<String>): String {
    val connections = lines.map { it.split('-').toSet() }.toSet()
    val nodes = connections.flatten().toSet()
    return nodes.asSequence().map { cluster(setOf(it), connections, nodes - it) }
        .maxBy { it.size }
        .sorted()
        .joinToString(",")
}

fun main() {
    val testInput =
        """
            kh-tc
            qp-kh
            de-cg
            ka-co
            yn-aq
            qp-ub
            cg-tb
            vc-aq
            tb-ka
            wh-tc
            yn-cg
            kh-ub
            ta-co
            de-co
            tc-td
            tb-wq
            wh-td
            ta-ka
            td-qp
            aq-cg
            wq-ub
            ub-vc
            de-ta
            wq-aq
            wq-vc
            wh-yn
            ka-de
            kh-ta
            co-tc
            wh-qp
            tb-vc
            td-yn
        """.trimIndent().lines()
    require(part1(testInput) == 7)
    require(part2(testInput) == "co,de,ka,ta")

    val realInput = inputOfDay(23)
    println(part1(realInput))
    println(part2(realInput))
}