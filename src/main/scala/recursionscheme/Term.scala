package recursionscheme

import scalaz.Functor

case class Term[F[_]](out: F[Term[F]])

object Term {

  type Algebra[F[_], A] = F[A] => A

  def bottomUp[F[_]](fn: Term[F] => Term[F])(implicit functor: Functor[F]): Term[F] => Term[F] = { term =>
    val expr: F[Term[F]] = term.out
    fn(Term(functor.map(expr)(bottomUp(fn)(functor))))
  }


  def bottomUpArrow[F[_]](fn: Term[F] => Term[F])(implicit functor: Functor[F]): Term[F] => Term[F] = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out
    val fmap: F[Term[F]] => F[Term[F]] = {
      functor.map(_)(bottomUpArrow(fn)(functor))
    }

    val in: F[Term[F]] => Term[F] = Term(_)

    out >>> fmap >>> in >>> fn
  }

  def bottomViaCata[F[_]](fn: Term[F] => Term[F])(implicit functor: Functor[F]): Term[F] => Term[F] = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val in: F[Term[F]] => Term[F] = Term(_)

    cata(in >>> fn)
  }


  def topDown[F[_]](fn: Term[F] => Term[F])(implicit functor: Functor[F]): Term[F] => Term[F] = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out
    val fmap: F[Term[F]] => F[Term[F]] = {
      functor.map(_)(bottomUpArrow(fn)(functor))
    }

    val in: F[Term[F]] => Term[F] = Term(_)

    in <<< fmap <<< out <<< fn
  }

  /*
    def mistery[F[_], A](fn: F[A] => A)(functor: Functor[F]): Term[F] => A = {
      import scalaz.std.function._
      import scalaz.syntax.arrow._

      val out: Term[F] => F[Term[F]] = _.out
      val fmap: F[Term[F]] => F[A] = {
        functor.map(_)(mistery(fn)(functor))
      }

      out >>> fmap >>> fn
    }*/


  def cata[F[_], A](algebra: Algebra[F, A])(implicit functor: Functor[F]): Term[F] => A = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out
    val fmap: F[Term[F]] => F[A] = {
      functor.map(_)(cata(algebra)(functor))
    }

    out >>> fmap >>> algebra
  }
}

