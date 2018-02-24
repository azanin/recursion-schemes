package recursionscheme

import scalaz.Functor

case class Attr[F[_], A](attribute: A, hole: F[Attr[F, A]])

object Attr {

  type CVAlgebra[F[_], A] = F[Attr[F, A]] => A //course-of-value algebra
  
  def histo[F[_], A](cvAlgebra: CVAlgebra[F, A])(implicit f: Functor[F]): Term[F] => A = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out

    def fmap(worker: Term[F] => Attr[F, A]): F[Term[F]] => F[Attr[F, A]] =
      f.map(_)(worker)

    val w: Term[F] => Attr[F, A] = { term =>
      val head: A = histo(cvAlgebra)(f)(term)
      val hole: F[Attr[F, A]] = f.map(term.out)(w)
      Attr(head, hole)
    }

    out >>> fmap(w) >>> cvAlgebra
  }
}