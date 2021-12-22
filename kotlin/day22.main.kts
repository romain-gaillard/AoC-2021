import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.math.abs

fun computeIntersections(entries: List<Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>>, index: Int): List<Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>> {
    val entry = entries.elementAt(index)
    val overlaps = mutableListOf<Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>>()

    for (i in index + 1 until entries.size) {
        val currentEntry = entries.elementAt(i)
        val xInter = intersection(entry.first.first, entry.first.second, currentEntry.first.first, currentEntry.first.second)
        val yInter = intersection(entry.second.first, entry.second.second, currentEntry.second.first, currentEntry.second.second)
        val zInter = intersection(entry.third.first, entry.third.second, currentEntry.third.first, currentEntry.third.second)

        if(xInter != null && yInter != null && zInter != null)
            overlaps.add(Triple(xInter, yInter, zInter))
    }

    return overlaps
}

fun intersection(start1: Int, end1: Int, start2: Int, end2: Int): Pair<Int, Int>? {
    if(start1 >= end2 || end1 <= start2)
        return null

    return Pair(max(start1, start2), min(end1, end2))
}

fun sizeNotOverlapping(entries: List<Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>>, index: Int): Long {
    val entry = entries.elementAt(index)

    val x = abs(entry.first.first - entry.first.second).toLong()
    val y = abs(entry.second.first - entry.second.second).toLong()
    val z = abs(entry.third.first - entry.third.second).toLong()

    var size = x * y * z

    val overlaps = computeIntersections(entries, index)

    for(i in overlaps.indices) {
        size -= sizeNotOverlapping(overlaps, i)
    }

    return size
}

val fileName = if (args.isNotEmpty()) args[0] else "day22.txt"

val entries = mutableMapOf<Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>, Boolean>()

File(fileName).forEachLine {
    val state = it.startsWith("on")
    val xRange = it.split(",")[0].replace("on x=", "").replace("off x=", "")
    val yRange = it.split(",")[1].replace("y=", "")
    val zRange = it.split(",")[2].replace("z=", "")

    val (x1, x2) = xRange.split("..").map { it.toInt() }
    val (y1, y2) = yRange.split("..").map { it.toInt() }
    val (z1, z2) = zRange.split("..").map { it.toInt() }

    entries[Triple(Pair(x1, x2 + 1), Pair(y1, y2 + 1), Pair(z1, z2 + 1))] = state
}

var part1 = 0L
var part2 = 0L
for(i in entries.keys.indices) {
    val currentEntry = entries.keys.elementAt(i)

    if(entries[currentEntry]!!) {
        part2 += sizeNotOverlapping(entries.keys.toList(), i)
    }

    // Only count this entry for part 1 if in the initialization zone
    if (currentEntry.first.first < -50 || currentEntry.first.second > 50)
        continue
    if (currentEntry.second.first < -50 || currentEntry.second.second > 50)
        continue
    if (currentEntry.third.first < -50 || currentEntry.third.second > 50)
        continue

    if(entries[currentEntry]!!) {
        part1 += sizeNotOverlapping(entries.keys.toList(), i)
    }
}

println(part1)
println(part2)
