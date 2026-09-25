package o1.adventure

import scala.collection.mutable.Map

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingArea  the player’s initial location */

class Player(startingArea: Area, initialHorcruxes: Vector[Item], bosses: Vector[BossFight]):

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private val inventory1 = Map[String, Item]()
  private var horcruxes: Vector[Item] = initialHorcruxes
  private val boss: Vector[BossFight] = bosses

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the player’s current location. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) =
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if destination.isDefined then s"You go $direction." else s"You can't go $direction."

  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    ""

  def get(itemName: String): String =
    this.location.removeItem(itemName) match
      case Some(item) =>
        this.inventory1(itemName) = item
        s"You pick up the $itemName."
      case None =>
        s"There is no $itemName here to pick up."

  def examine(itemName: String): String =
    this.inventory1.get(itemName) match
      case Some(item) =>
        s"You look closely at the $itemName.\n${item.description}"
      case None =>
        "If you want to examine something, you need to pick it up first."

  def inventory: String =
    if this.inventory1.isEmpty then
      "You are empty-handed."
    else
      "You are carrying:\n" + this.inventory1.keys.mkString("\n")

  def has(itemName: String): Boolean =
    this.inventory1.contains(itemName)
  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

  def destroyHorcrux(itemName: String): String =
    val horcruxOption = this.horcruxes.find(_.name == itemName)
    horcruxOption match
      case Some(horcrux) if itemName == "tom riddle's diary" =>
        if this.inventory1.contains("the basilisk venom") then
          s"You can now use the basilisk venom to destroy the diary"
        else
          "You cannot destroy the diary without the basilisk venom."
      case Some(horcrux) =>
        this.horcruxes = this.horcruxes.filterNot(_ == horcrux)
        this.inventory1.remove(itemName)
        s"You destroyed the Horcrux: $itemName!"
      case None =>
        s"$itemName is not one of the Horcruxes or has already been destroyed."

  def areHorcruxesDestroyed: Boolean =
    this.horcruxes.isEmpty


  def attackBoss(attackType: String): String =
    if boss.isEmpty then
      "There are no bosses to fight here."
    else
      val activeBossOption = this.currentLocation.bosses.find(!_.isDefeated)
      activeBossOption match
        case Some(activeBoss) =>
          attackType.trim.toLowerCase match
            case "expelliarmus" =>
              activeBoss.reduceHealth(50)
              if activeBoss.isDefeated then
                finalizeBossDefeat(activeBoss)
              else
                s"You use Expelliarmus! It dealt 50 damage. ${activeBoss.name} has ${activeBoss.health} health remaining."

            case "the sword" =>
              if inventory1.contains("the sword of gryffindor") then
                if activeBoss.name == "The Basilisk" || activeBoss.name == "Nagini" then
                  activeBoss.reduceHealth(activeBoss.health) // One-shot
                  finalizeBossDefeat(activeBoss)
                else
                  activeBoss.reduceHealth(20)
                  if activeBoss.isDefeated then
                    finalizeBossDefeat(activeBoss)
                  else
                    s"You use the Sword of Gryffindor! It dealt 20 damage. ${activeBoss.name} has ${activeBoss.health} health remaining."
              else
                "You do not have the sword of gryffindor to use"

            case "fireball" =>
              activeBoss.reduceHealth(30)
              if activeBoss.isDefeated then
                finalizeBossDefeat(activeBoss)
              else
                s"You cast Fireball! It dealt 30 damage. ${activeBoss.name} has ${activeBoss.health} health remaining."

            case "avada kedavra" =>
              "You failed as a wizard. Using the Unforgivable Curse costs your life. Try again.(you have infinite lives)"

            case _ =>
              "That attack type is not recognized! Try a valid attack."

        case None =>
          "All bosses in this area have already been defeated."
  private def finalizeBossDefeat(defeatedBoss: BossFight): String =
    this.currentLocation.removeBoss(defeatedBoss.name)
    this.currentLocation.containBoss = this.currentLocation.bosses.nonEmpty
    s"${defeatedBoss.name} has been defeated!"

  def handleDestroyCommand: String =
    if this.inventory1.contains("the basilisk venom") && this.inventory1.contains("tom riddle's diary") then
      this.inventory1.remove("the basilisk venom")
      this.inventory1.remove("tom riddle's diary")
      this.horcruxes = this.horcruxes.filterNot(_.name == "tom riddle's diary") // Remove the diary from Horcruxes
      "You have destroyed 'Tom Riddle's Diary' with the basilisk venom!"
    else
      "You cannot destroy the diary. Make sure you have both the diary and basilisk venom."

  def solveChallenge(answer: String): String =
    this.location.tryChallenge(answer, this)

  def help: String =
    "Your goal for this game is to destroy all the Hocruxes and kill all the bosses to save Hogwarts.\n The list of commands you can use for this game are:\n'go' - to head to some given direction\n'get' - to pickup an item, use this before writing exactly the item name you want to pickup.\n'examine' - to see the description of the items you have, use this before writing the item name.\n'destroy' - use to destroy hocruxes and only hocruxes, write before an item name.\n'inventory' - to check what items do you have.\n'use' - this is for fighting boss, type use before the spell or attack you want to use.\n'quit' - skill issue.\nThere are also challenges in some areas and when you answer the question, you must include 'sir' before each answer.i.e: sir, o1"



end Player

