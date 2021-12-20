import java.io.File
import kotlin.math.*

fun permutations(list: List<Int>): List<List<Int>> {
    var result = mutableListOf<List<Int>>()

    for (i in 0..5) {
        for (j in 0..7) {
            result.add(applyTransformation(list, i, j).toMutableList() + mutableListOf<Int>(i, j))
        }
    }

    return result.toList()
}

fun applyTransformation(list: List<Int>, perm: Int, dir: Int): List<Int> {
    var newList = listOf<Int>()
    when (perm) {
        0 -> newList = listOf(list[0], list[1], list[2])
        1 -> newList = listOf(list[0], list[2], list[1])
        2 -> newList = listOf(list[1], list[0], list[2])
        3 -> newList = listOf(list[1], list[2], list[0])
        4 -> newList = listOf(list[2], list[0], list[1])
        5 -> newList = listOf(list[2], list[1], list[0])
    }

    when (dir) {
        0 -> newList = listOf(newList[0], newList[1], newList[2])
        1 -> newList = listOf(newList[0] * -1, newList[1], newList[2])
        2 -> newList = listOf(newList[0], newList[1] * -1, newList[2])
        3 -> newList = listOf(newList[0], newList[1], newList[2] * -1)
        4 -> newList = listOf(newList[0] * -1, newList[1] * -1, newList[2])
        5 -> newList = listOf(newList[0], newList[1] * -1, newList[2] * -1)
        6 -> newList = listOf(newList[0] * -1, newList[1], newList[2] * -1)
        7 -> newList = listOf(newList[0] * -1, newList[1] * -1, newList[2] * -1)
    }

    return newList
}

val fileName = if (args.size > 0) args[0] else "day19.txt"

var scannerIndex = -1
val scanners = mutableListOf<MutableList<List<Int>>>()

File(fileName).forEachLine {
    if (it.isBlank())
        return@forEachLine

    if (it.startsWith("--- ")) {
        scanners.add(mutableListOf<List<Int>>())
        scannerIndex++
        return@forEachLine
    }

    scanners[scannerIndex].add(it.split(",").map { it.toInt() })
}


var scannerOffsets = mutableMapOf<Pair<Int, Int>, List<Int>>()

var adding = true
while (adding) {
    adding = false

    for (i in scanners.indices) {
        for (j in scanners.indices) {
            if (i == j)
                continue

            if (scannerOffsets.get(Pair(i, j)) == null) {
                var offsets = mutableMapOf<List<Int>, Int>()

                startLoop@for (b in scanners[j].indices) {
                    for (perm in permutations(scanners[j][b])) {
                        for (k in scanners[i].indices) {
                            val offsetX = scanners[i][k][0] - perm[0]
                            val offsetY = scanners[i][k][1] - perm[1]
                            val offsetZ = scanners[i][k][2] - perm[2]

                            offsets.merge(listOf(offsetX, offsetY, offsetZ, perm[3], perm[4]), 1, Int::plus)

                            if (offsets.getOrDefault(listOf(offsetX, offsetY, offsetZ, perm[3], perm[4]), 0) >= 12) {
                                break@startLoop
                            }
                        }
                    }
                }

                offsets = offsets.filter { (_, value) -> value >= 12 }.toMutableMap()

                if(offsets.size > 0) {
                    val offset = offsets.keys.first()
                    val x = offset[0]
                    val y = offset[1]
                    val z = offset[2]
                    val perm = offset[3]
                    val dir = offset[4]

                    scannerOffsets.put(Pair(i, j), listOf<Int>(x, y, z, perm, dir))

                    for (b in scanners[j]) {
                        var newBeacon = applyTransformation(listOf<Int>(b[0], b[1], b[2]), perm, dir)
                        newBeacon = listOf<Int>(newBeacon[0] + x, newBeacon[1] + y, newBeacon[2] + z)

                        if (!scanners[i].contains(newBeacon)) {
                            scanners[i].add(newBeacon)
                            adding = true
                        }

                    }
                }
            } else {
                val offsets = scannerOffsets.get(Pair(i, j))!!
                val x = offsets[0]
                val y = offsets[1]
                val z = offsets[2]
                val perm = offsets[3]
                val dir = offsets[4]

                for (b in scanners[j]) {
                    var newBeacon = applyTransformation(listOf<Int>(b[0], b[1], b[2]), perm, dir)
                    newBeacon = listOf<Int>(newBeacon[0] + x, newBeacon[1] + y, newBeacon[2] + z)

                    if (!scanners[i].contains(newBeacon)) {
                        scanners[i].add(newBeacon)
                        adding = true
                    }
                }
            }
        }
    }
}

println(scanners[0].size)

var maxDist = 0
for (i in 0..scannerIndex) {
    for (j in 0..scannerIndex) {
        if (i == j)
            continue

        val entry = scannerOffsets.getOrDefault(Pair(i, j), listOf<Int>(0, 0, 0, 0, 0))
        val dist = abs(entry[0]) + abs(entry[1]) + abs(entry[2])

        maxDist = max(dist, maxDist)
    }
}

println(maxDist)