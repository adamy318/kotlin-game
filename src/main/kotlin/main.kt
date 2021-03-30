import kotlin.random.Random

fun main() {

    //makes grid to play on
    val s = 3
    val grid = Array(s) { _ -> Array(s) { _ -> 0 } }

    var monsterCount = 0

    while (monsterCount < 5) {
        var (x, y) = Pair(Random.nextInt(0,s), Random.nextInt(0,s))
        if (grid[x][y] != 4) {
            grid[x][y] = 4
            monsterCount++
        }
    }

    var player: Pair<Int, Int>
    var (x, y) = Pair(Random.nextInt(0,s), Random.nextInt(0,s))
    if (grid[x][y] == 4) {
        while (grid[x][y] == 4) {
            x = Random.nextInt(0,s)
            y = Random.nextInt(0,s)
        }
    }
    player = Pair(x, y)

    //var monsters: MutableMap<Int, MutableList<Int>> = mutableMapOf(1 to mutableListOf(2))

    println("The size of the map is ${grid.size}.")
    println("Your position is at (${player.first},${player.second}).")
    println("There are $monsterCount monsters that you need to kill.")
    println("Kill them all to beat the game.")
    println()
    while (true) {
        var inValidMove = false
        val prevPlayerLocation = player

        println("You are at position (${player.first},${player.second}).")
        print("Your move: ")

        val input = readLine()

        if (input != null) {
            when (input) {
                "left" -> player = Pair(player.first - 1, player.second)
                "right" -> player = Pair(player.first + 1, player.second)
                "up" -> player = Pair(player.first, player.second + 1)
                "down" -> player = Pair(player.first, player.second - 1)
                else ->  inValidMove = true
            }

            if (inValidMove) {
                println("Invalid move. Try again.")
                println()
                continue
            }

            if (player.first !in grid.indices || player.second !in grid.indices) {
                if (player.first < 0) {
                    println("Cannot move left. Please try another move.")
                }
                if (player.first == grid.size) {
                    println("Cannot move right. Please try another move")
                }
                if (player.second < 0) {
                    println("Cannot move down. Please try another move")
                }
                if (player.second == grid.size) {
                    println("Cannot move up. Please try another move")
                }
                println()
                player = prevPlayerLocation
                continue
            }

            if (grid[player.first][player.second] == 4) {
                println("You have encountered a monster!")
                println("Type 'kill' to kill it.")
                print(": ")
                var kill = readLine()
                if (kill != "kill") {
                    while (kill != "kill") {
                        println("Type 'kill' to kill the monster")
                        print(": ")
                        kill = readLine()
                    }
                }
                monsterCount--
                if (monsterCount == 0) {
                    println("Congrats! You have killed all the monsters and beat the game!")
                    break
                } else {
                    grid[player.first][player.second] = 0
                    println("You have killed the monster! Only $monsterCount left to go.")
                }
            }

        } else {
            println("Please try again.")
            println()
        }
    }
}