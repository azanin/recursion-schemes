package recursionscheme

import scalaz.Functor

case class Attr[F[_], A](attribute: A, hole: F[Attr[F, A]])

object Attr {

  type CVAlgebra[F[_], A] = F[Attr[F, A]] => A //course-of-value algebra

  def histoUnefficient[F[_], A](cvAlgebra: CVAlgebra[F, A])(implicit f: Functor[F]): Term[F] => A = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out

    def fmap(worker: Term[F] => Attr[F, A]): F[Term[F]] => F[Attr[F, A]] =
      f.map(_)(worker)

    lazy val w: Term[F] => Attr[F, A] = { term =>
      val head: A = histo(cvAlgebra)(f)(term)
      lazy val hole: F[Attr[F, A]] = f.map(out(term))(w)
      Attr(head, hole)
    }

    out >>> fmap(w) >>> cvAlgebra
  }

  def histo[F[_], A](cvAlgebra: CVAlgebra[F, A])(implicit f: Functor[F]): Term[F] => A = { term =>
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out
    val id: F[Attr[F, A]] => F[Attr[F, A]] = attr => identity[F[Attr[F, A]]](attr)

    def fmap(w: => Term[F] => Attr[F, A]): F[Term[F]] => F[Attr[F, A]] ={
      f.map(_)(w)
    }

    val mkAttr = (t: (A, F[Attr[F, A]])) => Attr(t._1, t._2)
    lazy val worker: Term[F] => Attr[F, A] = out >>> fmap(worker) >>> (cvAlgebra &&& id) >>> mkAttr

    worker(term).attribute
  }
}