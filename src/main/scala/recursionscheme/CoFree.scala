package recursionscheme

import scalaz.Functor

case class CoFree[F[_], A](attribute: A, hole: F[CoFree[F, A]])

object CoFree {

  type CVAlgebra[F[_], A] = F[CoFree[F, A]] => A //course-of-value algebra

  def histoUnefficient[F[_], A](cvAlgebra: CVAlgebra[F, A])(implicit f: Functor[F]): Term[F] => A = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out

    def fmap(worker: Term[F] => CoFree[F, A]): F[Term[F]] => F[CoFree[F, A]] =
      f.map(_)(worker)

    lazy val w: Term[F] => CoFree[F, A] = { term =>
      val head: A = histo(cvAlgebra)(f)(term)
      lazy val hole: F[CoFree[F, A]] = f.map(out(term))(w)
      CoFree(head, hole)
    }

    out >>> fmap(w) >>> cvAlgebra
  }

  def histo[F[_], A](cvAlgebra: CVAlgebra[F, A])(implicit f: Functor[F]): Term[F] => A = { term =>
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out
    val id: F[CoFree[F, A]] => F[CoFree[F, A]] = attr => identity[F[CoFree[F, A]]](attr)

    def fmap(w: => Term[F] => CoFree[F, A]): F[Term[F]] => F[CoFree[F, A]] = {
      f.map(_)(w)
    }

    val mkAttr = (t: (A, F[CoFree[F, A]])) => CoFree(t._1, t._2)
    lazy val worker: Term[F] => CoFree[F, A] = out >>> fmap(worker) >>> (cvAlgebra &&& id) >>> mkAttr

    worker(term).attribute
  }
}