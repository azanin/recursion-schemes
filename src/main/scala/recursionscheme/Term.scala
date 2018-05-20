package recursionscheme

import scalaz.Functor

case class Term[F[_]](out: F[Term[F]])

object Term {

  type Algebra[F[_], A] = F[A] => A

  type Coalgebra[A, F[_]] = A => F[A]

  type RAlgebra[F[_], A] = F[(Term[F], A)] => A

  type RAlgebra1[F[_], A] = Term[F] => F[A] => A

  type RCoalgebra[F[_], A] = A => F[Either[Term[F], A]]

  type ElgotCoAlgebra[F[_], A, B] = A => Either[B, F[A]]

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


  def ana[F[_], A](coAlgebra: Coalgebra[A, F])(implicit functor: Functor[F]): A => Term[F] = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val in: F[Term[F]] => Term[F] = Term(_)
    val fmap: F[A] => F[Term[F]] = {
      functor.map(_)(ana(coAlgebra)(functor))
    }

    in <<< fmap <<< coAlgebra
  }

  def para[F[_], A](rAlgebra: RAlgebra[F, A])(implicit functor: Functor[F]): Term[F] => A = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val out: Term[F] => F[Term[F]] = _.out

    //val fanout: Term[F] => (Term[F], A) = term => (term, para(rAlgebra)(functor)(term))

    val id: Term[F] => Term[F] = term => identity[Term[F]](term)
    val fanout: Term[F] => (Term[F], A) = id &&& para(rAlgebra)(functor)

    val fmap: F[Term[F]] => F[(Term[F], A)] = {
      functor.map(_)(fanout)
    }

    out >>> fmap >>> rAlgebra
  }

  def para1[F[_], A](rAlgebra: RAlgebra1[F, A])(implicit functor: Functor[F]): Term[F] => A = { term =>

    val out: Term[F] => F[Term[F]] = _.out

    val fanout: Term[F] => A = para1(rAlgebra)(functor)

    val fmap: F[Term[F]] => F[A] = {
      functor.map(_)(fanout)
    }

    (out andThen fmap andThen rAlgebra(term)) (term)
  }

  def cataViaPara[F[_], A](algebra: Algebra[F, A])(implicit functor: Functor[F]) = {
    val rAlgebra: Term[F] => F[A] => A = _ => algebra
    para1(rAlgebra)
  }

  def apo[F[_], A](rCoalgebra: RCoalgebra[F, A])(implicit functor: Functor[F]): A => Term[F] = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    val in: F[Term[F]] => Term[F] = Term(_)

    val fanin: Either[Term[F], A] => Term[F] = {
      case Left(v) => identity(v)
      case Right(v) => apo(rCoalgebra)(functor)(v)
    }

    val fmap: F[Either[Term[F], A]] => F[Term[F]] = {
      functor.map(_)(fanin)
    }

    in <<< fmap <<< rCoalgebra
  }

  def hylo[F[_], A, B](algebra: Algebra[F, B], coalgebra: Coalgebra[A, F])
                      (implicit functor: Functor[F]): A => B = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    ana(coalgebra) >>> cata(algebra)
  }

  def elgot[F[_], A, B](algebra: Algebra[F, B], elgotCoAlgebra: ElgotCoAlgebra[F, A, B])
                       (implicit functor: Functor[F]): A => B = {
    import scalaz.std.function._
    import scalaz.syntax.arrow._

    def |||[A, B, C](ba: B => A, ca: C => A): Either[B, C] => A = _.fold[A](ba, ca)

    def fmap(f: A => B): F[A] => F[B] = functor.map(_)(f)

    elgotCoAlgebra >>> |||(identity, fmap(elgot(algebra, elgotCoAlgebra)) >>> algebra)
  }
}
