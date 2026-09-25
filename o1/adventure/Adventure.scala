package o1.adventure

class Adventure:

  /** the name of the game */
  val title = "Hogwarts Adventure"

  private val start                 = Area("Main Gate", "You are at the main gate of Hogwarts. Walk in and start your wizard adventure")
  private val MainHall              = Area("Main Hall", "You are in the main hall. \nUnfortunately, a wizard who shall not be named has taken over Hogwarts.\nYour mission is to go DEFEAT HIM and SAVE HOGWARTS!\nTo do this, you will have to find all the Hocruxes and destroy them before fighting him in the end")
  private val GryffindorCommonRoom  = Area("The Gryffindor Common Room", "You are in the Gryffindor Common Room.\nHere, you meet your friends Ron and Hermione.\nThey was wondering if you are a true Griffindor and ask you a question.")
  private val SlytherinCommonRoom   = Area("The Slytherin Common Room","You are in the Slytherin Common Room.\nThere is something on the ground")
  private val HufflepuffCommonRoom  = Area("The Hufflepuff Common Room","You are in the Hufflepuff Common Room.\nThere are cups on the floor.\nA LOT.\nFind the right one and destroy it")
  private val RavenClawCommonRoom   = Area("The Ravenclaw Common Room","You are in the Ravenclaw Common Room.\nHere you find your friend Luna Lovegood.\n'Follow me! I will lead you to the next Hocrux, if only you can get it from her!'She said")
  private val ChamberOfSecrets      = Area("The Chamber of Secrets","Wow. Didn't expect you to go this far.\nThe diary has led you here.\nJust a reminder. THERE'S A GIANT SNAKE.\nKILL HIM!!!")
  private val TheLastHocrux         = Area("The Last Hocrux", "Here he is. Voldemort's favourite snake. \nKill him and Voldemort will have no extra life!")
  private val FinalBoss             = Area("Lord Voldemort","You finally made it here. Kill him and save Hogwarts!!!")

  start.setNeighbors(Vector("north" -> MainHall))
  MainHall.setNeighbors(Vector("north" -> GryffindorCommonRoom))
  GryffindorCommonRoom.setNeighbors(Vector("north" -> SlytherinCommonRoom, "east" -> HufflepuffCommonRoom, "west" -> RavenClawCommonRoom))
  RavenClawCommonRoom.setNeighbors(Vector("east" -> GryffindorCommonRoom))
  HufflepuffCommonRoom.setNeighbors(Vector("west" -> GryffindorCommonRoom))
  SlytherinCommonRoom.setNeighbors(Vector("secretpath" -> ChamberOfSecrets, "south" -> GryffindorCommonRoom))
  ChamberOfSecrets.setNeighbors(Vector("slytherin" -> SlytherinCommonRoom, "outside" -> TheLastHocrux))
  TheLastHocrux.setNeighbors(Vector("the top of the castle" -> FinalBoss))
  FinalBoss.setNeighbors(Vector("gryffindor" -> GryffindorCommonRoom, "slytherin" -> SlytherinCommonRoom, "huffepuff" -> HufflepuffCommonRoom, "ravenclaw" -> RavenClawCommonRoom))


  private val sword = Item("the sword of gryffindor", "Hold on to this. It will be useful later on", isHorcrux = false)
  private val cup = Item("the hufflepuff's cup", "You found the real cup at last. This is a Hocrux\nDestroy it!!!", isHorcrux = true)
  private val diadem = Item("the ravenclaw's diadem", " 'Wit beyong measure is man's greatest treasure'\nNevermind. This is another Hocrux. DESTROYYY!!!", isHorcrux = true)
  private val diary = Item ("tom riddle's diary", "Tom Marvolo Riddle.\n Try rearranging the letter.(It's Lord Voldermort...)\nFor now, you do not possess the power to destroy it.\nGo through the secret path, kill the snake and get its venom.\nOnly then can you destroy it", isHorcrux = true)
  private val venom = Item ("the basilisk venom", "The Basilisk drop a fang. It's almighty power is enough to destroy the diary.\nDESTROYY!!", isHorcrux = false)

  GryffindorCommonRoom.addItem(sword)
  HufflepuffCommonRoom.addItem(cup)
  RavenClawCommonRoom.addItem(diadem)
  SlytherinCommonRoom.addItem(diary)
  ChamberOfSecrets.addItem(venom)

  GryffindorCommonRoom.setChallenge("Who founded the house of Gryffindor?(A reminder: A true wizard most important quality is his etiquettes.\nTherefore, starting from now, every question you answer must include 'sir,' at the beginning.\ni.e: sir, dumbledore)", "godric gryffindor")
  RavenClawCommonRoom.setChallenge("You see a ghost wearing a diadem.\nLuna: That's the Hocrux Harry! I just don't know how to get it from her.\nFortunately for you, the ghost saw you and ran away, dropping the diadem.\nType 'steal' and steal the Hocrux(even a thief has manners so still remember to include 'sir'.", "steal")
  HufflepuffCommonRoom.setChallenge("This is all based on luck. There are 5 cups on the floor, try to find the correct one. \nChoose a number between 1 and 5", "3")
  SlytherinCommonRoom.setChallenge("Only a true heir of Slytherin can proceed. Speak the phrase.(still remember to include 'sir,' in the beggining.", "open")

  val voldemort = BossFight(name = "Voldemort", description = "The Dark Lord himself, master of dark magic", health = 100, defeatCondition = "You have restored Hogwarts to peace!")
  val nagini = BossFight(name = "Nagini", description = "Voldemort's loyal snake and a living Horcrux", health = 50, defeatCondition = "Nagini is destroyed, the last Hocrux is GONE!")
  val basilik = BossFight(name = "The Basilisk", description = "A giant serpent lurking in the Chamber of Secrets", health = 75, defeatCondition = "You used the Sword of Gryffindor to slay the beast!")

  ChamberOfSecrets.addBosses(basilik)
  TheLastHocrux.addBosses(nagini)
  FinalBoss.addBosses(voldemort)
  private val horcruxes = Vector(cup, diadem, diary)
  private val bosses = Vector(voldemort, nagini, basilik)
  val player = Player(start, horcruxes, bosses)

  def isComplete =
    this.player.areHorcruxesDestroyed && this.voldemort.isDefeated

  def isOver = this.isComplete || this.player.hasQuit

  def welcomeMessage = "Another year in Hogwarts has come. Will you emerge as the hero who saves us all, or will the darkness prevail?"

  def goodbyeMessage =
    if this.isComplete then
      "You save Hogwarts! You are crowned the greatest wizard of all time!!!"
    else if this.voldemort.isDefeated && !this.player.areHorcruxesDestroyed then
      "There are Horcruxes that have not been destroyed. Come back to the rooms and destroy them before Voldemort respawn!!!"
    else
      "Quitter!"

  def playTurn(command: String): String =
    val action = Action(command)
    val outcomeReport = action.execute(this.player, this.player.location)
    outcomeReport.getOrElse(s"""Unknown command: "$command".""")

end Adventure
