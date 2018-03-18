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

## Come trovare l'uccello saggio

Date le sole condizioni C1 e C2 non è possibile trovare l'uccello saggio.

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













