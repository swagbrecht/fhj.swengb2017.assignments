package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Slider, TextArea}
import javafx.scene.layout.GridPane

import at.fhj.swengb.apps.battleship.BattleShipProtobuf
import at.fhj.swengb.apps.battleship.BattleShipProtocol._
import at.fhj.swengb.apps.battleship.model._

class BattleShipFxController extends Initializable {

  var game: BattleShipGame = _

  @FXML private var battleGroundGridPane: GridPane = _

  /**
    * A text area box to place the history of the game
    */
  @FXML private var log: TextArea = _

  @FXML private var slider: Slider = _

  @FXML
  def newGame(): Unit = initGame()

  @FXML
  def loadGame(): Unit = {
    val path = Paths.get("target/saved.game")
    val is = Files.newInputStream(path)
    val battleShipGame = BattleShipProtobuf.BattleShipGame.parseFrom(is)
    val game = BattleShipGame(getField(battleShipGame), getCellWidth, getCellHeight, appendLog, updateSliderState)
    game.moves = getMoves(battleShipGame)
    init(game)
    game.initMoves()
  }

  @FXML
  def saveGame(): Unit = {
    val path = Paths.get("target/saved.game")
    val os = Files.newOutputStream(path)
    convert(game).writeTo(os)
  }

  @FXML
  def onChangeSliderValue(): Unit = {
    init(game)
    game.initMoves(slider.getValue.toInt)
  }

  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()

  private def getCellHeight(y: Int): Double = battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double = battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  def appendLog(message: String): Unit = log.appendText(message + "\n")

  def updateSliderState(): Unit = {
    slider.setValue(game.moves.length)
  }

  /**
    * Create a new game.
    *
    * This means
    *
    * - resetting all cells to 'empty' state
    * - placing your ships at random on the battleground
    *
    */
  def init(game : BattleShipGame) : Unit = {
    battleGroundGridPane.getChildren.clear()
    for (c <- game.getCells) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }
    game.getCells().foreach(c => c.init)
    this.game = game
  }


  private def initGame(): Unit = {
    val game: BattleShipGame = createGame()
    init(game)
    appendLog("New game started.")
  }

  private def createGame(): BattleShipGame = {
    val field = BattleField(10, 10, Fleet(FleetConfig.Standard))

    val battleField: BattleField = BattleField.placeRandomly(field)

    BattleShipGame(battleField, getCellWidth, getCellHeight, appendLog, updateSliderState)
  }

}