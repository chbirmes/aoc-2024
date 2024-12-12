private fun part1(input: String): Long {
    val blocks = mutableListOf<Int?>()
    input.forEachIndexed { index, c ->
        val id = if (index % 2 == 0) index / 2 else null
        blocks.addAll(List(c.digitToInt()) { id })
    }

    while (blocks.contains(null)) {
        val last = blocks.last()
        if (last != null) {
            val emptyIndex = blocks.indexOfFirst { it == null }
            blocks[emptyIndex] = last
        }
        blocks.removeAt(blocks.lastIndex)
    }

    return blocks.foldIndexed(0L) { index, acc, id -> acc + index * id!! }
}

private sealed interface ContiguousBlocks {
    val size: Int

    data class Free(override val size: Int) : ContiguousBlocks
    data class File(override val size: Int, val id: Int) : ContiguousBlocks
}

private fun part2(input: String): Long {
    val fileSystem = mutableListOf<ContiguousBlocks>()
    input.forEachIndexed { index, c ->
        val id = if (index % 2 == 0) index / 2 else null
        val size = c.digitToInt()
        val blocks = if (id == null) ContiguousBlocks.Free(size) else ContiguousBlocks.File(size, id)
        fileSystem.add(blocks)
    }

    val reversedFiles = fileSystem.filterIsInstance<ContiguousBlocks.File>().reversed()

    reversedFiles.forEach { file ->
        val fileIndex = fileSystem.indexOf(file)
        val freeIndex = fileSystem.indexOfFirst { it is ContiguousBlocks.Free && it.size >= file.size }
        if (freeIndex != -1 && freeIndex < fileIndex) {
            val free = fileSystem[freeIndex]
            fileSystem[freeIndex] = file
            fileSystem[fileIndex] = ContiguousBlocks.Free(file.size)
            if (file.size < free.size) {
                fileSystem.add(freeIndex + 1, ContiguousBlocks.Free(free.size - file.size))
            }
        }
    }

    val blocks = fileSystem.flatMap { blocks ->
        when (blocks) {
            is ContiguousBlocks.Free -> List(blocks.size) { null }
            is ContiguousBlocks.File -> List(blocks.size) { blocks.id }
        }
    }
    return blocks.foldIndexed(0L) { index, acc, id -> acc + (index * (id ?: 0)) }

}

fun main() {
    val testInput = "2333133121414131402"
    require(part1(testInput) == 1928L)
    require(part2(testInput) == 2858L)

    val realInput = inputOfDay(9)[0]
    println(part1(realInput))
    println(part2(realInput))
}