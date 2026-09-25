package o1.adventure

import scala.collection.mutable.Map

/** The class `Area` represents locations in a text adventure game world. A game world
  * consists of areas. In general, an “area” can be pretty much anything: a room, a building,
  * an acre of forest, or something completely different. What different areas have in
  * common is that players can be located in them and that they can have exits leading to
  * other, neighboring areas. An area also has a name and a description.
  * @param name         the name of the area
  * @param description  a basic description of the area (typically not including information about items) */
class Area(var name: String, var description: String):

  private val neighbors = Map[String, Area]()
  private var items: Vector[Item] = Vector()
  var bosses: Vector[BossFight] = Vector()
  var containBoss: Boolean = false
  var challenge: Option[(String, String)] = None

  /** Returns the area that can be reached from this area by moving in the given direction. The result
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */

  def setChallenge(challengeDescription: String, correctAnswer: String): Unit =
    this.challenge = Some((challengeDescription, correctAnswer))

  def tryChallenge(answer: String, player: Player): String =
    if (this.name == "The Chamber of Secrets" && !player.has("Tom Riddle's Diary")) then
      "You cannot attempt the challenge here until you possess Tom Riddle's Diary."
    else
      this.challenge match
        case Some((_, correctAnswer)) =>
          if answer.trim.toLowerCase == correctAnswer.trim.toLowerCase then
            this.challenge = None
            "You have successfully completed the challenge!"
          else
            "That is not the correct answer. Try again."

  def neighbor(direction: String) =
      this.neighbors.get(direction)

  /** Adds an exit from this area to the given area. The neighboring area is reached by moving in
    * the specified direction from this area. */
  def setNeighbor(direction: String, neighbor: Area) =
    this.neighbors += direction -> neighbor

  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling
    * the `setNeighbor` method on each of the given direction–area pairs.
    * @param exits  contains pairs consisting of a direction and the neighboring area in that direction
    * @see [[setNeighbor]] */
  def setNeighbors(exits: Vector[(String, Area)]) =
    this.neighbors ++= exits

  def addItem(item: Item): Unit =
    this.items :+= item

  def contains(itemName: String): Boolean =
    this.items.exists(_.name == itemName)
  def removeItem(itemName: String): Option[Item] =
    val itemOpt = this.items.find(_.name == itemName)
    itemOpt match 
      case Some(item) =>
        this.items = this.items.filterNot(_.name == itemName) // Create a new Vector without the item
        Some(item)
      case None => None
  def removeBoss(bossName: String): Option[BossFight] =
    val bossOpt = this.bosses.find(_.name == bossName)
    bossOpt match
      case Some(boss) =>
        this.bosses = this.bosses.filterNot(_.name == bossName)
        this.containBoss = this.bosses.nonEmpty
        Some(boss)
      case None => None

  /** Returns a multi-line description of the area as a player sees it. This includes a basic
    * description of the area as well as information about exits and items. If there are no
    * items present, the return value has the form "DESCRIPTION\n\nExits available:
    * DIRECTIONS SEPARATED BY SPACES". If there are one or more items present, the return
    * value has the form "DESCRIPTION\nYou see here: ITEMS SEPARATED BY SPACES\n\nExits available:
    * DIRECTIONS SEPARATED BY SPACES". The items and directions are listed in an arbitrary order. */
  def fullDescription: String =
    if this.name == "The Chamber of Secrets" || this.name == "The Last Hocrux" || this.name == "Lord Voldemort" then
        if this.hasActiveBosses then 
          this.description + s"Behold ${bosses.map(_.description).mkString(" ")}. He has ${bosses.map(_.health).mkString(" ")} health"
        else
          val exitList = "\n\nExits available: " + neighbors.keys.mkString(" ")
          val itemList = if (items.nonEmpty) then "\nYou see here: " + items.map(_.name).mkString(" ") else ""
          s"${bosses.map(_.defeatCondition).mkString(" ")}" + itemList + exitList 
    else
      val itemList = if (items.nonEmpty) then "\nYou see here: " + items.map(_.name).mkString(" ") else ""
      val exitList = "\n\nExits available: " + neighbors.keys.mkString(" ")
      if this.name == "The Gryffindor Common Room" || this.name == "The Hufflepuff Common Room" || this.name == "The Ravenclaw Common Room" || this.name == "The Slytherin Common Room" then
        this.challenge match
          case None =>
            itemList + exitList
          case Some((challengeDescription, _)) => s"${this.description}\n$challengeDescription"
      else
        this.description + exitList + this.bosses.map(_.defeatCondition).mkString(" ")

  /** Returns a single-line description of the area for debugging purposes. */
  override def toString: String =
    this.name + ": " + this.description.replaceAll("\n", " ").take(150)

  def addBosses(boss: BossFight): Unit =
    this.bosses :+= boss
    this.containBoss = true

  def hasActiveBosses: Boolean =
    this.bosses.exists(!_.isDefeated)

end Area
