package at.fhj.swengb.apps.battleship

import at.fhj.swengb.apps.battleship.model._
import scala.collection.JavaConverters._

object BattleShipProtocol {

  def convert(g: BattleShipGame): BattleShipProtobuf.BattleShipGame = {
    val vessels = g.battleField.fleet.vessels.map(convert).asJava
    val fleet = BattleShipProtobuf.Fleet.newBuilder.addAllVessels(vessels)
    val moves = g.moves.map(convert).asJava
    val battleField = BattleShipProtobuf.BattleField.newBuilder
      .setWidth(g.battleField.width)
      .setHeight(g.battleField.height)
      .setFleet(fleet)

    BattleShipProtobuf.BattleShipGame.newBuilder.setBattleField(battleField).addAllMoves(moves).build
  }

  def convert(f: BattleShipProtobuf.Fleet): Fleet = Fleet(f.getVesselsList.asScala.map(convert).toSet)

  def convert(v: Vessel): BattleShipProtobuf.Vessel = BattleShipProtobuf.Vessel.newBuilder
    .setName(v.name.value)
    .setStartPos(convert(v.startPos))
    .setDirection(convert(v.direction))
    .setSize(v.size)
    .build

  def convert(v: BattleShipProtobuf.Vessel): Vessel = Vessel(NonEmptyString(v.getName),
                                                             convert(v.getStartPos),
                                                             convert(v.getDirection),
                                                             v.getSize)

  def convert(p: BattlePos): BattleShipProtobuf.BattlePos = BattleShipProtobuf.BattlePos.newBuilder
    .setX(p.x)
    .setY(p.y)
    .build

  def convert(p: BattleShipProtobuf.BattlePos): BattlePos = BattlePos(p.getX, p.getY)

  def convert(d: Direction): BattleShipProtobuf.Vessel.Direction = d match {
    case Vertical => BattleShipProtobuf.Vessel.Direction.VERTICAL
    case Horizontal => BattleShipProtobuf.Vessel.Direction.HORIZONTAL
  }

  def convert(d: BattleShipProtobuf.Vessel.Direction): Direction = d match {
    case BattleShipProtobuf.Vessel.Direction.VERTICAL => Vertical
    case BattleShipProtobuf.Vessel.Direction.HORIZONTAL => Horizontal
  }

  def getField(g: BattleShipProtobuf.BattleShipGame): BattleField = BattleField(g.getBattleField.getWidth,
                                                                                g.getBattleField.getHeight,
                                                                                convert(g.getBattleField.getFleet))

  def getMoves(g: BattleShipProtobuf.BattleShipGame): List[BattlePos] = g.getMovesList.asScala.map(convert).toList

}
