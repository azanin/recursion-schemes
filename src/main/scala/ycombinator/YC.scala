package ycombinator

object YC extends App {

  def factorial(n: Int): Int = if (n == 0) 1 else n * factorial(n - 1)

  def factF(f: => (Int => Int)): Int => Int = n => if (n == 0) 1 else n * f(n - 1)

  val factorialF: Int => Int = factF(factorialF)

  println(factorialF(5))

}
