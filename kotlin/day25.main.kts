import java.io.File

fun printGrid(grid: Array<Array<Char>>) {
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            print(grid[i][j])
        }
        println()
    }
}

fun step(grid: Array<Array<Char>>): Pair<Int, Array<Array<Char>>> {
    var moves = 0
    val newGrid = Array(grid.size) { Array(grid[0].size) {'.'} }

    // Move east-facing sea cucumbers
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '.')
                continue
            if (grid[i][j] == 'v') {
                newGrid[i][j] = 'v'
                continue
            }

            val newX = if (grid[i][j] == '>') (j + 1) % grid[i].size else j

            if (grid[i][newX] == '.') {
                newGrid[i][newX] = grid[i][j]
                newGrid[i][j] = '.'
                moves++
            } else {
                newGrid[i][j] = grid[i][j]
            }
        }
    }

    // Move south-facing sea cucumbers
    val newGrid2 = Array(grid.size) { Array(grid[0].size) {'.'} }
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (newGrid[i][j] == '.')
                continue
            if (newGrid[i][j] == '>') {
                newGrid2[i][j] = '>'
                continue
            }

            val newY = if (newGrid[i][j] == 'v') (i + 1) % grid.size else i

            if (newGrid[newY][j] == '.') {
                newGrid2[newY][j] = newGrid[i][j]
                newGrid2[i][j] = '.'
                moves++
            } else {
                newGrid2[i][j] = newGrid[i][j]
            }
        }
    }

    return Pair(moves, newGrid2)
}

val fileName = if (args.isNotEmpty()) args[0] else "day25.txt"
val lines = File(fileName).readLines()

var grid = Array(lines.size) { Array(lines[0].length) {'.'} }

for (i in lines.indices) {
    for (j in lines[i].indices) {
        grid[i][j] = lines[i][j]
    }
}

var moved = true
var nbSteps = 0

while (moved) {
    val res = step(grid)
    grid = res.second
    if (res.first == 0)
        moved = false
    nbSteps++
}

println(nbSteps)
// No part 2 for this day
