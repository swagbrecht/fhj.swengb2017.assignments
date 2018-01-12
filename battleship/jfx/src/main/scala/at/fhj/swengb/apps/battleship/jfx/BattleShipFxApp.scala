package at.fhj.swengb.apps.battleship.jfx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.{Failure, Success, Try}

object BattleShipFxApp {

  var FirstStage3000: Stage = _;
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[BattleShipFxApp], args: _*)
  }
}


class BattleShipFxApp extends Application {

  val triedRoot = Try(FXMLLoader.load[Parent](getClass.getResource("/at/fhj/swengb/apps/battleship/jfx/battleshipfx.fxml")))
  override def start(stage: Stage): Unit = {
    BattleShipFxApp.FirstStage3000 = stage
    triedRoot match {
      case Success(root) =>
        stage.setResizable(false)
        stage.setScene(new Scene(root))
        stage.setTitle("Battleship the Game")
        stage.show()
      case Failure(e) => e.printStackTrace()
    }
  }

}