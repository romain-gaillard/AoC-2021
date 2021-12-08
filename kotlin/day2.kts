import java.io.File

val fileName = if (args.size > 0) args[0] else "day2.txt"

// For part 1
var hPos = 0L
var depth = 0L

// For part 2
var aim = 0L
var hPos2 = 0L
var depth2 = 0L

File(fileName).forEachLine {
    val command = it.split(" ")[0]
    val arg = it.split(" ")[1].toInt()

    when (command) {
        "forward" -> {
            // Part 1
            hPos += arg
            // Part 2
            hPos2 += arg
            depth2 += aim * arg
        }
        "up" -> {
            // Part 1
            depth -= arg
            // Part 2
            aim -= arg
        }
        "down" -> {
            // Part 1
            depth += arg
            // Part 2
            aim += arg
        }
    }
}

println(hPos * depth)
println(hPos2 * depth2)
