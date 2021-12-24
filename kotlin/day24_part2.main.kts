/**
 * Solution used for part 2
 *
 * /!\ Doesn't read input, only works for mine
 *
 * After simplifying the input program, I noticed that it's basically 14 similar chunks that each act on a single digit.
 * There are two kinds of operations; half of them increase z whatever the digit (roughly multiplies it by 26), and
 * the other half decrease z (roughly divided by 26) if the digit matches a condition.
 *
 * From there, we see that we want always to meet the condition in the decreasing operations as it's the only way to get
 * z == 0 at the end of the program. That is, we don't try to guess the values of those digits but deduce it from
 * the current value of z in order to make the condition true. It therefore makes those 7 digits fixed.
 *
 * In the end, it means that we reduce the space of the solution to 9^7 (the non-fixed 7 digits)
 */

fun program(input: IntArray): Boolean {
    var w: Int
    var x: Int
    var z = 0
    var i = 0

    // Fixed digits: 3, 5, 6, 10, 11, 12, 13

    // Increases z
    w = input[i++]
    x = 13
    x = if (x == w) 0 else 1
    z *= (25 * x + 1)
    z += (w + 3) * x

    // Increases z
    w = input[i++]
    x = (z % 26) + 11
    x = if (x == w) 0 else 1
    z *= (25 * x + 1)
    z += (w + 12) * x

    // Increases z
    w = input[i++]
    x = (z % 26) + 15
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 9) * x

    // Decreases z if w=(z % 26) - 6
    // w = input[i++]
    // Force the digit to the right value to decrease z
    w = (z % 26) - 6
    // Check that w if a correct value
    if (w !in 1..9)
        return false
    input[i++] = w
    x = (z % 26) - 6
    z /= 26
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += x * (w + 12)

    // Increases z
    w = input[i++]
    x = (z % 26) + 15
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 2) * x

    // Decreases z if w = (z % 26) - 8
    // Force the digit to the right value to decrease z
    w = (z % 26) - 8
    // Check that w if a correct value
    if (w !in 1..9)
        return false
    input[i++] = w
    x = (z % 26) - 8
    z /= 26
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 1) * x

    // Decreases z if w = (z % 26) - 4
    // Force the digit to the right value to decrease z
    w = (z % 26) - 4
    // Check that w if a correct value
    if (w !in 1..9)
        return false
    input[i++] = w
    x = (z % 26) - 4
    z /= 26
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 1) * x

    // Increases z
    w = input[i++]
    x = (z % 26) + 15
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 13) * x

    // Increases z
    w = input[i++]
    x = (z % 26) + 10
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 1) * x

    // Increases z
    w = input[i++]
    x = (z % 26) + 11
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 6) * x

    // Decreases z if w = (z % 26) - 11
    // Force the digit to the right value to decrease z
    w = (z % 26) - 11
    // Check that w if a correct value
    if (w !in 1..9)
        return false
    input[i++] = w
    x = (z % 26) - 11
    z /= 26
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 2) * x

    // Decreases z if w = (z % 26)
    // Force the digit to the right value to decrease z
    w = (z % 26)
    // Check that w if a correct value
    if (w !in 1..9)
        return false
    input[i++] = w
    x = z % 26
    z /= 26
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 11) * x

    // Decreases z if w = (z % 26) - 8
    // Force the digit to the right value to decrease z
    w = (z % 26) - 8
    // Check that w if a correct value
    if (w !in 1..9)
        return false
    input[i++] = w
    x = (z % 26) - 8
    z /= 26
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 10) * x

    // Decreases z if w = (z % 26) - 7
    // Force the digit to the right value to decrease z
    w = (z % 26) - 7
    // Check that w if a correct value
    if (w !in 1..9)
        return false
    input[i] = w
    x = (z % 26) - 7
    z /= 26
    x = if (x == w) 0 else 1
    z *= (25 * x) + 1
    z += (w + 3) * x

    return z == 0
}

// Fixed digits: 3, 5, 6, 10, 11, 12, 13. It doesn't matter which value we set before calling "program" as their
// actual value will be fixed by the program.

// Greatest model number
outer@for (d0 in 9 downTo 1) {
    for(d1 in 9 downTo 1) {
        for(d2 in 9 downTo 1) {
            for (d4 in 9 downTo 1) {
                for(d7 in 9 downTo 1) {
                    for (d8 in 9 downTo 1) {
                        for (d9 in 9 downTo 1) {
                            val digits: IntArray = intArrayOf(d0, d1, d2, 0, d4, 0, 0, d7, d8, d9, 0, 0, 0, 0)
                            if (program(digits)) {
                                for(i in 0..13)
                                    print(digits[i])
                                println()
                                break@outer
                            }
                        }
                    }
                }
            }
        }
    }
}

// Smallest model number
outer@for (d0 in 1..9) {
    for(d1 in 1..9) {
        for(d2 in 1..9) {
            for (d4 in 1..9) {
                for(d7 in 1..9) {
                    for (d8 in 1..9) {
                        for (d9 in 1..9) {
                            val digits: IntArray = intArrayOf(d0, d1, d2, 0, d4, 0, 0, d7, d8, d9, 0, 0, 0, 0)
                            if (program(digits)) {
                                for(i in 0..13)
                                    print(digits[i])
                                println()
                                break@outer
                            }
                        }
                    }
                }
            }
        }
    }
}
