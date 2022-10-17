package models

sealed trait Position {

}

case object GoalKeeper extends Position {
  override def toString: String = "Goalkeeper"
}

object RightFullback extends Position {
  override def toString: String = "Right Fullback"
}

object LeftFullback extends Position {
  override def toString: String = "Left Fullback"

}

object CenterBack extends Position {
  override def toString: String = "Center Back"

}

object Sweeper extends Position {
  override def toString: String = "Sweeper"

}

object Striker extends Position {
  override def toString: String = "Striker"

}

object HoldingMidfielder extends Position {
  override def toString: String = "Holding Midfielder"

}

object RightMidfielder extends Position {
  override def toString: String = "Right Midfielder"

}

object Central extends Position {
  override def toString: String = "Central"

}

object AttackingMidfielder extends Position {
  override def toString: String = "Attacking Midfielder"

}

object LeftMidfielder extends Position {
  override def toString: String = "Left Midfielder"

}

object Referee extends Position {
  override def toString: String = "Referee"

}