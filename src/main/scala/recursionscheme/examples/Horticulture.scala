package recursionscheme.examples

import recursionscheme.{Continue, Free, Suspend}
import recursionscheme.Free.CVCoalgebra

import scala.util.Random
import scalaz.Functor

object Horticulture extends App {

  sealed trait Plant[+A]

  case class Root[A](r: A) extends Plant[A]

  case class Stalk[A](s: A) extends Plant[A]

  case class Fork[A](l: A, c: A, r: A) extends Plant[A]

  case object Bloom extends Plant[Nothing]

  implicit val functor: Functor[Plant] = new Functor[Plant] {
    override def map[A, B](fa: Plant[A])(f: A => B): Plant[B] = fa match {
      case Root(r) => Root[B](f(r))
      case Stalk(s) => Stalk[B](f(s))
      case Fork(l, c, r) => Fork[B](f(l), f(c), f(r))
      case Bloom => Bloom
    }
  }

  /**
    *1. Plants begin at the ground.
    *2. Every plant has a maximum height of 10.
    *3. Plants choose randomly whether to fork, grow, or bloom.
    *4. Every fork will contain one immediate bloom and two further stems.
    */

  val cvCoalgebra: CVCoalgebra[Plant, Seed] = seed => {
    val (action, leftSeed, rightSeed) = seed.grow
    (action, seed.height) match {
      case (_, 0) => Root(Continue[Plant, Seed](leftSeed))
      case (_, 10) => Bloom
      case (Flower, _) => Bloom
      case (Upwards, _) => Stalk(Continue(rightSeed))
      case (Branch, _) => Fork(
        Suspend[Plant, Seed](Stalk(Continue[Plant, Seed](leftSeed))),
        Suspend[Plant, Seed](Bloom),
        Suspend[Plant, Seed](Stalk(Continue[Plant, Seed](rightSeed)))
      )
    }
  }

  sealed trait Action

  case object Flower extends Action

  case object Upwards extends Action

  case object Branch extends Action


  case class Seed(height: Int, rng: Random) {

    def grow: (Action, Seed, Seed) = {

      def randomAction = rng.nextInt(6) match {
        case 1 => Flower
        case 2 => Branch
        case _ => Upwards
      }

      (randomAction, copy(height = height + 1), copy(height = height + 1))
    }

  }

  val plant = Free.futu(cvCoalgebra).apply(Seed(0, Random))

  println(plant)
}
