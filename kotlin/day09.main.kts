import java.io.File

fun calculateRisk(map: List<List<Int>>, x: Int, y: Int, lowPoints: MutableList<Pair<Int, Int>>): Int {
    val value = map[x][y]

    // UP
    if (y > 0) {
        if (map[x][y - 1] <= value)
            return 0
    }
    // DOWN
    if (y + 1< map[x].size) {
        if (map[x][y + 1] <= value)
            return 0
    }
    // LEFT
    if (x > 0) {
        if (map[x - 1][y] <= value)
            return 0
    }
    // RIGHT
    if (x + 1 < map.size) {
        if (map[x + 1][y] <= value)
            return 0
    }

    lowPoints.add(Pair(x, y))
    return value + 1
}

fun calculateBasinSize(map: List<List<Int>>, x: Int, y: Int, visited: MutableList<Pair<Int, Int>>): Int {
    var size = 0

    if (Pair(x, y) in visited)
        return 0

    visited.add(Pair(x, y))

    if (x < 0 || x >= map.size)
        return 0
    if (y < 0 || y >= map[x].size)
        return 0

    if (map[x][y] == 9)
        return 0

    // UP
    size += calculateBasinSize(map, x, y - 1, visited)
    // DOWN
    size += calculateBasinSize(map, x, y + 1, visited)

    // LEFT
    size += calculateBasinSize(map, x - 1, y, visited)
    // RIGHT
    size += calculateBasinSize(map, x + 1, y, visited)

    return size + 1
}

val fileName = if (args.isNotEmpty()) args[0] else "day09.txt"
val lines = File(fileName).readLines().map{ it.map { Character.getNumericValue(it) } }

// Part 1
var risk = 0
val lowPoints = mutableListOf<Pair<Int, Int>>()
for (i in lines.indices) {
    for (j in lines[i].indices) {
        risk += calculateRisk(lines, i, j, lowPoints)
    }
}


// Part 2
val basins = mutableListOf<Int>()
val visited = mutableListOf<Pair<Int, Int>>()
for (point in lowPoints) {
    basins.add(calculateBasinSize(lines, point.first, point.second, visited))
}

println(risk)
println(basins.sortedDescending().take(3).reduce { acc, i ->  acc * i })
