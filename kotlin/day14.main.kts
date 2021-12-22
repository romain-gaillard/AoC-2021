import java.io.File

val fileName = if (args.isNotEmpty()) args[0] else "day14.txt"
val lines = File(fileName).readLines()

fun step(polymer: MutableMap<Pair<Char, Char>, Long>, rules: MutableMap<Pair<Char, Char>, Char>):
        MutableMap<Pair<Char, Char>, Long> {

    val newPolymer = mutableMapOf<Pair<Char, Char>, Long>()

    for ((k, v) in polymer) {
        val newChar = rules.getOrDefault(k, ' ')
        if (newChar != ' ') {
            newPolymer.merge(Pair(k.first, newChar), v, Long::plus)
            newPolymer.merge(Pair(newChar, k.second), v, Long::plus)
        } else {
            newPolymer.merge(Pair(k.first, k.second), v, Long::plus)
        }
    }

    return newPolymer
}

fun count(polymer: MutableMap<Pair<Char, Char>, Long>, lastChar: Char): Long {
    val counters = mutableMapOf<Char, Long>()

    // We only count the first character of each pair as the second one will be the
    // first character of the next pair (except for the last character of the polymer)
    for ((k, v) in polymer) {
        counters.merge(k.first, v, Long::plus)
    }

    // We have to count the last character of the polymer as it's not the first character of a pair
    counters.merge(lastChar, 1L, Long::plus)

    return counters.maxByOrNull { it.value }?.value!! - counters.minByOrNull { it.value }?.value!!
}

var polymer = mutableMapOf<Pair<Char, Char>, Long>()
val rules = mutableMapOf<Pair<Char, Char>, Char>()
var lastChar = ' '

lines.forEachIndexed { i, line ->
    if (line.isBlank())
        return@forEachIndexed
    if (i == 0) {
        for (j in line.dropLast(1).indices) {
            polymer.merge(Pair(line[j], line[j + 1]), 1L, Long::plus)
        }

        // We save the last character of the initial polymer as it will also be the last
        // character of the final polymer and we need this for the counting
        lastChar = line.last()
    } else {
        val (left, right) = line.split(" -> ")
        rules[Pair(left[0], left[1])] = right[0]
    }
}

var partOne = 0L
for (i in 1..40) {
    polymer = step(polymer, rules)

    if (i == 10)
        partOne = count(polymer, lastChar)
}

println(partOne)
println(count(polymer, lastChar))
