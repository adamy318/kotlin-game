import kotlin.random.Random
import kotlin.system.exitProcess

abstract class Entity(var position: Pair<Int, Int>) {
    open var hp: Int = 0
    open val attackPower: Int = 0

    fun getXPos(): Int {
        return position.first
    }

    fun getYPos(): Int {
        return position.second
    }

    fun attack(player: Entity) {
        if (player.hp <= attackPower) {
            player.hp = -1
        } else {
            player.hp -= attackPower
        }
    }

    fun isDead(): Boolean {
        return this.hp == -1
    }

    fun checkSamePosition(entity: Entity): Boolean {
        return this.getXPos() == entity.getXPos() && this.getYPos() == entity.getYPos()
    }

}

class Monster(position: Pair<Int, Int>) : Entity(position = position) {
    override var hp = 50
    override val attackPower = 10
}

class Hero(position: Pair<Int, Int>) : Entity(position = position) {
    override var hp = 200
    override val attackPower = 40

    fun moveLeft() {
        position = Pair(position.first - 1, position.second)
    }

    fun moveRight() {
        position = Pair(position.first + 1, position.second)
    }

    fun moveUp() {
        position = Pair(position.first , position.second + 1)
    }

    fun moveDown() {
        position = Pair(position.first , position.second - 1)
    }

}

fun main() {

    //makes grid to play on
    val s = 3
    val grid = Array(s) { _ -> Array(s) { _ -> 0 } }

    val monsters = mutableListOf<Monster>()
    val monsterPositions = mutableListOf<Pair<Int,Int>>()

    while (monsters.size < 5) {

        var (x, y) = Pair(Random.nextInt(0,s), Random.nextInt(0,s))
        for (monster in monsters) {
            if (x == monster.getXPos() && y == monster.getYPos()) {
                while (x == monster.getXPos() && y == monster.getYPos()) {
                    x = Random.nextInt(0,s)
                    y = Random.nextInt(0,s)
                }
            }

        }
        monsters.add(Monster(Pair(x,y)))
    }

    val (x, y) = Pair(Random.nextInt(0,s), Random.nextInt(0,s))
    val player = Hero(Pair(x, y))

    for (monster in monsters) {
        monsterPositions.add(monster.position)
    }
    while (player.position !in monsterPositions) {
        player.position = Pair(Random.nextInt(0,s), Random.nextInt(0,s))
    }



    println("The size of the map is ${grid.size}.")
    println("Your position is at (${player.getXPos()},${player.getYPos()}).")
    println("There are ${monsters.size} monsters that you need to kill.")
    println("Kill them all to beat the game.")
    println()
    while (true) {
        var inValidMove = false
        val prevPlayerLocation = player.position

        println("You are at position (${player.getXPos()},${player.getYPos()}).")
        print("Your move: ")

        val input = readLine()

        if (input != null) {
            when (input) {
                "left" -> player.moveLeft()
                "right" -> player.moveRight()
                "up" -> player.moveUp()
                "down" -> player.moveDown()
                else ->  inValidMove = true
            }

            if (inValidMove) {
                println("Invalid move. Try again.")
                println()
                continue
            }

            if (player.getXPos() !in grid.indices || player.getYPos() !in grid.indices) {
                if (player.getXPos() < 0) {
                    println("Cannot move left. Please try another move.")
                }
                if (player.getXPos() == grid.size) {
                    println("Cannot move right. Please try another move")
                }
                if (player.getYPos() < 0) {
                    println("Cannot move down. Please try another move")
                }
                if (player.getYPos() == grid.size) {
                    println("Cannot move up. Please try another move")
                }
                println()
                player.position = prevPlayerLocation
                continue
            }

            for (monster in monsters) {
                if (player.checkSamePosition(monster)) {
                    println("You have encountered a monster!")
                    println("It has ${monster.hp} HP")
                    println("Type 'attack' to attack it until it dies.")
                    print(": ")
                    while (monster.hp > 0) {
                        var attack = readLine()
                        if (attack != "attack") {
                            while (attack != "attack") {
                                println("Type 'attack' to attack the monster")
                                print(": ")
                                attack = readLine()
                            }
                        }
                        player.attack(monster)
                        if (!monster.isDead()) {
                            println("You've dealt ${player.attackPower} dmg to the monster.")
                            println("It has ${monster.hp} HP left.")
                            println("Keep attacking the monster until it dies!")
                            print(": ")
                        }
                    }


                    monsters.remove(monster)
                    if (monsters.size == 0) {
                        println("Congrats! You have killed all the monsters and beat the game!")
                        exitProcess(0)
                    } else {
                        println("You have killed the monster! Only ${monsters.size} left to go.")
                    }
                    break


                }

            }



        } else {
            println("Please try again.")
            println()
        }
    }
}