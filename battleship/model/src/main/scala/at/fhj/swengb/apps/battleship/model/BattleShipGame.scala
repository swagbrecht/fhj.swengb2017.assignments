package at.fhj.swengb.apps.battleship.model

/**
  * Contains all information about a battleship game.
  */
case class BattleShipGame(battleField: BattleField,
                          log: String => Unit,
                          updateSlider: Int => Unit,
                          getCellWidth: Int => Double,
                          getCellHeight: Int => Double,
                          ) {

  /**
    * remembers which vessel was hit at which position
    * starts with the empty map, meaning that no vessel was hit yet.
    *
    **/
  var hits: Map[Vessel, Set[BattlePos]] = Map()

  /**
    * contains all vessels which are destroyed
    */
  var sunkShips: Set[Vessel] = Set()

  /**
    * Contains all already clicked positions.
    * Array keeps sorting...
    */
  var GameState: List[BattlePos] = List();

  /**
    * We don't ever change cells, they should be initialized only once.
    */
  private val cells: Seq[BattleFxCell] = for {
    x <- 0 until battleField.width
    y <- 0 until battleField.height
    pos = BattlePos(x, y)
  } yield {
    BattleFxCell(BattlePos(x, y),
      getCellWidth(x),
      getCellHeight(y),
      log,
      battleField.fleet.findByPos(pos),
      GameUpdater3000,
      ClickReader3000)
  }

  def CellReader3000(): Seq[BattleFxCell] = cells

  def ClickReader3000(pos: BattlePos): Unit = {
    GameState = pos :: GameState
    updateSlider(GameState.size)
  }

  def RebuildGame(pos: List[BattlePos]): Unit = {

    for (p <- pos) {
      val Cell: BattleFxCell = cells.filter(e => e.pos.equals(p)).head
      Cell.Clicker3000()
    }
  }

  def GameUpdater3000(vessel: Vessel, pos: BattlePos): Unit = {
    log(vessel.name.value + "was hit!")

    if (GameState.contains(vessel)) {

      val oldPos: Set[BattlePos] = hits(vessel)
      hits = hits.updated(vessel, oldPos + pos)
      hits(vessel).foreach(p => log(p.toString))

      if (oldPos.contains(pos)) {
        log("Area already hit!")
      }

      if (vessel.occupiedPos == hits(vessel)) {
        log(s"${vessel.name.value} annihilated!")
        sunkShips = sunkShips + vessel

        if (battleField.fleet.vessels == sunkShips) {
          log("GAME OVER!")
        }
      }

    } else {
      hits = hits.updated(vessel, Set(pos))
    }

  }

}