package at.fhj.swengb.apps.battleship.model

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle;

/**
  * Represents one part of a vessel or one part of the ocean.
  */
case class BattleFxCell(pos: BattlePos,
                        width: Double,
                        height: Double,
                        log: String => Unit,
                        someVessel: Option[Vessel] = None,
                        fn: (Vessel, BattlePos) => Unit,
                        upClickedPos: BattlePos => Unit)
  extends Rectangle(width, height) {

  def init(): Unit = {
    setFill(Color rgb(50,50,50,1))
  }

/* mark ships for testing purposes
  def init(): Unit = {
    if (someVessel.isDefined) {
      setFill(Color.YELLOWGREEN)
    } else {
      setFill(Color.BLUE)
    }
  }
*/

  setOnMouseClicked(e => {
    if(!isDisable)
      upClickedPos(pos)
    someVessel match {
      case None =>
        log(s"Just hit water!")
        setFill(Color rgb(202,202,202,1))
      case Some(v) =>
        fn(v, pos)
        setFill(Color rgb(118,184,42,1))
    }
  })

  def Clicker3000() = {
    if(!isDisable)
      upClickedPos(pos)
    someVessel match {
      case None =>
        log(s"Just hit water!")
        setFill(Color rgb(202,202,202,1))
      case Some(v) =>
        fn(v, pos)
        setFill(Color rgb(118,184,42,1))
    }
  }

}