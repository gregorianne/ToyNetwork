Concepte folosite

SOLID
1. Single Responsibility: o clasă = o responsabilitate.
NumereComplexe: tine doar arhitectura unui nr complex si operatiile dintre 2 astfel de nr.
ExpressionParser: doar citește și validează textul.
ExpressionFactory: doar alege ce expresie să creeze.
Adunare, Scadere etc.: cate o operatie.

2. Open/closed: codul e extins doar la extindere,  nu la modificare

3. Liskov: execute() din clasa Complex Expression lucrează cu un ComplexExpression (Nu stie daca e adunare sau impartire, etc...); orice copil se comportă corect în locul lui. (polimorfism)

4. Interface Segregation: nu obliga o clasa sa implementeze metode de care nu are nevoie. fiecare copil implementează o singură metodă abstractă, executeOneOperation, desi aici nu avem interfete propriu zise

5. Dependency Inversion: Dependentele tin de lucruri generale, nu concrete. Exemplu: execute() și ExpressionParser lucrează cu ComplexExpression (abstractul), nu cu Adunare direct.

Sabloane:

-Single Responsability
-Factory: ExpressionFactory.createExpression() primește un Operation și creează instanta potrivită. Cine îl cheamă nu face new Adunare direct.
-Singleton: ExpressionFactory are o singură instanță (constructor privat + getInstance()), fiindcă nu are stare și nu are rost să existe mai multe (date care se schimbă).
-Template Method: execute() din ComplexExpression definește scheletul (ciclul de combinare de la stânga la dreapta), iar executeOneOperation e pasul pe care îl completează fiecare copil.