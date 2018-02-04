
package recursionscheme

import recursionscheme.Expr.{Call, Expr, Ident, Literal, Unary}

import scalaz.Functor

object Main extends App {


  val ten = Term[Expr](Literal(10))
  val five = Term[Expr](Literal(5))
  val add = Term[Expr](Ident("add"))
  val call = Term[Expr](Call(func = add, Seq(ten, five)))

  val unary = Term[Expr](Unary("call", call))

  val transformLiterals: Term[Expr] => Term[Expr] = {
    term => term.out match {
      case Literal(10) => Term[Expr](Literal(5))
      case Ident(name) => Term[Expr](Ident(name.toUpperCase))
      case _ => term
    }
  }
  implicit val functor: Functor[Expr] = Expr.exprFunctor

  println(call.out)
  val bottomUp = Term.bottomUp[Expr](transformLiterals)
  println(bottomUp(unary).out)

  val countNodes: Term[Expr] => Int = Term.cata(Expr.countNodes)
  println(countNodes(unary))

  val prettyPrint: Term[Expr] => String = Term.cata(Expr.prettyPrint)
  println(prettyPrint(unary))



}
