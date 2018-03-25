# The Sage bird

## Talking birds forest

Somewhere there is a magic forest where thousand of talking birds are living.

Birds follow three talking rules:

* If you call out B to A bird, it will answer x where x is another forest's bird, so *AB = x*

* C1 condition. In the forest exist a Mocking bird so that *Mx = xx*.
M is called Mocking bird because it's answer to any x's bird is the same that x would give to itself.

* C2 condition. Given any two birds A and B, C is a bird so that *Cx = A(Bx)*.
C is the bird that call you back the composition bird of A and B given any x.


## Fond, normal and happy bird

### Fond and normal bird
It could happen that calling out B to A you get back B as answer.
In this case **A is fond to B** and so *AB = B*.

Given C1 and C2 conditions, every forest's bird is fond to another one at least...

Every fond bird is called normal too.

### Happy birds
Given two birds x and y, a bird is happy if *Ax = y* and *Ay = x*

In general, every fond bird (and normal too) is happy but not every happy bird is fond.
Infact, only if C2 hold and and an happy bird is in the forest then we can say that an happy bird is normal too.

## Larks

Given two birds x and y, a bird L is a Lark if *(Lx)y = x(yy)*

Lark is a very special bird, because its presence in the forest ensure that all the other birds are fond to at least another one.
This is a wonderful effect, infact every bird are normal too and happy as well!

To demonstrate this:
- Suppose there is a lark L and any x,y birds, so *(Lx)y = x(yy)*
- This is true for *y = Lx* as well, and so *(Lx)(Lx) = x((Lx)(Lx))*
- We can say then that **x is fond to (Lx)(Lx) and so normal as well**

## The sage bird

> *"Is there some special bird which, whenever I call out the name of a bird x to it, will respond by naming a bird of which x is fond?*

This bird is named "**sage bird**" or "**oracle bird**" and we can refer to it as &theta;

- Any bird x is fond to &theta; and so *x(&theta;x) = &theta;x*

## Is there a sage bird?

It's not possible to be sure about sage bird's existence by C1 and C2 only.

- Given a M and X bird we know that exist a bird that is the composition of X and M. *Cv = X(Mv)*
- And so we know that X is fond to yy. *(Yv = X(Mv) => YY = X(MY) => X(YY) = YY)*

But, **how can we find a y bird that is the composition of x and M?**

- We already talk about such bird.
- A bird A that answer back y bird that is the composition of x and M.
- Ax composing x to M is the same of *(Ax)y = x(My)* for any x,y. *(Cx = A(Bx) where C = Ax; x = y; A = x; B = M)*
- We know that *x(My) = x(yy)* and so *(Ax)y = x(yy)* => **A is a Lark**.

So, given that a M and L are present in the forest and C2 holds then we can say:

- Every x is fond to *(Lx)(Lx)* (thank you Lark!), and so x is fond to *M(Lx)* as well.
- From C2 we know that exist a &theta; bird composing M and L. *&theta;x = M(Lx)*
- Since x is fond to M(Lx) and *M(Lx) = &theta;x* then x is fond to &theta;x

**&theta; is the sage bird also knows as fixed point combinator**.

## Fixed Point

A practical example to understand what a function's fix point is, consist in repeatedly hit a calculator's cos key starting from 0.
- You will notice that the function converge to an x value. *cos(cos(cos(cos(cos(cos(x)))))) = x*

In general an f's fix point is where *x = f(x)*
- Notice that x could be a function as well and so *fixpoint-function = f(fixpoint-function)*

## Factorial function

```tut:silent
def factorial(n: Int): Int = n match {
    case 0 => 1
    case x => x * factorial(x - 1)
  }
```
## Puzzle

*Is it possible to create a non recursive factorial function? (avoid the use of imperative operators)*

```tut:silent
def factFLazy(f: => (Int => Int)): Int => Int = { n =>
    if (n == 0) 1
    else n * f(n - 1)
 }
```
- We just renamed recursive call to factorial with f that it's provided as an argument to *factFLazy*.
- *factFLazy* is an higher-order function, returning another function wich will be the factorial one.

## factorial via factFLazy

Given that we have a *factA* function able to compute factorials

```tut:silent
def factA(num: Int) : Int = factorial(num)
```
Let's consider *factB*

```tut:silent
val factB: Int => Int = factFLazy(factA)
```

*factb* computes factorials as well because depend on *factA* and *factFLazy*
```tut
factB(5)
```
Hence, given a factorial f function, factFLazy will return a f' that will compute factorials.
Assuming *f' = f* then we have:

```tut
val fact: Int => Int = factFLazy(fact)
fact(5)
```
## factorial via induction

Suppose we have

```tut:silent
val identity: Int => Int = { (x: Int) => x }
val fact0: Int => Int = factFLazy(identity)
```
*fact0* can compute just the factorial's number of 0

```tut
fact0(0)
fact0(1) // wrong
```

We can define *fact1* in terms of *fact0*

```tut
val fact1: Int => Int = factFLazy(fact0)
fact(1)
```
Hence, we can define factN in terms of factN-1 exploiting *factFLazy*

```tut
val fact2: Int => Int = factFLazy(fact1)
val fact3: Int => Int = factFLazy(fact2)
val fact4: Int => Int = factFLazy(fact3)
val fact5: Int => Int = factFLazy(fact4)

fact5(5)
```
*fact* function is the **fixed point** of *factFLazy*

```tut
val fixFact: Int => Int = factFLazy(fixFact)
```
## The Sage Bird again

Knowing that *factorial* is the *factFLazy*'s fixed point doesn't tell us how to compute it.
- We'd need a function that receives another function like *factFLazy* as argument and returns the *factFLazy*'s fixed point

> *Some special bird which, whenever I call out the name of a bird x to it, will respond by naming a bird of which x is fond*.

## Derivation

 - *&theta;f = fix-poinf-f*
 - *f(fix-point-f) = fix-point-f* by definition of fix point *(cos x = x)*
 - Hence *&theta;f = f(fix-point-f)* and so *&theta;f = f(&theta;f)*

## Y-Combinator in Scala

```tut:silent
def Y[T](f: (T => T) => (T => T)): T => T = f(Y(f))(_:T)

def factF(f: Int => Int): Int => Int = { n =>
  if (n == 0) 1
  else n * f(n - 1)
}

def factorial: Int => Int = Y[Int](factF)
```

```tut
factorial(5)
```

## Notes

*Y* as we have defined is not a combinator.
- *Y*'s body contains a free variable: *Y*

In lambda calculus *Y* is defined as: *Y = &lambda;f.(&lambda;x.f (x x)) (&lambda;x.f (x x))*
- Notations are equivalent but you can't implement the *Y* combinator in the same way of lambda calculus in languages like Haskell or Scala because of the type system.