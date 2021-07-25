case class Position(x: Double, y: Double):
  def distanceTo(that: Position): Double =
    Math.sqrt(Math.pow(that.x - x, 2.0) + Math.pow(that.y - y, 2.0))

  def distanceToLine(line: (Position, Position)): Double =
    val top = Math.abs( (line._2.x - line._1.x)*(line._1.y - y) - (line._1.x - x)*(line._2.y -line._2.y))
    top / line._1.distanceTo(line._2)

object Position
  val player = Position(0, 1.80)
  val hoop = Position(6.75,3.048)

case class Angle(radians: Double)
case class Speed(metersPerSecond: Double)

val isWinningShot: (Angle, Speed) => Boolean =  (angle: Angle, speed: Speed) =>
  true

println(isWinningShot(Angle(0.0), Speed(2)))

