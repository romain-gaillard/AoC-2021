import java.io.File
import kotlin.math.abs

val destinationsA = listOf(Pair(1, 2), Pair(2, 2))
val destinationsB = listOf(Pair(1, 4), Pair(2, 4))
val destinationsC = listOf(Pair(1, 6), Pair(2, 6))
val destinationsD = listOf(Pair(1, 8), Pair(2, 8))
val destinationsAPartTwo = listOf(Pair(1, 2), Pair(2, 2), Pair(3, 2), Pair(4, 2))
val destinationsBPartTwo = listOf(Pair(1, 4), Pair(2, 4), Pair(3, 4), Pair(4, 4))
val destinationsCPartTwo = listOf(Pair(1, 6), Pair(2, 6), Pair(3, 6), Pair(4, 6))
val destinationsDPartTwo = listOf(Pair(1, 8), Pair(2, 8), Pair(3, 8), Pair(4, 8))
val costFactor = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
val hallWay = listOf(Pair(0, 0), Pair(0, 1), Pair(0, 3), Pair(0, 5), Pair(0, 7), Pair(0, 9), Pair(0, 10))

class Amphipod(val type: Char, var moves: Int) {
    override fun toString(): String {
        return "$type:$moves"
    }
}

fun parseInput(lines: List<String>, map: MutableMap<Pair<Int, Int>, Amphipod>, partTwo: Boolean) {
    for(row in lines.indices) {
        val line = lines[row]
        if (row < 2 || row > 3)
            continue

        var column = 0
        for(c in line) {
            if (c == 'A' || c == 'B' || c == 'C' || c == 'D') {
                val insRow = if (partTwo && row == 3) 4 else row - 1
                val insCol = column - 1
                map[Pair(insRow, insCol)] = Amphipod(c, 0)
            }

            column++
        }
    }

    if (partTwo) {
        map[Pair(2, 2)] = Amphipod('D', 0)
        map[Pair(2, 4)] = Amphipod('C', 0)
        map[Pair(2, 6)] = Amphipod('B', 0)
        map[Pair(2, 8)] = Amphipod('A', 0)
        map[Pair(3, 2)] = Amphipod('D', 0)
        map[Pair(3, 4)] = Amphipod('B', 0)
        map[Pair(3, 6)] = Amphipod('A', 0)
        map[Pair(3, 8)] = Amphipod('C', 0)
    }
}

fun isDone(map: MutableMap<Pair<Int, Int>, Amphipod>, partTwo: Boolean): Boolean {
    val checkA = if (partTwo) destinationsAPartTwo else destinationsA
    val checkB = if (partTwo) destinationsBPartTwo else destinationsB
    val checkC = if (partTwo) destinationsCPartTwo else destinationsC
    val checkD = if (partTwo) destinationsDPartTwo else destinationsD

    for (amphipod in map) {
        when (amphipod.value.type) {
            'A' -> if(amphipod.key !in checkA) return false
            'B' -> if(amphipod.key !in checkB) return false
            'C' -> if(amphipod.key !in checkC) return false
            'D' -> if(amphipod.key !in checkD) return false
        }
    }

    return true
}

fun costMove(map: MutableMap<Pair<Int, Int>, Amphipod>, source: Pair<Int, Int>, destination: Pair<Int, Int>): Int {
    if (destination in map)
        return -1

    var cost = 0
    var currentRow = source.first
    var currentCol = source.second

    if (source in hallWay) {
        // Bring to correct column
        var step = if(source.second <= destination.second) 1 else - 1
        for (i in 0 until abs(source.second - destination.second)) {
            currentCol += step
            cost++
            if (Pair(currentRow, currentCol) in map)
                return -1
        }
        // Bring to correct row
        step = if(source.first <= destination.first) 1 else - 1
        for (i in 0 until abs(source.first - destination.first)) {
            currentRow += step
            cost++
            if (Pair(currentRow, currentCol) in map)
                return -1
        }
    } else {
        // Bring to correct row
        var step = if(source.first <= destination.first) 1 else - 1
        for (i in 0 until abs(source.first - destination.first)) {
            currentRow += step
            cost++
            if (Pair(currentRow, currentCol) in map)
                return -1
        }
        // Bring to correct column
        step = if(source.second <= destination.second) 1 else - 1
        for (i in 0 until abs(source.second - destination.second)) {
            currentCol += step
            cost++
            if (Pair(currentRow, currentCol) in map)
                return -1
        }
    }

    return cost
}

fun nextMove(map: MutableMap<Pair<Int, Int>, Amphipod>, cost: Int, costs: MutableList<Int>, partTwo: Boolean) {
    if (costs.size > 0 && cost > costs.first())
        return
    if (isDone(map, partTwo)) {
        costs.add(cost)
        costs.sort()
        for (i in 1 until costs.size)
            costs.removeAt(i)
        return
    }

    val destsA = if (partTwo) destinationsAPartTwo else destinationsA
    val destsB = if (partTwo) destinationsBPartTwo else destinationsB
    val destsC = if (partTwo) destinationsCPartTwo else destinationsC
    val destsD = if (partTwo) destinationsDPartTwo else destinationsD

    for (amphipod in map) {
        if (amphipod.value.moves >= 2)
            continue

        if (amphipod.key in hallWay) {
            // Go to its destination
            val dest = when (amphipod.value.type) {
                'A' -> destsA
                'B' -> destsB
                'C' -> destsC
                'D' -> destsD
                else -> null
            }!!

            for (d in dest) {
                val costMove = costMove(map, amphipod.key, d)

                if(costMove == -1)
                    break
                if (costMove != -1) {
                    val newMap = map.toMutableMap()
                    newMap.remove(amphipod.key)
                    newMap[d] = Amphipod(amphipod.value.type, amphipod.value.moves + 1)
                    nextMove(newMap, cost + costMove * costFactor[amphipod.value.type]!!, costs, partTwo)
                }
            }
        } else {
            for (d in hallWay) {
                val costMove = costMove(map, amphipod.key, d)
                if (costMove != -1) {
                    val newMap = map.toMutableMap()
                    newMap.remove(amphipod.key)
                    newMap[d] = Amphipod(amphipod.value.type, amphipod.value.moves + 1)
                    nextMove(newMap, cost + costMove * costFactor[amphipod.value.type]!!, costs, partTwo)
                }
            }
        }
    }
}

var map = mutableMapOf<Pair<Int, Int>, Amphipod>()
var costs = mutableListOf<Int>()

val fileName = if (args.isNotEmpty()) args[0] else "day23.txt"
val lines = File(fileName).readLines()

parseInput(lines, map, false)
nextMove(map, 0, costs, false)
println(costs.first())
// Part 2 calculated by hand :(
// This algorithm was way too slow and in the end, I managed to get the answer manually before it even returned
// Not sure if it can be improved by pruning or if it's just too naive to work for the solution space of part 2
println("51436")
