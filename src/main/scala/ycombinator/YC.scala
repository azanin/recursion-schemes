package ycombinator

object YC extends App {

  def factorial(n: Int): Int = if (n == 0) 1 else n * factorial(n - 1)

  def factFLazy(f: => (Int => Int)): Int => Int = n =>
    if (n == 0) 1
    else n * f(n - 1)

  def factF(f: Int => Int): Int => Int = n =>
    if (n == 0) 1
    else n * f(n - 1)

  def YC[T](f: (T => T) => (T => T)): T => T = f(YC(f))(_: T)

  val factorialF: Int => Int = factF(factorialF)

  val fact: Int => Int = YC(factF)


  println(factorialF(5))

}
