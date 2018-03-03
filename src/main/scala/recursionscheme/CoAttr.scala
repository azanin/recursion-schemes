package recursionscheme

import scalaz.Functor

sealed trait CoAttr[F[_], A] //aka Free Monad

case class Automatic[F[_], A](a: A) extends CoAttr[F, A] //this is Point of Free Monad

case class Manual[F[_], A](m: F[CoAttr[F, A]]) extends CoAttr[F, A] //this is join of Free Monad

object CoAttr {

  type CVCoalgebra[F[_], A] = A => F[CoAttr[F, A]]

  def futu[F[_], A](cVCoalgebra: CVCoalgebra[F, A])(implicit f: Functor[F]): A => Term[F] = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val in: F[Term[F]] => Term[F] = Term(_)

    def fmap(w: => CoAttr[F, A] => Term[F]): F[CoAttr[F, A]] => F[Term[F]] = f.map(_)(w)

    lazy val worker: CoAttr[F, A] => Term[F] = {
      case Automatic(a) => futu(cVCoalgebra).apply(a)
      case Manual(m) => in(fmap(worker).apply(m))
    }

    in <<< fmap(worker) <<< cVCoalgebra
  }
}
