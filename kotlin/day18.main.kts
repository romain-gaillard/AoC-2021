@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
import kotlinx.serialization.json.*
import java.io.File
import kotlin.math.max

fun main(args: Array<String>) {
    val fileName = if (args.size > 0) args[0] else "day18.txt"

    val lines = File(fileName).readLines()

    var currentSum: MathPair? = null
    for (line in lines) {
        if (currentSum == null) {
            currentSum = buildMathPair(line)
            continue
        }

        currentSum = addMathPairs(currentSum, buildMathPair(line))
        reduce(currentSum)
    }

    println(magnitude(currentSum!!))

    var maxSum = 0
    for (i in lines.indices) {
        for (j in lines.indices) {
            if (i == j)
                continue

            currentSum = addMathPairs(buildMathPair(lines[i]), buildMathPair(lines[j]))
            reduce(currentSum)

            maxSum = max(maxSum, magnitude(currentSum))
        }
    }

    println(maxSum)
}

class MathPair() {
    var leftPair: MathPair?
    var rightPair: MathPair?
    var leftValue: Int
    var rightValue: Int
    var parent: MathPair?

    init {
        leftPair = null
        rightPair = null
        leftValue = 0
        rightValue = 0
        parent = null
    }

    constructor(leftValue: Int, rightValue: Int, parent: MathPair?): this() {
        this.leftValue = leftValue
        this.rightValue = rightValue
        this.parent = parent
    }

    constructor(leftPair: MathPair, rightPair: MathPair, parent: MathPair?): this() {
        this.leftPair = leftPair
        this.rightPair = rightPair
        this.parent = parent
    }

    fun setLeft(leftPair: MathPair) {
        this.leftPair = leftPair
    }

    fun setLeft(leftValue: Int) {
        this.leftValue = leftValue
        this.leftPair = null
    }

    fun setRight(rightPair: MathPair) {
        this.rightPair = rightPair
    }

    fun setRight(rightValue: Int) {
        this.rightValue = rightValue
        this.rightPair = null
    }

    fun isLeftAValue(): Boolean {
        return leftPair == null
    }

    fun isRightAValue(): Boolean {
        return rightPair == null
    }

    override fun toString(): String {
        var result = "["
        result += if (isLeftAValue())
            leftValue.toString()
        else
            leftPair.toString()

        result += ","

        result += if (isRightAValue())
            rightValue.toString()
        else
            rightPair.toString()

        result += "]"

        return result
    }
}

fun buildMathPair(str: String): MathPair {
    val parser = Json { isLenient = true }

    val element: JsonArray = parser.parseToJsonElement(str).jsonArray

    return buildMathPairHelper(element, null)
}

fun buildMathPairHelper(element: JsonArray, parent: MathPair?): MathPair {
    val currentPair = MathPair()
    currentPair.parent = parent

    if (element[0] is JsonArray) {
        currentPair.setLeft(buildMathPairHelper(element[0].jsonArray, currentPair))
    } else {
        currentPair.setLeft(element[0].jsonPrimitive.toString().toInt())
    }
    if (element[1] is JsonArray) {
        currentPair.setRight(buildMathPairHelper(element[1].jsonArray, currentPair))
    } else {
        currentPair.setRight(element[1].jsonPrimitive.toString().toInt())
    }

    return currentPair
}

fun updateSuccessor(node: MathPair, value: Int) {
    var parent = node.parent
    var currentNode = node
    while (parent != null && currentNode == parent!!.rightPair) {
        currentNode = parent!!
        parent = parent!!.parent
    }

    if (parent == null)
        return

    currentNode = parent!!

    if (currentNode.isRightAValue()) {
        currentNode.setRight(currentNode.rightValue + value)
    } else {
        currentNode = currentNode.rightPair!!

        while (!currentNode.isLeftAValue()) {
            currentNode = currentNode.leftPair!!
        }

        currentNode.setLeft(currentNode.leftValue + value)
    }
}

fun updatePredecessor(node: MathPair, value: Int) {
    var parent = node.parent
    var currentNode = node
    while (parent != null && currentNode == parent!!.leftPair) {
        currentNode = parent!!
        parent = parent!!.parent
    }

    if (parent == null)
        return

    currentNode = parent!!

    if (currentNode.isLeftAValue()) {
        currentNode.setLeft(currentNode.leftValue + value)
    } else {
        currentNode = currentNode.leftPair!!

        while (!currentNode.isRightAValue()) {
            currentNode = currentNode.rightPair!!
        }

        currentNode.setRight(currentNode.rightValue + value)
    }
}

fun explode(currentPair: MathPair, level: Int): Boolean {
    if (currentPair.isLeftAValue() && currentPair.isRightAValue() && level >= 4) {

        updatePredecessor(currentPair, currentPair.leftValue)

        updateSuccessor(currentPair, currentPair.rightValue)

        if (currentPair.parent?.leftPair == currentPair) {
            currentPair.parent?.setLeft(0)
        }
        if (currentPair.parent?.rightPair == currentPair) {
            currentPair.parent?.setRight(0)
        }

        return true
    } else {
        if (!currentPair.isLeftAValue()) {
            if (explode(currentPair.leftPair!!, level + 1))
                return true
        }

        if (!currentPair.isRightAValue()) {
            if (explode(currentPair.rightPair!!, level + 1))
                return true
        }
    }

    return false
}

fun split(pair: MathPair): Boolean {
    if (pair.isLeftAValue()) {
        if (pair.leftValue >= 10) {
            val value = pair.leftValue / 2
            pair.setLeft(MathPair(value, pair.leftValue - value, pair))

            return true
        }
    } else {
        if (split(pair.leftPair!!))
            return true
    }

    if (pair.isRightAValue()) {
        if (pair.rightValue >= 10) {
            val value = pair.rightValue / 2
            pair.setRight(MathPair(value, pair.rightValue - value, pair))

            return true
        }
    } else
    {
        if (split(pair.rightPair!!))
            return true
    }

    return false
}


fun reduce(pair: MathPair) {
    var reducing = true

    while (reducing) {
        reducing = if (explode(pair, 0))
            true
        else
            split(pair)
    }
}

fun addMathPairs(pair1: MathPair, pair2: MathPair): MathPair {
    val newPair = MathPair(pair1, pair2, null)
    pair1.parent = newPair
    pair2.parent = newPair

    return newPair
}

fun magnitude(pair: MathPair): Int {
    if (pair.isLeftAValue() && pair.isRightAValue())
        return 3 * pair.leftValue + 2 * pair.rightValue

    if (pair.isLeftAValue())
        return 3 * pair.leftValue + 2 * magnitude(pair.rightPair!!)

    if (pair.isRightAValue())
        return 3 * magnitude(pair.leftPair!!) + 2 * pair.rightValue

    return 3 * magnitude(pair.leftPair!!) + 2 * magnitude(pair.rightPair!!)
}

main(args)