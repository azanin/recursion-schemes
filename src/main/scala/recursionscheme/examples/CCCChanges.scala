package recursionscheme.examples

import recursionscheme.Attr.CVAlgebra
import recursionscheme.{Attr, Term}
import recursionscheme.examples.Nat.{Nat, Next, Zero}

import scala.language.postfixOps
import scalaz.Functor

object CCCChanges extends App {

  /**
    * The change-making problem is simple: given a monetary amount N,
    * and a set of denominations (penny, nickel, dime, &c.),
    * how many ways can you make change for N
    */

  type Cent = Int
  val coins: Seq[Cent] = Seq(50, 25, 10, 5, 1)

  implicit val natF: Functor[Nat] = Nat.natFunctor


  val cvAlgebra: CVAlgebra[Nat, Int] = {
    case Zero => 1
    case curr@Next(attr) =>
      val givenAmount: Cent = Nat.toInt(curr)
      val validCoins = coins.filter(_ <= givenAmount)
      val remaining = validCoins.map(givenAmount -)
      val (zeroes, toProcess) = remaining.partition(_ == 0)
      val results = toProcess.map(v => Nat.lookup[Int](attr)(v)).sum
      zeroes.length + results
  }

  def change(value: Cent): Int = {
    val function: Term[Nat] => Cent = Attr.histo(cvAlgebra)
    function(Nat.fromInt(value))
  }


  println(change(100))
}
