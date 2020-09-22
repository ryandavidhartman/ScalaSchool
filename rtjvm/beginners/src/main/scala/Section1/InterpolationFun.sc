import java.time.LocalDate

// see https://ryandavidhartman.github.io/ScalaSchool/String-Interpolation.html

/*
  s interpolator
*/

val foo = "bar"
println(s"Hi $foo")

println(s"Sum math ${110*2+5}")

/*
  raw interpolator
*/

println(raw"No\\\\escape!")

/*
  f interpolator
*/

println(f"Pi is ${math.Pi}%.8f")
println(f"Pi is ${math.Pi}%06.3f")


/* Custom Interpolator
  Here we define custom string interpolator `date` so that you can define a
  java.time.LocalDate as data"$year-$month-$day"
*/


implicit class DateInterpolator(val sc: StringContext) extends AnyVal {
  def date(args: Any*): LocalDate = LocalDate.of(
    args(0).toString.toInt,
    args(1).toString.toInt,
    args(2).toString.toInt
  )
}

val year = 2020
val month = 9
val day = 22

val fancyDate: LocalDate = date"$year-$month-$day"

