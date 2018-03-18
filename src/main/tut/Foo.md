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




