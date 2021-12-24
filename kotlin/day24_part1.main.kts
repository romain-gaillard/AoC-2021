/**
 * Solution used for part 1
 *
 * I converted the input program to Kotlin instructions using a straightforward script.
 * Then I assumed the largest valid model number started with a 9 and hoped for the best.
 * So the first digit of the answer is "fixed"
 * The rest was basically bruteforcing the model number by opening 9 instances of this program, so that for
 * each instance, the second digit is fixed. For the record, I used Azure Cloud VMs to run the program :)
 *
 * Unfortunately, this cannot be used for part 2 as it doesn't seem very likely that my answer starts with a "1"
 * and there is no way I can bruteforce 14 digits.
 *
 * I'll have to be smarter for part 2 but it allowed to secure a very good rank for part 1:
 *
 * 91699394894995
 * real	68m41.203s
 * user	68m54.147s
 * sys	0m1.697s
 */

fun program(input: IntArray): Boolean {
    var w = 0
    var x = 0
    var y = 0
    var z = 0
    var i = 0

    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 1
    x = x + 13
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 3
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 1
    x = x + 11
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 12
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 1
    x = x + 15
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 9
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 26
    x = x + -6
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 12
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 1
    x = x + 15
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 2
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 26
    x = x + -8
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 1
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 26
    x = x + -4
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 1
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 1
    x = x + 15
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 13
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 1
    x = x + 10
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 1
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 1
    x = x + 11
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 6
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 26
    x = x + -11
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 2
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 26
    x = x + 0
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 11
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 26
    x = x + -8
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 10
    y = y * x
    z = z + y
    w = input[i++]
    x = x * 0
    x = x + z
    x = x % 26
    z = z / 26
    x = x + -7
    x = if (x == w) 1 else 0
    x = if (x == 0) 1 else 0
    y = y * 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + w
    y = y + 3
    y = y * x
    z = z + y

    return z == 0
}

var found = false
val digits: IntArray = intArrayOf(9, args[0].toInt(), 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9)

while(!found) {

    for (i in digits.size - 1 downTo 0) {
        // No zero in the answer
        if(digits[i] > 1) {
            digits[i]--
            break
        }
        else {
            digits[i] = 9
        }
    }

    found = program(digits)
    if (found) {
        for(i in digits) {
            print(i)
        }
        found = true
        println()
        break
    }
}
