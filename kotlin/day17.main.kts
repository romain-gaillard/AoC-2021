import java.io.File
import kotlin.math.max
import kotlin.math.abs

class Area(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
}

fun shoot(xInitialVel: Int, yInitialVel: Int, targetArea: Area): Pair<Boolean, Int> {
    var currentX = 0
    var currentY = 0

    var xVel = xInitialVel
    var yVel = yInitialVel

    var maxY = 0

    // Perform another step until be are below the target area
    while (currentY >= targetArea.y1 || currentY >= targetArea.y2) {
        currentX += xVel
        currentY += yVel

        if (xVel != 0)
            xVel += if (xVel > 0) -1 else 1
        yVel -= 1

        maxY = max(maxY, currentY)

        // Check if we are in the target area
        if (targetArea.x1 <= currentX && currentX <= targetArea.x2 &&
            targetArea.y1 <= currentY && currentY <= targetArea.y2) {
            return Pair(true, maxY)
        }
    }

    return Pair(false, 0)
}

val fileName = if (args.size > 0) args[0] else "day17.txt"

val areaStr = File(fileName).readLines()[0].replace("target area: ", "").split(", ")
val (areaX1, areaX2) = areaStr[0].replace("x=", "").split("..").map { it.toInt() }
val (areaY1, areaY2) = areaStr[1].replace("y=", "").split("..").map { it.toInt() }

val targetArea = Area(areaX1, areaX2, areaY1, areaY2)
var maxY = 0
var successes = 0

for (x in 0..max(areaX1, areaX2)) {
    for (y in -(max(abs(areaY1), abs(areaY2)))..max(abs(areaY1), abs(areaY2))) {
        val result = shoot(x, y, targetArea)
        if (result.first) {
            successes++
            maxY = max(maxY, result.second)
        }
    }
}

println(maxY)
println(successes)
