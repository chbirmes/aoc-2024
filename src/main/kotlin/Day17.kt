private data class Computer(var a: Long, var b: Long, var c: Long) {
    private var instructionPointer = 0

    fun runProgram(program: List<Int>): List<Int> {
        val outputs = mutableListOf<Int>()
        while (instructionPointer in program.indices) {
            val operand = program[instructionPointer + 1]
            var nonJump = true
            when (program[instructionPointer]) {
                0 -> a /= (1L shl resolveComboOperand(operand).toInt())
                1 -> b = b xor operand.toLong()
                2 -> b = resolveComboOperand(operand).mod(8L)
                3 -> if (a != 0L) {
                    instructionPointer = operand
                    nonJump = false
                }

                4 -> b = b xor c
                5 -> outputs += resolveComboOperand(operand).mod(8)
                6 -> b = a / (1L shl resolveComboOperand(operand).toInt())
                7 -> c = a / (1L shl resolveComboOperand(operand).toInt())
            }
            if (nonJump) instructionPointer += 2
        }
        return outputs
    }

    private fun resolveComboOperand(operand: Int) =
        when (operand) {
            0, 1, 2, 3 -> operand.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> throw IllegalArgumentException("Illegal operand: $operand")
        }

}

private fun parseComputer(lines: List<String>) =
    Computer(
        lines[0].substringAfter(": ").toLong(),
        lines[1].substringAfter(": ").toLong(),
        lines[2].substringAfter(": ").toLong()
    )

private fun parseProgram(lines: List<String>) =
    lines[4].substringAfter(": ").split(',').map { it.toInt() }

private fun part1(lines: List<String>) =
    parseComputer(lines).runProgram(parseProgram(lines)).joinToString(",")

private fun part2(lines: List<String>): Long {
    var input = -1L
    var increment = 1L
    var matches = 0
    val program = parseProgram(lines)
    while (true) {
        input += increment
        val computer = parseComputer(lines).apply { a = input }
        val output = computer.runProgram(program)
        if (output.size <= matches || output.size > program.size || output != program.subList(0, output.size)) {
            continue
        }

        matches = output.size
        if (matches > 2) increment = 1L shl (matches * 3)

        if (output == program) return input
    }
}


fun main() {
    val testInput =
        """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
        """.trimIndent().lines()
    require(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")

    val testInput2 =
        """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0
        """.trimIndent().lines()
    require(part2(testInput2) == 117440L)

    val realInput = inputOfDay(17)
    println(part1(realInput))
    println(part2(realInput))
}