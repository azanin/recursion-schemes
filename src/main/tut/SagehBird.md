# The Sage bird
aka Fix Point Combinator

## La foresta degli uccelli parlanti

Si supponga una foresta abitata da uccelli parlanti.

Le regole base che regolano il canto di questi sono le seguenti:

* Se si urla B all' uccello A questo risponderà x dove x è un qualsiasi uccello della foresta quindi AB = x

* Condizione C1. Nella foresta è presente un uccello Mock per cui Mx = xx. M è chiamato uccello Mock poichè la sua risposta a qualsiasi
uccello x è la stessa che x avrebbe dato a se stesso.

* Condizione C2. Dati due uccelli qualsiasi A e B, esiste un uccello C tale che Cx = A(Bx).
In altre parole, per ogni coppia di uccelli A e B ne esiste un terzo C che è la composizione dei due.


## Gli uccelli innamorati, felici e normali

Nella foresta potrebbe accadere che gridando il nome di un uccello B ad un uccello A quest'ultimo risponda B.
In questo caso A è innamorato di B e quindi che AB = B.

Si dice che nella foresta, date le condizioni C1 e C2, ogni uccello sia innamorato di almeno un altro uccello...

Ogni uccello innamorato di almeno un altro uccello viene anche detto normale.

Un uccello, invece, è felice se dati due uccelli x e y, Ax = y ed Ay = x.

In generale ogni uccello che è innamorato (e quindi normale) è anche felice ma il viceversa non è generalmente vero.
Infatti, solamente se la condizione C1 tiene e vi è un uccello felice nella foresta che allora è presente almeno un uccello normale.

## Allodola

> "La lòdola perduta nell'aurora si spazia, e di lassù canta alla villa, che un fil di fumo qua e là vapora." Giovanni Pascoli.

Un'allodola è un uccello per il quale dati due uccelli x ed y allora (Lx)y = x(yy).

Questa specie di uccello ha un'importante proprietà,
infatti la sua sola presenza nella foresta garantisce che tutti gli uccelli siano innamorati di almeno un altro uccello.
Questo ha un effetto meraviglioso sulla foresta poichè significa che tutti gli uccelli sono normali e quindi felici!

La dimostrazione di ciò è abbastanza semplice:
- Si supponga che L sia un'allodola e x ed y due uccelli qualsiasi, ne consegue che (Lx)y = x(yy)
- Questo è vero anche se y = Lx, che quindi implica che (Lx)(Lx) = x((Lx)(Lx))
- Ovviamente è anche vero che x((Lx)(Lx)) = (Lx)(Lx) da cui ne consegue che x è innamorato di (Lx)(Lx)
- Quindi x è normale

## L'uccello saggio

> C'è un uccello speciale nella foresta che, se interrogato con il nome di un uccello x risponda nominando un uccello y del quale x è innamorato?

Questo tipo di uccello viene chiamato "uccello saggio" o "uccello oracolo" ed è denotato dal simbolo &theta;

- Un uccello &theta; gode della proprietà per cui dato un qualsiasi uccello x, esso è innamorato di &theta;x
- x(&theta;x) = &theta;x

## Esistenza dell'uccello saggio

Date le sole condizioni C1 e C2 non è possibile dimostrare l'esistenza dell'uccello saggio nella foresta.

- Sappiamo che esiste un uccello Mock M e sappiamo pure che dato un uccello x ne esiste uno y che compone con x con M. (Cx = A(Bx))
- Sappiamo anche che, dunque, x è innamorato di yy. (Yv = X(Mv) => YY = X(MY) => X(YY) = YY)

Ma dato un un uccello x come possiamo trovare un uccello y che compone x ed M?

- In realtà esiste un uccello del genere.
- Un uccello A che che dato un qualsiasi uccello x risponda nominando un uccello y che componga x ad M.
- Ax che compone x ad M è equivalente a dire che per ogni uccello x ed y (Ax)y = x(My) (Cx = A(Bx) dove C = Ax; x = y; A = x; B = M)
- Ma x(My) = x(yy) e quindi (Ax)y = x(yy) per ogni x ed y e quindi A è un'Allodola

Supponiamo dunque che nella foresta sia presente un uccello Mock, un'Allodola e che valga la regola di composizione C2.

- Sappiamo che ogni uccello x è innamorato di (Lx)(Lx) (grazie Allodola!), quindi x è innamorato anche di M(Lx).
- Dalla condizione di composizione sappiamo che esiste un uccello &theta; che compone M con L. &theta;x = M(Lx)
- Dato che x è innamorato di M(Lx) e M(Lx) = &theta;x allora x è innamorato di &theta;x 

&theta; è dunque l'uccello saggio, anche detto fixed point combinator.


## Fixed Point

- Un esempio pratico per capire un fixed point è usare una calcolatrice e provare a schiacciare ripetutamente il
tasto cos partendo dal valore 0.

- Si noterà che il valore converge. cos(cos(cos(cos(cos(cos(x)))))) = x

- In generale il punto fisso di una funzione è dove y = x = f(x)

- E' importante notare che un punto fisso non per forza deve essere un valore, ma può essere a sua volta una funzione

- fixpoint-function = f(fixpoint-function)

## Funzione Fattoriale

- Implementazione:

```tut:silent
def factorial(n: Int): Int = n match {
    case 0 => 1
    case x => x * factorial(x - 1)
  }
```

## Puzzle

- Provare a creare una versione di factorial che non è ricorsiva

```tut:silent
def factFLazy(f: => (Int => Int)): Int => Int = { n =>
    if (n == 0) 1
    else n * f(n - 1)
   }
```
- Quello che è stato fatto è rinominare la chiamata ricorsiva a factorial in f e passare f come argomento a factFLazy
- factFLazy è una higher-order function che riceve una funzione f e ritorna un'altra funzione che dovrà essere quella fattoriale.

## Factorial tramite factFLazy

- Assumiamo di avere una funzione factA in grado di computare fattoriali
```tut:silent
def factA(num: Int) : Int = factorial(num)
```
- Si consideri ora la funzione factB
```tut:silent
val factB: Int => Int = factFLazy(factA)
```

- La funzione factB è in grado di computare correttamente i fattoriali poichè dipende da factA
```tut
factB(5)
```

- E' evidente che data una funzione f in grado di calcolare fattoriali correttamente, la funzione
factFLazy restituirà un'altra funzione f' che sarà ancora una funzione valida per calcolare fattoriali.

- Ma allora assumendo che f' = f

```tut
val fact: Int => Int = factFLazy(fact)
fact(5)
```
## Fattoriale per induzione

- Definiamo le due seguenti funzioni
```tut:silent
val identity: Int => Int = { (x: Int) => x }
val fact0: Int => Int = factFLazy(identity)
```
- Fact0 può computare solamente fact(0).

```tut
fact0(0)
fact0(1) // wrong
```

- Proviamo a definire ora fact1 in termini di fact0

```tut
val fact1: Int => Int = factFLazy(fact0)
fact(1)
```

- E' evidente che è possibile definire fact n in termini di fact n -1 utilizzando factFLazy.

```tut
val fact2: Int => Int = factFLazy(fact1)
val fact3: Int => Int = factFLazy(fact2)
val fact4: Int => Int = factFLazy(fact3)
val fact5: Int => Int = factFLazy(fact4)

fact5(5)
```

- Induttivamente dunque abbiamo mostrato che una catena infinita di factFLazy ci darà la funzione fattoriale

- la funzione fact è dunque il punto fisso di factFLazy:

```tut
val fixFact: Int => Int = factFLazy(fixFact)
```

## L'uccello saggio, ancora

- Sapere che fact è il punto fisso di factFLazy non ci dice come computarlo.

- Servirebbe una funzione che prendesse in input una funzione come factFLazy e ci restituisse una funzione che è il punto fisso di factFLazy

> Un uccello speciale che se interrogato con il nome di un uccello x risponde
 nominando un uccello y del quale x è innamorato

## Derivazione

 - &theta;f = fix-poinf-f
 - f(fix-point-f) = fix-point-f per definizione di fix point (cos x = x)
 - Per cui &theta;f = f(fix-point-f) e ne consegue che &theta;f = f(&theta;f)

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

## Note

- Così definito Y non è un combinatore vero e proprio.

- Il corpo della funzione ha infatti una variabile libera che è Y stessa

- In lambda calcolo è definito come: Y = &lambda;f.(&lambda;x.f (x x)) (&lambda;x.f (x x))

- Le due notazioni sono comunque equivalenti ma implementare il combinatore in linguaggi come Haskell o Scala non è possibile senza fare delle forzature sul type system.







