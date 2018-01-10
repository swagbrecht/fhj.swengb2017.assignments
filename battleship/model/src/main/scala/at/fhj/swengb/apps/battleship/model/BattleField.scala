package at.fhj.swengb.apps.battleship.model

import scala.util.Random

/**
  * Denotes the size of our region of interest
  */
case class BattleField(width: Int, height: Int, fleet: Fleet) {
  val Positions: Set[BattlePos] = (for {x <- 0 until width
                                        y <- 0 until height} yield BattlePos(x, y)).toSet
  val availablePos: Set[BattlePos] = Positions -- fleet.occupiedPositions

  /**
    * Adds vessel at a random, free position in the battlefield. if no position could be found,
    * returns the current battlefield without vessel added.
    *
    * @param Vessel3000 vessel to add
    * @return
    */
  def addAtRandomPosition(Vessel3000: Vessel): BattleField = {

    def Iterator3000(Placed: Boolean, Position: Set[BattlePos], Field: BattleField): BattleField = {
      if (Placed == false) {
        val p = Position.toSeq(Random.nextInt(Position.size))
        if (Vessel3000.copy(InitPosition = p).occupiedPos.subsetOf(availablePos)) {
          Iterator3000(true, Position - p, Field.copy(fleet = Field.fleet.copy(vessels = Field.fleet.vessels + Vessel3000.copy(InitPosition = p))))
        } else {
          Iterator3000(false, Position - p, Field)
        }
      } else if (Position.isEmpty) {
        println(s"Can't place ${Vessel3000.getClass.getSimpleName} on the battlefield!")
        Field
      } else {
        println(s"Placed ${Vessel3000.getClass.getSimpleName} on the battlefield!")
        Field
      }
    }
    Iterator3000(false, availablePos, this)

  }
}

object BattleField {
  def RandomPlacer3000(BattleField3000: BattleField): BattleField = {
    def Placer3000(CurrentBattleField: BattleField, Vessels: Set[Vessel]): BattleField = {
      if (Vessels.isEmpty) CurrentBattleField
      else {
        Placer3000(CurrentBattleField.addAtRandomPosition(Vessels.head), Vessels.tail)
      }
    }
    Placer3000(BattleField3000.copy(fleet = BattleField3000.fleet.copy(vessels = Set())), BattleField3000.fleet.vessels)
  }
}