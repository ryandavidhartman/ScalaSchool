/* Exercise 2.5.
Show that we can represent pairs of non-negative integers using only numbers and arithmetic operations if we represent
the pair a and b as the integer that is the product 2^a 3^b.
Give the corresponding definitions of the procedures cons, car, and cdr.
 */

def power(num: Int, exp: Int): Int = {
  @scala.annotation.tailrec
  def power_helper(acc:Int, e: Int): Int = if(e < 1) acc else power_helper(acc*num, e-1)
  power_helper(acc = 1,exp)
}

def log_int(num: Int, base: Int): Int = {

  @scala.annotation.tailrec
  def log_helper(acc:Int, n: Double): Int = if(n < base ) acc else log_helper(acc+1, n/base)
  log_helper(acc = 0, num)
}


@scala.annotation.tailrec
def remove_factors(num: Int, factor: Int): Int =
  if(num % factor != 0) num else remove_factors(num/factor, factor)

type Pair = Int

def cons(x: Int, y: Int): Pair = power(2,exp = x)*power(3,exp = y)

def car(z: Pair): Int = log_int(remove_factors(z, factor = 3), base = 2)

def cdr(z: Pair): Int = log_int(remove_factors(z, factor = 2), base = 3)

val pair = cons(10,7)
car(pair)
cdr(pair)

