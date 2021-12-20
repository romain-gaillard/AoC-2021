import java.io.File
import java.util.ArrayDeque

val fileName = if (args.size > 0) args[0] else "day10.txt"
val lines = File(fileName).readLines()

var scorePartOneMap = mapOf<Char, Int>(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
var scorePartTwoMap = mapOf<Char, Int>('(' to 1, '[' to 2, '{' to 3, '<' to 4)
var closingChars = mapOf<Char, Char>(')' to '(', ']' to '[', '}' to '{', '>' to '<')

var scorePartOne = 0
val scoresPartTwo = mutableListOf<Long>()

linesLoop@ for (line in lines) {
    val stack = ArrayDeque<Char>()
    var currentScorePartTwo = 0L

    for (c in line) {
        if (c !in closingChars) {
            // Opening character, push it on the stack
            stack.push(c)
        } else {
            // Closing character, pop the last opening character from the stack
            val openingChar = stack.pop()

            // The line is corrupted if the current closing character doesn't match
            // the last opening character we saw
            if (openingChar != closingChars[c]) {
                scorePartOne += scorePartOneMap.getOrDefault(c, 0)
                continue@linesLoop
            }
        }
    }

    // At this point, we know it's an "incomplete" line
    // We'll pop the rest of the stack and count the points according to the
    // opening characters that remained to be closed
    for (c in stack) {
        currentScorePartTwo *= 5
        currentScorePartTwo += scorePartTwoMap.getOrDefault(c, 0)
    }

    scoresPartTwo.add(currentScorePartTwo)
}

println(scorePartOne)
println(scoresPartTwo.sorted()[scoresPartTwo.size / 2])
