import java.io.File

fun calculateScores(lines: List<String>): List<IntArray> {
    val scoreZeroes = IntArray(lines[0].length)
    val scoreOnes = IntArray(lines[0].length)

    for (line in lines) {
        for (d in line.indices) {
            if (line[d] == '0')
                scoreZeroes[d] += 1
            else
                scoreOnes[d] += 1
        }
    }

    return listOf(scoreZeroes, scoreOnes)
}

fun filterList(list: MutableList<String>, filterLeastCommon: Boolean): String {
    var nextDigit = 0
    while (list.size > 1) {
        val (scoreZeroes, scoreOnes)  = calculateScores(list)
        val scoreZero = scoreZeroes[nextDigit]
        val scoreOne = scoreOnes[nextDigit]

        val selectedChar = if (scoreOne >= scoreZero) {
            if (filterLeastCommon) '1' else '0'
        } else {
            if (filterLeastCommon) '0' else '1'
        }

        list.retainAll { it[nextDigit] == selectedChar }

        nextDigit++
    }

    return list[0]
}

val fileName = if (args.isNotEmpty()) args[0] else "day03.txt"
val lines = File(fileName).readLines()

// Part 1
var gammaStr = ""
var epsilonStr = ""

val (scoreZeroes, scoreOnes) = calculateScores(lines)

for (d in scoreZeroes.indices) {
    gammaStr += if (scoreZeroes[d] > scoreOnes[d]) '0' else '1'
    epsilonStr += if (scoreOnes[d] > scoreZeroes[d]) '0' else '1'
}

// Part 2
var oxygenStr = filterList(lines.toMutableList(), true)
var co2Str = filterList(lines.toMutableList(), false)

println(gammaStr.toLong(2) * epsilonStr.toLong(2))
println(oxygenStr.toLong(2) * co2Str.toLong(2))
