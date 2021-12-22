import java.io.File

fun findAllPaths(caves: MutableMap<String, MutableList<String>>,
                 canVisitASmallCaveTwice: Boolean): MutableList<MutableList<String>> {

    val visited = mutableListOf<String>()
    val currentPath = mutableListOf<String>()
    val paths = mutableListOf<MutableList<String>>()

    visitCave(caves, visited, currentPath, paths,"start", canVisitASmallCaveTwice, false)

    return paths
}

fun isBigCave(cave: String): Boolean {
    for (c in cave) {
        if (c.isLowerCase())
            return false
    }

    return true
}

fun visitCave(caves: MutableMap<String, MutableList<String>>, visited: MutableList<String>,
              currentPath: MutableList<String>, paths: MutableList<MutableList<String>>,
              currentCave: String, canVisitASmallCaveTwice: Boolean, smallCaveVisitedTwice: Boolean) {

    visited.add(currentCave)
    currentPath.add(currentCave)

    if (currentCave == "end") {
        paths.add(currentPath.toMutableList())
    } else {
        val adjacentCaves = caves.getOrDefault(currentCave, mutableListOf())

        for (nextCave in adjacentCaves) {
            if (nextCave == "start")
                continue

            var canVisit = (isBigCave(nextCave) || (nextCave !in visited))
            var smallCaveVisitedTwiceNow = smallCaveVisitedTwice

            // If we are in the second part of the puzzle, one small cave can be visited twice
            // We check if we are in a position where we can still do this
            if (!canVisit && canVisitASmallCaveTwice && !smallCaveVisitedTwice) {
                canVisit = true
                smallCaveVisitedTwiceNow = true
            }

            if (canVisit)
                visitCave(caves, visited, currentPath, paths, nextCave, canVisitASmallCaveTwice, smallCaveVisitedTwiceNow)
        }
    }

    currentPath.removeLast()
    visited.remove(currentCave)
}

fun buildGraph(fileName: String): MutableMap<String, MutableList<String>> {
    val caves = mutableMapOf<String, MutableList<String>>()

    File(fileName).forEachLine {
        val (start, end) = it.split("-")

        // Bidirected graph, so we add both start->end and end->start into the list of edges
        caves[start] = (caves.getOrDefault(start, mutableListOf()) + end).toMutableList()
        caves[end] = (caves.getOrDefault(end, mutableListOf()) + start).toMutableList()
    }

    return caves
}

val fileName = if (args.isNotEmpty()) args[0] else "day12.txt"

// List of edges of the bidirected graph
var caves = buildGraph(fileName)

println(findAllPaths(caves, false).size)
println(findAllPaths(caves, true).size)