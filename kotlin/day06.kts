import java.io.File

fun simulateDay(population: Map<Int, Long>): Map<Int, Long> {
    var nextPopulation = mutableMapOf<Int, Long>()

    for (i in 8 downTo 0) {
        if ( i > 0) {
            nextPopulation.put(i - 1, population.getOrDefault(i, 0))
        } else {
            nextPopulation.put(6, nextPopulation.getOrDefault(6, 0) + population.getOrDefault(0, 0))
            nextPopulation.put(8, nextPopulation.getOrDefault(8, 0) + population.getOrDefault(0, 0))
        }
    }

    return nextPopulation
}

fun simulateUntil(population: Map<Int, Long>, days: Int): Map<Int, Long> {
    var nextPopulation = population.toMap()

    for (i in 1..days) {
        nextPopulation = simulateDay(nextPopulation)
    }

    return nextPopulation
}

val fileName = if (args.size > 0) args[0] else "day06.txt"
val lines = File(fileName).readLines()

// Convert the comma-separated  list of lanternfish counters to a frequency map <Int, Long>
var population = lines[0].split(",").map { it.trim().toInt() }.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

println(simulateUntil(population, 80).values.sum())
println(simulateUntil(population, 256).values.sum())
