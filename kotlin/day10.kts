import java.io.File
import java.util.ArrayDeque

val fileName = if (args.size > 0) args[0] else "day10.txt"
val lines = File(fileName).readLines()

var scorePartOneMap = mutableMapOf<Char, Int>()
scorePartOneMap[')'] = 3
scorePartOneMap[']'] = 57
scorePartOneMap['}'] = 1197
scorePartOneMap['>'] = 25137

var scorePartTwoMap = mutableMapOf<Char, Int>()
scorePartTwoMap['('] = 1
scorePartTwoMap['['] = 2
scorePartTwoMap['{'] = 3
scorePartTwoMap['<'] = 4

var closingChars = mutableMapOf<Char, Char>()
closingChars[')'] = '('
closingChars[']'] = '['
closingChars['}'] = '{'
closingChars['>'] = '<'

var scorePartOne = 0
var scoresPartTwo = mutableListOf<Long>()

linesLoop@ for (line in lines) {
    var stack = ArrayDeque<Char>()
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
