package week02

case class Position(x: Double, y: Double):
  def distanceTo(that: Position): Double =
    math.sqrt(Math.pow(that.x - x, 2.0) + math.pow(that.y - y, 2.0))

  def distanceToLine(line: (Position, Position)): Double =
    val top = math.abs( (line._2.x - line._1.x)*(line._1.y - y) - (line._1.x - x)*(line._2.y -line._2.y))
    top / line._1.distanceTo(line._2)

object Position
val player = Position(0, 1.80)
val hoop = Position(6.75,3.048)

case class Angle(radians: Double)
case class Speed(metersPerSecond: Double)

/*
Compute the successive positons of the ball over time.  Tha alogirithm terminats in two cases:
    * If the ball passes through the hoop, return true
    * If the ball end up too far (on the ground or beyond the hoop), return false

To check whether the ball pased throught the hoop, we compute the distance between the center of
the hoop and the straight segment made of the two successive positions of the ball.
 */
val isWinningShot: (Angle, Speed) => Boolean =  (angle: Angle, speed: Speed) =>
  import Position._
  val v0x = speed.metersPerSecond * math.cos(angle.radians)
  val v0y = speed.metersPerSecond * math.sin(angle.radians)
  val p0x = player.x
  val p0y = player.y
  val g = -9.81

  def goesThroughHoop(line:(Position, Position)): Boolean =
    hoop.distanceToLine(line) < 0.01

  def isNotTooFar(position: Position): Boolean =
    position.y > 0 && position.x <= hoop.x + 0.01

  def position(t: Double): Position =
    val x = p0x + v0x*t
    val y = p0y + v0y*t + (0.5 * g * t * t)
    Position(x, y)

  // Example 1 traditional loop
//  println(s"Initial speed: ($v0x, $v0y)")
//  var isWinning = false
//  var t = 0.0
//  var currentPosition = position(0)
//  while isNotTooFar(currentPosition) == true && isWinning == false do
//    t = t + 0.5
//    val nextPosition = position(t)
//    println(s"Current Position (${currentPosition.x}, ${currentPosition.y}) Next Position: (${nextPosition.x},${nextPosition.y})")
//    isWinning = goesThroughHoop((currentPosition, nextPosition))
//    currentPosition = nextPosition

  def loop(time: Double): Boolean =
    val currentPosition = position(time)
    if(isNotTooFar(currentPosition)) then
      val nextPosition = position(time + 1)
      val line = (currentPosition, nextPosition)
      goesThroughHoop(line) || loop(time+0.5)
    else
      false

  loop(0.0)
end isWinningShot

object Basketball extends App {
  println("Starting")

  val answers: Seq[((BigDecimal, BigDecimal),Boolean)] = for {
    angle <- (BigDecimal(20.toRadians) to BigDecimal(80.toRadians) by BigDecimal(0.5.toRadians))
    speed <- (BigDecimal(1.0) to BigDecimal(12.0) by BigDecimal(0.25))
  } yield ((angle, speed) -> isWinningShot(Angle(angle.toDouble), Speed(speed.toDouble)))

  println(s"Winning shots are ${answers.filter(_._2 == true)}")
}

