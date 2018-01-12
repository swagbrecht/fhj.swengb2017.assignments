package at.fhj.swengb.apps.battleship.model

import org.scalacheck.Gen

/**
  * Implement in the same manner like MazeGen from the lab, adapt it to requirements of BattleShip
  */

object BattleshipGameGen {

  //Generator to generate a random Battlefield including the Fleet
  val FieldGenerator3000: Gen[BattleField] = for {
    width <- Gen.chooseNum[Int](1, 10)
    height <- Gen.chooseNum[Int](1, 10)
    x <- Gen.chooseNum[Int](0, Seq(FleetConfig.OneShip, FleetConfig.TwoShips, FleetConfig.Standard).size - 1)
  } yield BattleField(width, height, Fleet(Seq(FleetConfig.OneShip, FleetConfig.TwoShips, FleetConfig.Standard)(x)))

  //Generator to generate a new instance of BattleShipGame
  val battleShipGameGen: Gen[BattleShipGame] = for {
    battlefield <- FieldGenerator3000
  } yield {
    //                          Log,     Slider,  CellWidth,       CellHeight
    BattleShipGame(battlefield, x => (), x => (), x => x.toDouble, x => x.toDouble)
  }
}