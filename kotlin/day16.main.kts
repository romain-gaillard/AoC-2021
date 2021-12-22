import java.io.File

class Stream(val content: CharArray, var start: Int, var end: Int, var totalVersion: Long)

fun String.toLongIgnorePadding(radix: Int): Long {
    val newString = this.trimStart { it == '0' }

    return if (newString.isEmpty())
        0
    else
        newString.toLong(radix)
}

fun readLong(stream: Stream, nbBits: Int): Long {
    return readString(stream, nbBits).toLongIgnorePadding(2)
}

fun readBit(stream: Stream): Boolean {
    val result = stream.content[stream.start]

    stream.start += 1

    return (result == '1')
}

fun readLiteral(stream: Stream): Long {
    var keepReading = true
    var literal = ""

    while (keepReading) {
        keepReading = readBit(stream)
        literal += readString(stream, 4)
    }

    return literal.toLongIgnorePadding(2)
}

fun readString(stream: Stream, nbBits: Int): String {
    val start = stream.start
    val end = stream.start + nbBits
    val result = content.slice(start until end).joinToString("")

    stream.start = end

    return result
}

fun decodePacket(stream: Stream): Pair<Long, Boolean> {
    if (stream.start == stream.end)
        return Pair(0, false)

    val version = readLong(stream, 3)
    val typeID = readLong(stream, 3)

    stream.totalVersion += version

    if (typeID == 4L) {
        // Literal type of packet
        return Pair(readLiteral(stream), true)
    }
    else {
        // Operator type of packet
        val isMode2 = readBit(stream)

        // Keep the list of values we obtain while we browse inner packets
        val innerValues = mutableListOf<Long>()

        if (!isMode2) {
            val length = readLong(stream, 15).toInt()

            var subStream = Stream(stream.content, stream.start, stream.start + length, 0L)

            var reachedEnd = false
            while (!reachedEnd) {
                val returnValue = decodePacket(subStream)
                reachedEnd = !returnValue.second
                if(!reachedEnd) {
                    innerValues.add(returnValue.first)
                    stream.totalVersion += subStream.totalVersion
                    subStream = Stream(stream.content, subStream.start, stream.start + length, 0L)
                }
            }

            stream.start = stream.start + length
        } else {
            val nbPackets = readLong(stream, 11)

            var subStream = Stream(stream.content, stream.start, stream.end, 0)
            for (i in 1..nbPackets) {
                innerValues.add(decodePacket(subStream).first)

                stream.totalVersion += subStream.totalVersion
                stream.start = subStream.start

                subStream = Stream(stream.content, stream.start, stream.end, 0)
            }
        }

        // We now process the values returned by the inner packets according to the type of this packet
        when (typeID) {
            0L -> return Pair(innerValues.sum(), true)
            1L -> return Pair(innerValues.reduce { acc, i ->  acc * i }, true)
            2L -> return Pair(innerValues.minOrNull()!!, true)
            3L -> return Pair(innerValues.maxOrNull()!!, true)
            5L -> return Pair(if (innerValues[0] > innerValues[1]) 1L else 0L, true)
            6L -> return Pair(if (innerValues[0] < innerValues[1]) 1L else 0L, true)
            7L -> return Pair(if (innerValues[0] == innerValues[1]) 1L else 0L, true)
        }
    }

    // We should never reach that line
    return Pair(0, false)
}

val fileName = if (args.isNotEmpty()) args[0] else "day16.txt"

val content = File(fileName).readLines()[0].map{
    it.toString().toInt(16).toString(2).padStart(4, '0')
}.joinToString("").toCharArray()

val stream = Stream(content, 0, content.size, 0L)
val result = decodePacket(stream).first
println(stream.totalVersion)
println(result)
