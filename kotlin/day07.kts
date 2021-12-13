import java.io.File
import kotlin.math.*

val fileName = if (args.size > 0) args[0] else "day07.txt"
val lines = File(fileName).readLines()

// Convert the comma-separated list of crabs' horizontal positions to a List of Int
var population = lines[0].split(",").map { it.trim().toInt() }

fun computeFuelConsumption(population: List<Int>, secondPart: Boolean): Int {
    var bestFuelConsumption = -1

    // Try every horizontal position to see which one is the best
    for (i in population.indices) {
        // Compute the fuel cost needed to move each crab to that position
        var currentFuelConsumption = 0

        for (j in population.indices) {
            val distance = abs(population[j] - i) + 1
            // The key to do the second part quickly was just to notice that we can use the formula
            // n*(n-1)/2 to have the sum of the first n integers
            val cost = if (!secondPart) distance else distance*(distance-1)/2

            currentFuelConsumption += cost
        }

        if (bestFuelConsumption == -1 || currentFuelConsumption < bestFuelConsumption)
            bestFuelConsumption = currentFuelConsumption
    }

    return bestFuelConsumption
}

println(computeFuelConsumption(population, false))
println(computeFuelConsumption(population, true))
