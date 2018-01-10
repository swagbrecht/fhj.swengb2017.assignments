package at.fhj.swengb.apps.battleship.jfx

import java.io.File
import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{TextArea, Slider, Label}
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter

import at.fhj.swengb.apps.battleship.model.{BattleField, BattleShipGame, Fleet, FleetConfig, BattlePos}
import at.fhj.swengb.apps.battleship.BattleShipProtocol

class BattleShipFxController extends Initializable {

  private var Game: BattleShipGame = _

  override def initialize(url: URL, rb: ResourceBundle): Unit = newGame()
  def LogAdder3000(text: String): Unit = log.appendText(text + "\n")
  def WidthReader3000(width: Int): Int = battleGroundGridPane.getColumnConstraints.get(width).getPrefWidth.toInt
  def HeightReader3000(height: Int): Int = battleGroundGridPane.getRowConstraints.get(height).getPrefHeight.toInt



  def Initiator3000(game: BattleShipGame, ClickChecker3000: List[BattlePos]): Unit = {
    Game = game
    battleGroundGridPane.getChildren.clear()
    for (cells <- game.CellReader3000()) {

      battleGroundGridPane.add(cells, cells.pos.x, cells.pos.y)
    }
    game.GameState = List()
    game.CellReader3000().foreach(c => c.init())
    game.RebuildGame(ClickChecker3000)
    SliderAdder3000(ClickChecker3000.size)
  }


  private def GameCreator3000(): BattleShipGame = {
    val Field = BattleField(10, 10, Fleet(FleetConfig.Standard))
    BattleShipGame(BattleField.RandomPlacer3000(Field), LogAdder3000, SliderAdder3000, WidthReader3000, HeightReader3000)
  }

  private def GameLoader3000(filePath: String): (BattleShipGame, List[BattlePos]) = {
    val LoadDestination = at.fhj.swengb.apps.battleship.BattleShipProtobuf.BattleShipGame
        .parseFrom(Files.newInputStream(Paths.get(filePath)))

    val Game = BattleShipGame(BattleShipProtocol.convert(LoadDestination).battleField,
      LogAdder3000,
      SliderAdder3000,
      WidthReader3000,
      HeightReader3000)

    Game.GameState = List()
    (Game, BattleShipProtocol.convert(LoadDestination).GameState.reverse)
  }

  def SliderAdder3000(Stack: Int): Unit = {
    SliderState.setMax(Stack)
    SliderState.setValue(Stack)
  }

  @FXML private var battleGroundGridPane: GridPane = _
  @FXML private var SliderState: Slider = _
  @FXML private var Title: Label = _

  @FXML private var log: TextArea = _

  //Creating a new game and resetting all states
  @FXML def newGame(): Unit = {
    log.setText("")
    LogAdder3000("A new game has started")
    Initiator3000(GameCreator3000(), List())
  }

  @FXML def saveGame(): Unit = {
      //Using FileChooser for accessing our files
      val FileChooser3000 = new FileChooser();
      //Filtering on our protobuf files with the ending .bin
      val ProtoFilter3000: FileChooser.ExtensionFilter = new ExtensionFilter("Protobuf files","*.bin")
      FileChooser3000.getExtensionFilters.add(ProtoFilter3000)
      //Converting and saving
      val FileSaver3000: File = FileChooser3000.showSaveDialog(BattleShipFxApp.FirstStage3000)
      BattleShipProtocol.convert(Game).writeTo(Files.newOutputStream(Paths.get(FileSaver3000.getAbsolutePath)))
      LogAdder3000("Saved Game")
  }


  @FXML def loadGame(): Unit = {
      val FileChooser3000 = new FileChooser();
      val ProtoFilter3000: FileChooser.ExtensionFilter = new ExtensionFilter("Protobuf files","*.bin")
      FileChooser3000.getExtensionFilters.add(ProtoFilter3000)
      val FileLoader3000: File = FileChooser3000.showOpenDialog(BattleShipFxApp.FirstStage3000)
      val (clickedPos, battleShipGame) = GameLoader3000(FileLoader3000.getAbsolutePath)
      //Resetting log
      log.setText("")
      Initiator3000(clickedPos, battleShipGame)
      LogAdder3000("Loaded Game")
  }

  @FXML def onSliderChanged(): Unit = {
    val TimeOnSlider = SliderState.getValue.toInt
    var PastChecker: Boolean = true
    val PastState: List[BattlePos] = Game.GameState.takeRight(TimeOnSlider).reverse

    if (TimeOnSlider != SliderState.getMax.toInt) {
      log.setText("")
      Title.setText("Reviewing Past")
      PastChecker = true
      LogAdder3000("Reviewing Past")
    } else {
      log.setText("")
      Title.setText("BATTLESHIP")
      PastChecker = false
      Game.GameState = List()
      LogAdder3000("Back to Present")
    }
    battleGroundGridPane.getChildren.clear()
    for (cells <- Game.CellReader3000()) {
      battleGroundGridPane.add(cells, cells.pos.x, cells.pos.y)
      cells.init()
      cells.setDisable(PastChecker) //PastChecker True if past -> cells deactivated
    }
    Game.RebuildGame(PastState) //Rebuilding the Game
  }


}