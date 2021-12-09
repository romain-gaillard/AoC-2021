import java.io.File

val fileName = if (args.size > 0) args[0] else "day05.txt"

fun computeRange(a: Int, b: Int): IntProgression {
    if (a <= b)
        return a..b
    else
        return a downTo b
}

fun increaseMapValue(map: HashMap<Pair<Int, Int>, Int>, x: Int, y: Int): Unit {
    val value = map.getOrDefault(Pair(x, y), 0)
    map.put(Pair(x, y), value + 1)
}

var grid = hashMapOf<Pair<Int, Int>, Int>()
var grid2 = hashMapOf<Pair<Int, Int>, Int>()

File(fileName).forEachLine {
    val (start, end) = it.split("->")
    val (x1, y1) = start.trim().split(",").map { it.toInt() }
    val (x2, y2) = end.trim().split(",").map { it.toInt() }

    if (x1 == x2 || y1 == y2) {
        for (x in computeRange(x1, x2)) {
            for (y in computeRange(y1, y2)) {
                increaseMapValue(grid, x, y)
                increaseMapValue(grid2, x, y)
            }
        }
    } else {
        val xIncr = if (x2 > x1) 1 else -1
        val yIncr = if (y2 > y1) 1 else -1
        val length = Math.abs(x2 - x1)

        var currentX = x1
        var currentY = y1
        for (i in 0..length) {
            increaseMapValue(grid2, currentX, currentY)
            currentX += xIncr
            currentY += yIncr
        }
    }
}

println(grid.filterValues { it > 1 }.size)
println(grid2.filterValues { it > 1 }.size)
