import java.util.*

fun <T> tracingAStar(
    start: T,
    neighborsFunction: (T) -> List<T>,
    costFunction: (T, T) -> Int,
    stopPredicate: (T) -> Boolean,
    priorityComparator: Comparator<in Pair<T, Int>> = compareBy { it.second }
): Pair<Int, MutableList<T>> {
    val priorityQueue = PriorityQueue(priorityComparator)
    return recursiveSearch(start, neighborsFunction, costFunction, stopPredicate, priorityQueue)
}

private tailrec fun <T> recursiveSearch(
    node: T,
    neighborsFunction: (T) -> List<T>,
    costFunction: (T, T) -> Int,
    stopPredicate: (T) -> Boolean,
    priorityQueue: PriorityQueue<Pair<T, Int>>,
    lowestCosts: MutableMap<T, Pair<Int, MutableList<T>>> = mutableMapOf(),
): Pair<Int, MutableList<T>> {
    if (stopPredicate(node)) {
        return lowestCosts[node]!!
    }
    visitNode(node, neighborsFunction, costFunction, priorityQueue, lowestCosts)
    val nextNode = priorityQueue.poll().first
    return recursiveSearch(nextNode, neighborsFunction, costFunction, stopPredicate, priorityQueue, lowestCosts)
}

private fun <T> visitNode(
    node: T,
    neighborsFunction: (T) -> List<T>,
    costFunction: (T, T) -> Int,
    priorityQueue: PriorityQueue<Pair<T, Int>>,
    lowestCosts: MutableMap<T, Pair<Int, MutableList<T>>>
) {
    val (costToCurrent, pathToCurrent) = lowestCosts.getOrDefault(node, 0 to mutableListOf())
    neighborsFunction(node).forEach { neighbor ->
        val newCost = costFunction(node, neighbor) + costToCurrent
        val nextCost = lowestCosts.getOrDefault(neighbor, Integer.MAX_VALUE to mutableListOf()).first
        if (newCost == nextCost) {
            lowestCosts[neighbor]?.let {
                it.second.addAll(pathToCurrent)
                it.second.add(node)
            }
        }
        if (newCost < nextCost) {
            val pathToNeighbor = mutableListOf(node)
            pathToNeighbor.addAll(pathToCurrent)
            lowestCosts[neighbor] = newCost to pathToNeighbor
            priorityQueue.add(neighbor to newCost)
        }
    }
}
