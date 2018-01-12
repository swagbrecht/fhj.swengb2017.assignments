package at.fhj.swengb.apps.battleship

import at.fhj.swengb.apps.battleship.model._
import org.scalacheck.Prop
import org.scalatest.WordSpecLike
import org.scalatest.prop.Checkers

class BattleShipProtocolSpec extends WordSpecLike {

  import at.fhj.swengb.apps.battleship.model.BattleshipGameGen._

  "BattleShipProtocol" should {
    "be deserializable" in {
      val battlefield = BattleField(10, 10, Fleet(FleetConfig.Standard))
      val expected = BattleShipGame(battlefield, x => (), x => (), (x => x.toDouble), (x => x.toDouble))
      expected.ClickReader3000(BattlePos(1,2))
      val actual = BattleShipProtocol.convert(BattleShipProtocol.convert(expected))
      assert(actual.battleField == expected.battleField)
      assert(actual.GameState == expected.GameState)
    }
  }
}