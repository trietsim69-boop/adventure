package o1.adventure

/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect,
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as “go east” or “rest” */
class Action(input: String):

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim

  /** Causes the given player to take the action represented by this object, assuming
    * that the command was understood. Returns a description of what happened as a result
    * of the action (such as “You go west.”). The description is returned in an `Option`
    * wrapper; if the command was not recognized, `None` is returned. */
  def execute(actor: Player, area: Area): Option[String] =
    this.verb match
      case "go"        => Some(actor.go(this.modifiers))
      case "quit"      => Some(actor.quit())
      case "get"       => Some(actor.get(this.modifiers))
      case "examine"   => Some(actor.examine(this.modifiers))
      case "inventory" => Some(actor.inventory)
      case "help"      => Some(actor.help)
      case "destroy"   =>
        if this.modifiers == "tom riddle's diary" then
           Some(actor.handleDestroyCommand) // Call the new method for destroying the diary
        else
          Some(actor.destroyHorcrux(this.modifiers))
      case "use" =>
        if area.hasActiveBosses then
          Some(actor.attackBoss(this.modifiers))
        else
          Some("There are no bosses to fight here.")
      case other       =>
          area.challenge match
            case None => None
            case Some((_, _)) => Some(actor.solveChallenge(this.modifiers))

  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = s"$verb (modifiers: $modifiers)"

end Action

