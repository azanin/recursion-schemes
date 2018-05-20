package recursionscheme

import recursionscheme.CoFree.CVAlgebra
import scalaz.Functor

sealed trait Free[F[_], A] //aka Free Monad

case class Continue[F[_], A](a: A) extends Free[F, A]

case class Suspend[F[_], A](m: F[Free[F, A]]) extends Free[F, A]

object Free {

  type CVCoalgebra[F[_], A] = A => F[Free[F, A]]

  def futu[F[_], A](cVCoalgebra: CVCoalgebra[F, A])(implicit f: Functor[F]): A => Term[F] = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val in: F[Term[F]] => Term[F] = Term(_)

    def fmap(w: => Free[F, A] => Term[F]): F[Free[F, A]] => F[Term[F]] = f.map(_)(w)

    lazy val worker: Free[F, A] => Term[F] = {
      case Continue(a) => futu(cVCoalgebra).apply(a)
      case Suspend(m) => in(fmap(worker).apply(m))
    }

    in <<< fmap(worker) <<< cVCoalgebra
  }

  def crono[F[_], A, B](cVCoalgebra: CVCoalgebra[F, A], cVAlgebra: CVAlgebra[F, B])
                    (implicit f: Functor[F]): A => B = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    futu(cVCoalgebra) >>> CoFree.histo(cVAlgebra)
  }
}
