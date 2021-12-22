import java.io.File

val fileName = if (args.isNotEmpty()) args[0] else "day01.txt"
val lines = File(fileName).readLines().map { it.toInt() }

var nbIncreases = 0
var nbIncreasesWindow = 0

for (i in lines.indices) {
    if (i > 0) {
        if (lines[i] > lines[i - 1])
            nbIncreases++
    }

    if (i > 2) {
        if (lines[i] + lines[i - 1] + lines[i - 2] > lines[i - 1] + lines[i - 2] + lines[i - 3])
            nbIncreasesWindow++
    }
}

println(nbIncreases)
println(nbIncreasesWindow)
