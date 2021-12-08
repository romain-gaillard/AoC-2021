import java.io.File

fun countCharsInCommon(a: String, b: String): Int {
    var counter = 0
    for(c in a) {
        if(c in b)
            counter++
    }

    return counter
}

val fileName = if(args.size > 0) args[0] else "day8.txt"

var partOne = 0
var partTwo = 0

File(fileName).forEachLine {
    var (inputs, outputs) = it.split("|").map { it.trim() }
        .map { it.split(" ")}.map { it.map { it.trim() } }

    // Count the number of signal patterns for 2, 3, 4, and 7 segements
    // (corresponding respectively to digits 1, 7, 4, 8)
    partOne += outputs.filter{ it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }.size

    // We sort the signal patterns alphabetically so that they can be compared easily to see if they match
    inputs = inputs.map { it.toCharArray().sorted().joinToString("") }
    outputs = outputs.map { it.toCharArray().sorted().joinToString("") }

    // Building a list containing both inputs and outputs from which we will make the analysis on the
    // signal patterns
    val inputsAndOutputs = (inputs + outputs).distinct().toList()

    // We'll store the signal patterns that correspond to each digit
    var digitStr = Array(10 ) { "" }

    // Note: for all the deductions below, we assume all the 10 digits are always present in an entry

    // Case for 1 - contains 2 segments
    digitStr[1] = inputsAndOutputs.filter { it.length == 2 }[0]

    // Case for 4 - contains 7 segments
    digitStr[4] = inputsAndOutputs.filter { it.length == 4 }[0]

    // Case for 7 - contains 3 segments
    digitStr[7] = inputsAndOutputs.filter { it.length == 3 }[0]

    // Case for 8 - contains 7 segments
    digitStr[8] = inputsAndOutputs.filter { it.length == 7 }[0]

    // Case for 2, 3, 5 - contain 6 segments
    for(currentDigit in inputsAndOutputs.filter { it.length == 6 }) {
        if(countCharsInCommon(currentDigit, digitStr[7]) == 3) {
            // It's a 0 or a 9
            if(countCharsInCommon(currentDigit, digitStr[4]) == 4) {
                digitStr[9] = currentDigit
            }
            else  {
                digitStr[0] = currentDigit
            }
        }
        else {
            digitStr[6] = currentDigit
        }
    }

    // Case for 2, 3, 5 - contain 5 segments
    for(currentDigit in inputsAndOutputs.filter { it.length == 5 }) {
        if (countCharsInCommon(currentDigit, digitStr[4]) == 3) {
            // It's a 3 or a 5
            if (countCharsInCommon(currentDigit, digitStr[1]) == 2) {
                digitStr[3] = currentDigit
            } else {
                digitStr[5] = currentDigit
            }
        } else {
            digitStr[2] = currentDigit
        }
    }

    // Map each character of the signal pattern to the correspondig digit, build a string containing the number
    // And then, convert that to an Int
    partTwo += outputs.map { digitStr.indexOf(it) }.joinToString("","", "").toInt(10)
}

println(partOne)
println(partTwo)
