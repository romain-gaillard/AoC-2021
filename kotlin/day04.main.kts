import java.io.File

class Board {
    private var cells = Array(5) {Array(5) {0} }
    private var currentRow = 0

    fun addRow(row: String) {
        val entries = row.trim().replace("\\s+".toRegex(), " ").split(" ")

        for (i in entries.indices) {
            cells[currentRow][i] = entries[i].toInt()
        }

        currentRow++
    }

    fun isComplete(): Boolean {
        // Check rows
        for (i in cells.indices) {
            var fullRow = true
            for (j in 0 until cells[i].size) {
                if (cells[i][j] != -1)
                    fullRow = false
            }

            if (fullRow)
                return true
        }

        // Check columns
        for (j in 0 until cells[0].size) {
            var fullColumn = true
            for (element in cells) {
                if (element[j] != -1)
                    fullColumn = false
            }

            if (fullColumn)
                return true
        }

        return false
    }

    fun playNumber(number: Int) {
        for (i in cells.indices) {
            for (j in 0 until cells[i].size) {
                if (cells[i][j] == number)
                    cells[i][j] = -1
            }
        }

    }

    fun computeScore(): Int {
        var score = 0
        for (i in cells.indices) {
            for (j in 0 until cells[i].size) {
                if (cells[i][j] != -1)
                    score += cells[i][j]
            }
        }

        return score
    }
}

val fileName = if (args.isNotEmpty()) args[0] else "day04.txt"
val lines = File(fileName).readLines()

val drawnNumbers = lines[0].split(",")

var currentRow = 0
var currentBoard = Board()
var boards = mutableListOf<Board>()

// Create boards
for (i in 2 until lines.size) {
    val line = lines[i]
    if (line.isNotBlank()) {
        currentBoard.addRow(line)

        currentRow++
        if (currentRow == 5) {
            boards.add(currentBoard)
            currentBoard = Board()
            currentRow = 0
        }
    }
}

// Play
var foundWinning = false
for (number in drawnNumbers) {
    boards.map { it.playNumber(number.toInt()) }

    val fullBoards = boards.filter { it.isComplete() }

    // We assume only one board wins at a time (at least for the first and the last to win)
    if (fullBoards.size == 1) {
        // Check if it's the first board to win
        if (!foundWinning) {
            println(fullBoards[0].computeScore() * number.toInt())
            foundWinning = true
        } else if (boards.size == 1) {  // Check if it's the last board to win
            println(fullBoards[0].computeScore() * number.toInt())
        }
    }

    // Remove all winning boards from the list
    boards.removeAll { it.isComplete() }
}

