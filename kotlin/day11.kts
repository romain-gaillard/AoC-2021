import java.io.File

fun applyToCells(grid: Array<Array<Int>>, transform: (Int) -> Int) {
    for (x in grid.indices)
        for (y in grid[x].indices)
            grid[x][y] = transform(grid[x][y])
}

fun nextStep(grid: Array<Array<Int>>): Int {
    var flashes = 0
    var flashedThisStep = mutableListOf<Pair<Int, Int>>()

    applyToCells(grid, { it + 1 })

    var newFlashes: Int
    do {
        newFlashes = makeOctopusesFlash(grid, flashedThisStep)
        flashes += newFlashes
    } while(newFlashes > 0)

    applyToCells(grid, { if (it > 9) 0 else it })

    return flashes
}

fun makeOctopusesFlash(grid: Array<Array<Int>>, flashedThisStep: MutableList<Pair<Int, Int>>): Int {
    var flashes = 0

    for (x in grid.indices) {
        for (y in grid[x].indices) {
            if (grid[x][y] > 9 && Pair(x, y) !in flashedThisStep) {
                flashes++
                flashedThisStep.add(Pair(x, y))
                updateAdjacents(grid, x, y)
            }
        }
    }

    return flashes
}

fun updateAdjacents(grid: Array<Array<Int>>, x: Int, y: Int) {
    var cellsToUpdate = mutableListOf<Pair<Int, Int>>()
    cellsToUpdate.add(Pair(x - 1, y)) // Top
    cellsToUpdate.add(Pair(x, y - 1)) // Left
    cellsToUpdate.add(Pair(x + 1, y)) // Down
    cellsToUpdate.add(Pair(x, y + 1)) // Right
    cellsToUpdate.add(Pair(x - 1, y - 1)) // Top-left
    cellsToUpdate.add(Pair(x - 1, y + 1)) // Top-right
    cellsToUpdate.add(Pair(x + 1, y - 1)) // Bottom-left
    cellsToUpdate.add(Pair(x + 1, y + 1)) // Bottom-right

    cellsToUpdate.filter { isInGrid(grid, it.first, it.second) }.forEach() {
        grid[it.first][it.second]++
    }
}

fun isInGrid(grid: Array<Array<Int>>, x: Int, y: Int): Boolean {
    if (x < 0 || y < 0 || x >= grid.size || y >= grid[x].size)
        return false

    return true
}

val fileName = if (args.size > 0) args[0] else "day11.txt"
val lines = File(fileName).readLines()

val GRID_HEIGHT = 10
val GRID_WIDTH = 10

var grid = Array(GRID_HEIGHT) {Array(GRID_WIDTH) {0} }

var row = 0
for (line in lines) {
    var column = 0
    for (c in line.trim()) {
        grid[row][column] = Character.getNumericValue(c)
        column++
    }
    row++
}

var step = 1
var stepAllFlashed = 0
var flashesAtStep100 = 0

// We simulate new steps until we have an answer for both parts
while (step <= 100 || stepAllFlashed == 0) {
    var newFlashes = nextStep(grid)

    if (step <= 100)
        flashesAtStep100 += newFlashes

    if (newFlashes == GRID_HEIGHT * GRID_WIDTH && stepAllFlashed == 0)
        stepAllFlashed = step

    step++
}

println(flashesAtStep100)
println(stepAllFlashed)
