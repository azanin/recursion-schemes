package recursionscheme.examples

import recursionscheme.{Attr, Term}

import scalaz.Functor

object Nat {

  sealed trait Nat[+A]

  case object Zero extends Nat[Nothing]

  case class Next[A](v: A) extends Nat[A]

  val natFunctor: Functor[Nat] = new Functor[Nat] {
    override def map[A, B](fa: Nat[A])(f: A => B): Nat[B] = fa match {
      case n: Next[A] => Next(f(n.v))
      case _ => Zero
    }
  }

  def fromInt(value: Int): Term[Nat] = value match {
    case 0 => Term[Nat](Zero)
    case n: Int => Term[Nat](Next(fromInt(n-1)))
  }

  def toInt(nat: Nat[Attr[Nat, Int]]): Int = nat match {
    case Zero => 0
    case Next(Attr(_, x)) => 1 + toInt(x)
  }

  def lookup[A]: Attr[Nat, A] => Int => A = att => {
    case 0 => att.attribute
    case n =>
      val hole: Attr[Nat, A] = att.hole match {
        case Next(a) => a
        case _ => throw new RuntimeException("Unconceavable")
      }
      lookup(hole)(n - 1)
  }

}
