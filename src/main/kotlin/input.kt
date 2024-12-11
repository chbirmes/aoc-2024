import kotlin.io.path.Path
import kotlin.io.path.readLines

fun inputOfDay(day: Int): List<String> {
    val dayString = day.toString().padStart(2, '0')
    return Path("./src/main/resources/Day$dayString.txt").readLines()
}