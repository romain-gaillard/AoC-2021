import java.io.File
import java.io.Serializable

data class Quintuple<out A, out B, out C, out D, out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
) : Serializable

fun part1(playerStartingPositions: IntArray): Int {
    val playerPositions = playerStartingPositions.clone()
    val playerScores = Array(2) { 0 }
    var nbDiceRolls = 0
    var diceValue = 0
    var currentPlayer = 0

    while(playerScores[0] < 1000 && playerScores[1] < 1000) {
        for (i in 1..3) {
            nbDiceRolls++
            diceValue++
            if (diceValue > 100)
                diceValue = 1

            playerPositions[currentPlayer] += diceValue
            playerPositions[currentPlayer] = ((playerPositions[currentPlayer] - 1) % 10) + 1
        }

        playerScores[currentPlayer] += playerPositions[currentPlayer]
        currentPlayer = if (currentPlayer == 1) 0 else 1
    }

    val losingScore = if (playerScores[0] < playerScores[1]) playerScores[0] else playerScores[1]
    return nbDiceRolls * losingScore
}

fun part2(playerPositions: IntArray): Long {
    val result = part2Aux(playerPositions[0], playerPositions[1], 0, 0, true,
        mutableMapOf())

    return if (result.first > result.second) result.first else result.second
}

fun part2Aux(player1Position: Int, player2Position: Int, player1Score: Int, player2Score: Int, player1Plays: Boolean,
             states: MutableMap<Quintuple<Int, Int, Int, Int, Boolean>, Pair<Long, Long>>): Pair<Long, Long> {
    if (player1Score >= 21)
        return Pair(1L, 0L)
    else if (player2Score >= 21)
        return Pair(0L, 1L)

    var result = Pair(0L, 0L)

    val savedState = states[Quintuple(player1Position, player2Position, player1Score, player2Score, player1Plays)]
    if (savedState != null) {
        return savedState
    }

    for (roll1 in 1..3) {
        for (roll2 in 1..3) {
            for (roll3 in 1..3) {
                val rolls = roll1 + roll2 + roll3
                var nextPlayer1Position = player1Position
                var nextPlayer2Position = player2Position
                var nextPlayer1Score = player1Score
                var nextPlayer2Score = player2Score

                if (player1Plays) {
                    nextPlayer1Position += rolls
                    nextPlayer1Position = ((nextPlayer1Position - 1) % 10) + 1
                    nextPlayer1Score += nextPlayer1Position
                } else {
                    nextPlayer2Position += rolls
                    nextPlayer2Position = ((nextPlayer2Position - 1) % 10) + 1
                    nextPlayer2Score += nextPlayer2Position
                }

                val currentResult = part2Aux(nextPlayer1Position, nextPlayer2Position, nextPlayer1Score,
                    nextPlayer2Score, !player1Plays, states)
                
                result = Pair(result.first + currentResult.first, result.second + currentResult.second)
            }
        }
    }

    states[Quintuple(player1Position, player2Position, player1Score, player2Score, player1Plays)] = result

    return result
}

val fileName = if (args.isNotEmpty()) args[0] else "day21.txt"
var playerPositions = File(fileName).readLines().map {
    it.replace("Player.*starting position: ".toRegex(), "").toInt()
}.toIntArray()

println(part1(playerPositions))
println(part2(playerPositions))


