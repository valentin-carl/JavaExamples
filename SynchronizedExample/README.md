# Synchronized-Beispiel in Java

Die Klasse `ThreadBeispiel.java` illustriert anhand eines einfachen Beispiels, wie sich Threads in Java verhalten, wenn gemeinsame (hier Klassenvariablen) oder unterschiedliche (hier Instanzvariablen) Objekte zur Synchronisation verwendet werden.

----

### Output

Der Output des Codes hängt davon ab, ob die Variable `static boolean SWITCH` `true`oder `false` ist.

Für `true` wird `static Object monitor = new Object();` zur Synchronisation verwendet.

~~~
a says hello.
a says hello.
a says hello.
...
~~~

Für `false` verwendet jeder Thread das eigene Instanzobjekt `Object o = new Object();`.

~~~
b says hello.
a says hello.
c says hello.
e says hello.
f says hello.
g says hello.
d says hello.
h says hello.
i says hello.
j says hello.
a says hello.
c says hello.
b says hello.
d says hello.
f says hello.
h says hello.
g says hello.
i says hello.
e says hello.
j says hello.
...
~~~

**Wichtig:** Die Reihenfolge der Ausgabe im zweiten Fall ist nicht deterministisch und kann sich unterscheiden!
