package ycombinator

object YCombinator {

  def factorial(n: Int): Int = n match {
    case 0 => 1
    case x => x * factorial(x - 1)
  }

  def almostFactorial(f: Int => Int): Int => Int = {
    case 0 => 1
    case x@n => x * f(n - 1)
  }


  def lazyAlmostFactorial(f: => Int => Int): Int => Int = {
    case 0 => 1
    case x@n => x * f(n - 1)
  }


  val factorialA: Int => Int = lazyAlmostFactorial(factorialA)

  val factorialB: Int => Int = almostFactorial(factorialA)

  /*
  {
    case 0 => 1
    case x@n => x * factorialA(n - 1)
  }
   */


  //Strict version

  val identity: Int => Int = { (x: Int) => x }
  val factorial0: Int => Int = almostFactorial(identity)
  val factorial1: Int => Int = almostFactorial(factorial0)
  val factorial2: Int => Int = almostFactorial(factorial1)
  val factorial3: Int => Int = almostFactorial(factorial2)
  val factorial4: Int => Int = almostFactorial(factorial3)
  val factorial5: Int => Int = almostFactorial(factorial4)
  val factorial6: Int => Int = almostFactorial(factorial5)
  val factorial7: Int => Int = almostFactorial(factorial6)
  val factorial8: Int => Int = almostFactorial(factorial7) //almostFactorial(almostFactorial(almostFactorial(almostFactorial(identity)))


  //Fix Point
  //fixpoint-function = (almost-factorial fixpoint-function)

  //val fixPointFunction = almostFactorial(fixPointFunction)


  //Y Lazy version
  // Y(f) = fix-point-f = f(fix-point-f)  Y(cos) = x = cos(x)
  // Y(f) = f(Y(f))

  def Y(f: (Int => Int) => (Int => Int)): Int => Int = f(Y(f))

  def YC[T](f: (T => T) => (T => T)): T => T = f(YC(f))

  def fact: Int => Int = YC[Int](almostFactorial)

  /*
  1. It will only work in a lazy language (see below).
  2. It is not a combinator, because the Y in the body of the definition is a free variable which is only bound once
  the definition is complete.
  In other words, we couldn't just take the body of this version of Y and plop it in wherever we needed it,
  because it requires that the name Y be defined somewhere.
   */


  //Deriving the Y combinator


  def partfactorial(self: Int => Int)(n: Int) = n match {
    case 0 => 1
    case x => x * self(self(x - 1))
  }

}
