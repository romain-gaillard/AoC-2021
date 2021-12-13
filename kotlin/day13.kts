import java.io.File
import kotlin.math.*

class Paper {
    private val points = mutableMapOf<Pair<Int, Int>, Char>()
    var width = 0
        private set
    var height = 0
        private set

    fun setPoint(x: Int, y: Int, value: Char) {
        width = max(width, x + 1)
        height = max(height, y + 1)

        points[Pair(x, y)] = value
    }

    fun getPoint(x: Int, y: Int): Char {
        return points.getOrDefault(Pair(x, y), '.')
    }

    fun fold(isVerticalFolding: Boolean, lineIndex: Int) {
        if (isVerticalFolding) {
            for (y in 0 until height) {
                for (x in 0 until lineIndex) {
                    if (getPoint(width - x - (if (width % 2 == 0) 0 else 1), y) == '#')
                        setPoint(x, y, '#')
                }
            }

            width = lineIndex
        } else {
            for (y in 0 until lineIndex) {
                for (x in 0 until width) {
                    if (getPoint(x, height - y - (if (height % 2 == 0) 0 else 1)) == '#')
                        setPoint(x, y, '#')
                }
            }

            height = lineIndex
        }
    }

    fun print() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                print(getPoint(x, y))
            }
            println()
        }
    }

    fun countPoints(): Int {
        var counter = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (getPoint(x, y) == '#')
                    counter++
            }
        }

        return counter
    }
}

val paper = Paper()

val fileName = if (args.size > 0) args[0] else "day13.txt"
val lines = File(fileName).readLines()

var processingPoints = true
var isFirstFold = true
for (line in lines) {
    if (processingPoints) {
        if (line.isBlank()) {
            processingPoints = false
            continue
        }

        val (x, y) = line.split(",").map { it.toInt() }
        paper.setPoint(x, y, '#')
    } else {
        val (axis, coord) = line.replace("fold along ", "").split("=")

        paper.fold(axis == "x", coord.toInt())
        if (isFirstFold) {
            println(paper.countPoints())
            isFirstFold = false
        }
    }
}

paper.print()
