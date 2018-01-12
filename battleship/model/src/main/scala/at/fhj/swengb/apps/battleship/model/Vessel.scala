package at.fhj.swengb.apps.battleship.model

/**
  * A vessel is the common denominator of all ships which are to be defined.
  *
  * Implementation shows that by providing constructors which create the datatype correctly
  * one can save much work verifying that our data invariants are correct. If you think about
  * it, you can proof already with the compiler that the types are correct, which leads to less
  * work with runtime tests - you need less unit tests!
  *
  * @param name each vessel has a (nonempty) name.
  *
  */
case class Vessel(name: NonEmptyString, InitPosition: BattlePos, direction: Direction, size: Int) {

  // parts a vessel consists of parts, they have to be connected either in x or in y direction
  final val occupiedPos: Set[BattlePos] =
    direction match {
      case Horizontal => (InitPosition.x until (InitPosition.x + size)).map(x => BattlePos(x, InitPosition.y)).toSet
      case Vertical => (InitPosition.y until (InitPosition.y + size)).map(y => BattlePos(InitPosition.x, y)).toSet
    }

}