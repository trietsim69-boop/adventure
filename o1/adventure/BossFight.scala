package o1.adventure

class BossFight(val name: String, val description: String, var health: Int, val defeatCondition: String):
  def reduceHealth(amount: Int): Unit =
    this.health = math.max(0, this.health - amount)
  def isDefeated: Boolean =
    this.health <= 0
  override def toString: String =
    s"$name: $description (Health: ${if this.health > 0 then this.health else "Defeated"})"
