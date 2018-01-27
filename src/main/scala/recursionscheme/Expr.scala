package recursionscheme

import scalaz._

object Expr {

  sealed trait Expr[+A]

  case class Index[A](target: A, idx: A) extends Expr[A]

  case class Literal(intVal: Int) extends Expr[Nothing]

  case class Ident(name: String) extends Expr[Nothing]

  case class Unary[A](op: String, target: A) extends Expr[A]

  case class Binary[A](lhs: A, op: String, rhs: A) extends Expr[A]

  case class Call[A](func: A, args: Seq[A]) extends Expr[A]

  case class Paren[A](target: A) extends Expr[A]


  val exprFunctor = new Functor[Expr] {
    override def map[A, B](fa: Expr[A])(f: A => B): Expr[B] = fa match {
      case Index(x, y) => Index(f(x), f(y))
      case Literal(x) => Literal(x)
      case Ident(name) => Ident(name)
      case Unary(op, targer) => Unary(op, f(targer))
      case Binary(lhs, op, rhs) => Binary(f(lhs), op, f(rhs))
      case Call(func, args) => Call(f(func), args.map(f))
      case Paren(target) => Paren(f(target))
    }
  }


  val countNodes: Term.Algebra[Expr, Int] = {
    case Index(x, y) => x + y + 1
    case Literal(_) => 1
    case Ident(_) => 1
    case Unary(_, target) => target + 1
    case Binary(lhs, _, rhs) => lhs + rhs + 1
    case Call(func, args) => args.sum + func + 1
    case Paren(target) => target + 1
  }


  val prettyPrint: Term.Algebra[Expr, String] = {
    case Index(x, y) => s"$x[$y]"
    case Literal(l) => l.toString
    case Ident(i) => i
    case Unary(op, target) => s"$op $target"
    case Binary(lhs, op, rhs) => s"$lhs $op $rhs"
    case Call(func, args) => func + s"(${args.mkString(",")})"
    case Paren(target) => s"($target)"
  }

  /* def bottomUp(term: Term[Expr])(fn: Term[Expr] => Term[Expr])(functor: Functor[Expr]): Term[Expr] = {
     val expr: Expr[Term[Expr]] = term.out
     fn(Term(functor.map(expr)(t => bottomUp(t)(fn)(functor))))
   }*/
}


