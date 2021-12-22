import java.io.File
import java.util.*

class Edge(val sourceVertex: Pair<Int, Int>, val destinationVertex: Pair<Int, Int>, val weight: Int)

fun isValidVertex(vertex: Pair<Int, Int>, height: Int, width: Int): Boolean {
    return (vertex.first in 0 until width && vertex.second > 0 && vertex.second < height)
}

fun addNeighbour(edges: MutableMap<Pair<Int, Int>, MutableList<Edge>>, vertex: Pair<Int, Int>,
                 neighbour: Pair<Int, Int>, grid: List<List<Int>>) {

    if (isValidVertex(neighbour, grid.size, grid[0].size)) {
        val newEdge = Edge(vertex, neighbour, grid[neighbour.second][neighbour.first])
        val currentEdges = edges.getOrDefault(vertex, mutableListOf())

        edges[vertex] = (currentEdges + newEdge).toMutableList()
    }
}

fun addNeighbours(edges: MutableMap<Pair<Int, Int>, MutableList<Edge>>, vertex: Pair<Int, Int>, grid: List<List<Int>>) {
    val leftNeighbour = Pair(vertex.first - 1, vertex.second)
    val rightNeighbour = Pair(vertex.first + 1, vertex.second)
    val topNeighbour = Pair(vertex.first, vertex.second - 1)
    val bottomNeighbour = Pair(vertex.first, vertex.second + 1)

    addNeighbour(edges, vertex, leftNeighbour, grid)
    addNeighbour(edges, vertex, rightNeighbour, grid)
    addNeighbour(edges, vertex, topNeighbour, grid)
    addNeighbour(edges, vertex, bottomNeighbour, grid)
}

fun findShortestPath(grid: MutableList<MutableList<Int>>, nbTiles: Int): Int {
    val edges = mutableMapOf<Pair<Int, Int>, MutableList<Edge>>()
    var source = Pair(0, 0)
    var destination = Pair(0, 0)

    // It was obviously not the best idea to just duplicate the grid content
    // Instead, the algorithm should be adapted to calculate the correct weights on the fly
    if (nbTiles > 1) {
        // We duplicate the entries on each row
        for (y in grid.indices) {
            var currentRow = grid[y].toMutableList()
            for (t in 1 until nbTiles) {
                currentRow = currentRow.map { if (it == 9) 1 else (it + 1) }.toMutableList()
                grid[y] += currentRow
            }
        }

        // We duplicate each row
        var currentGrid = grid.toMutableList()
        for (t in 1 until nbTiles) {
            currentGrid = currentGrid.map { it.map { if (it == 9) 1 else (it + 1) }.toMutableList() }.toMutableList()
            grid += currentGrid
        }
    }

    // Build the list of edges in the graph
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            addNeighbours(edges, Pair(x, y), grid)

            if (x == 0 && y == 0)
                source = Pair(x, y)
            if (x == grid[y].size - 1 && y == grid.size - 1)
                destination = Pair(x, y)
        }
    }

    val queue = PriorityQueue<Pair<Pair<Int, Int>, Int>>(Comparator.comparingInt { elem -> elem.second })
    val distances = mutableMapOf<Pair<Int, Int>, Int>()
    val verticesDone = mutableMapOf<Pair<Int, Int>, Boolean>()

    queue.add(Pair(source, 0))

    distances[source] = 0
    verticesDone[source] = true

    // Perform Dijkstra
    while (!queue.isEmpty()) {
        val currentVertex = queue.poll().first

        for (edge in edges.getOrDefault(currentVertex, mutableListOf())) {
            val nextVertex = edge.destinationVertex
            val weight = edge.weight

            if (nextVertex !in verticesDone) {
                val distCurrentVertex = distances.getOrDefault(currentVertex, Int.MAX_VALUE)
                val distNextVertex = distances.getOrDefault(nextVertex, Int.MAX_VALUE)
                if (distCurrentVertex + weight < distNextVertex) {
                    val newDistance = distCurrentVertex + weight
                    distances[nextVertex] = newDistance
                    queue.add(Pair(nextVertex, newDistance))
                }
            }
        }

        verticesDone[currentVertex] = true
    }

    return distances.getOrDefault(destination, Int.MAX_VALUE)
}

val fileName = if (args.isNotEmpty()) args[0] else "day15.txt"

val grid = File(fileName).readLines().map { it.toList().map { Character.getNumericValue(it) } }

println(findShortestPath(grid.map { it.toMutableList() }.toMutableList(), 1))
println(findShortestPath(grid.map { it.toMutableList() }.toMutableList(), 5))
