import java.io.File

fun String.toIntIgnorePadding(radix: Int): Int {
    val newString = this.trimStart { it == '0' }

    return if (newString.isEmpty())
        0
    else
        newString.toInt(radix)
}

fun getImageSize(image: MutableMap<Pair<Int, Int>, Int>): List<Int> {
    val x1 = image.minByOrNull { it.key.first }?.key?.first ?:0
    val x2 = image.maxByOrNull { it.key.first }?.key?.first ?:0
    val y1 = image.minByOrNull { it.key.second }?.key?.second ?:0
    val y2 = image.maxByOrNull { it.key.second }?.key?.second ?:0

    return listOf(x1, x2, y1, y2)
}

fun calculateNewPixelValue(inputImage: MutableMap<Pair<Int, Int>, Int>, x: Int, y: Int, default: Int,
                           enhancementAlgo: String): Int {
    var pattern = ""
    
    pattern += inputImage.getOrDefault(Pair(x - 1, y - 1), default)
    pattern += inputImage.getOrDefault(Pair(x, y - 1), default)
    pattern += inputImage.getOrDefault(Pair(x + 1, y - 1), default)
    pattern += inputImage.getOrDefault(Pair(x - 1, y), default)
    pattern += inputImage.getOrDefault(Pair(x, y), default)
    pattern += inputImage.getOrDefault(Pair(x + 1, y), default)
    pattern += inputImage.getOrDefault(Pair(x - 1, y + 1), default)
    pattern += inputImage.getOrDefault(Pair(x, y + 1), default)
    pattern += inputImage.getOrDefault(Pair(x + 1, y + 1), default)

    val patternInt = pattern.toIntIgnorePadding(2)

    return if (enhancementAlgo[patternInt] == '#') 1 else 0
}

fun enhance(inputImage: MutableMap<Pair<Int, Int>, Int>, default: Int, enhancementAlgo: String):
        Pair<MutableMap<Pair<Int, Int>, Int>, Int> {
    val outputImage = mutableMapOf<Pair<Int, Int>, Int>()

    val (x1, x2, y1, y2) = getImageSize(inputImage)

    // Compute the value of a pixel outside the main image pattern, to determine the value
    // of the pixels that will extend infinitely. This will be the new default value of the map
    val newDefault = calculateNewPixelValue(inputImage, x1 - 5, y1 - 5, default, enhancementAlgo)

    for (i in (y1 - 3)..(y2 + 3)) {
        for (j in (x1 - 3)..(x2 + 3)) {
            val newPixel = calculateNewPixelValue(inputImage, j, i, default, enhancementAlgo)
            if (newPixel != newDefault)
                outputImage[Pair(j, i)] = newPixel
        }
    }

    return Pair(outputImage, newDefault)
}

fun performSteps(inputImage: MutableMap<Pair<Int, Int>, Int>, enhancementAlgo: String, steps: Int): Int {
    var default = 0
    var currentImage = inputImage

    for (i in 1..steps) {
        val result = enhance(currentImage, default, enhancementAlgo)
        currentImage = result.first
        default = result.second
    }

    return currentImage.values.filter { it == 1 }.size
}

val fileName = if (args.isNotEmpty()) args[0] else "day20.txt"

var enhancementAlgo = ""
var inputImage = mutableMapOf<Pair<Int, Int>, Int>()

var rowIndex = 0
File(fileName).forEachLine {
    if (it.isBlank())
        return@forEachLine

    if (enhancementAlgo.isBlank()) {
        enhancementAlgo = it
        return@forEachLine
    }

    for (i in it.indices) {
        inputImage[Pair(i, rowIndex)] = if (it[i] == '#') 1 else 0
    }

    rowIndex++
}

println(performSteps(inputImage, enhancementAlgo, 2))
println(performSteps(inputImage, enhancementAlgo, 50))
