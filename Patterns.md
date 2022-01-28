---
title: Software Design Patterns
---

# Design Patterns

## Grundidee

*Design Patterns* (dt. Entwicklungsmuster) sind allgemeine, wiederverwendbare Lösungsansätze für häufig vorkommende Probleme in der Softwareentwicklung. Entwicklungsmuster sind keine fertig implementierten Designs, sondern können eher als *template* und *best practice* verstanden werden.

## Arten von Patterns

Verschiedene Patterns können grob in drei Kategorien eingeteilt werden:

- *creational design patterns* (dt. Erzeugungsmuster)
- *structural design patterns* (dt. Strukturmuster)
- *behavioral design patterns* (dt. Verhaltensmuster)

Es folgt eine kurze Beschreibung von einem Auszug der Design Patterns, die Liste ist keineswegs vollständig, sondern dient nur zur Illustration der Grundidee.

## Creational Design Patterns (Erzeugungsmuster)

*Creational design patterns* betreffen die Erzeugung von Klassen oder Objekten in Softwaresystemen.

### Factory Pattern

Das Factory Pattern beschreibt eine allgemeine Möglichkeit, Objekte in einem Programm zu erstellen. Die Idee ist es, ein Interface oder eine abstrakte Klasse für die Objekte zu definieren, die erstellt werden sollen. Subklassen davon bestimmen dann, wie diese Objekte tatsächlich implementiert werden. Dazu gibt es ein Factory Objekt (oder eine Factory Klasse), das die Instanziierung der Objekte übernimmt. Der Vorteil dieser Strukturierung ist, dass die Erstellungslogik der Objekte unabhängig davon ist, wo die Objekte gebraucht werden und nur einmal festgelegt werden muss – gleichzeitig können Objekte, dort wo sie gebraucht werden, vorher über das Interface oder die abstrakte Klasse referenziert werden. Das folgende Klassendiagramm zeigt den Aufbau des Factory Patterns anhand eines Beispiels.

```
┌─────────────────────────────────────────┐
│                                         │
│            ┌─────────────┐              │           ┌─────────────┐
│            │<<interface>>│              │           │             │
│            │    Ball     │              │           │  SomeClass  │
│            ├─────────────┤              │           ├─────────────┤
│            │+size: int   │              │           │             │
│            │+price: float│              │           │             │
│            ├─────────────┤              │           ├─────────────┤
│            │+roll(): void│              │           │+main(): void│
│            │             │              │           │             │
│            └─────────────┘              │           └──────┬──────┘
│                   ▲                     │                  │
│                   │ implements          │                  │ calls
│        ┌──────────┴───────────┐         │                  ▼
│        │                      │         │         ┌─────────────────┐
│  ┌─────┴──────┐       ┌───────┴──────┐  │         │                 │
│  │            │       │              │  │         │   BallFactory   │
│  │  Football  │       │  Volleyball  │  │         ├─────────────────┤
│  ├────────────┤       ├──────────────┤  │ creates │                 │
│  │            │       │              │  │◄────────┤                 │
│  │            │       │              │  │         ├─────────────────┤
│  ├────────────┤       ├──────────────┤  │         │+getBall(): Ball │
│  │            │       │              │  │         │                 │
│  │            │       │              │  │         └─────────────────┘
│  └────────────┘       └──────────────┘  │
│                                         │
└─────────────────────────────────────────┘
```

Hinweis: Es bietet sich an, entweder die Klasse `BallFactory` und nur Klassenmehtoden und einem privaten Konstruktor auszustatten oder das Singleton Pattern auf die Klassen anzuwenden, um zu verhindern, dass mehr Objekte der Factory Klassen als notwendig erstellt werden.

**Mögliche Java Implementierung**

```java
public interface Ball {

  int size;
  float price;
  
  public void roll ();
}
```
```java
public class Volleyball {

  int size;
  float price;
  // more attributes here
  
  protected Volleyball (int size, float price) {
    this.size = size;
    this.price = price;
  }
  
  public void roll () {
    System.out.println(this + " volleyball is rolling..");
  }
}
```
```java
public class Football {

  int size;
  float price;
  // more attributes here
  
  protected Football (int size, float price) {
    this.size = size;
    this.price = price;
  }
  
  public void roll () {
    System.out.println(this + " football is rolling..");
  }
}
```
```java
public class BallFactory {

  // private constructor to prevent object creation
  private BallFactory () {}
  
  // factory method
  public static Ball getBall (String type) throws IllegalArgumentException {
    if (!(type.equals("Volleyball") || type.equals("Football"))) throw new IllegalArgumentException("Type has to be either Football or Volleyball");
    return switch (type) {
      case "Volleyball" -> new Volleyball(3, 15.0f);
      case "Football" -> new Football(2, 12.5f);
    }
    return null;
  }
}
```
```java
public class SomeClass {

  ...
  
  // main method
  public static void main (String[] args) {
    ...
    Ball b = null;
    try {
      b = BallFactory.getBall("Volleyball");
    } catch (IllegalArgumentException e) {}
    b.roll;
    ...
  }
}
```
~~~
Volleyball@0x298f74 volleyball is rolling.
~~~

[Mehr lesen](https://en.wikipedia.org/wiki/Factory_method_pattern)

### Singleton Pattern

In machen Fällen ist es sinnvoll, nur eine Instanz einer Klasse zu erlauben. Dafür kann das Singleton Pattern verwendet werden. Der Konstruktur der Klasse wird auf privat gesetzt, damit er von außen nicht erreichbar ist. Es gibt eine Klassenreferenz auf das Singleton Objekt. Zuletzt gibt es eine Klassenmethode `getInstance`, die – falls diese zum ersten Mal aufgerufen wird – das Klassenobjekt instanziiert und anschließend eine Referenz darauf zurückgibt. **Ergänzung:** Wird im restlichen Programm mit Threads gearbeitet, ist es sinnvoll, die `getInstance()`-Methode um das `synchronized`-Keyword zu ergänzen. Sonst ist das Programm nicht mehr thread-safe.

**Mögliche Java Implementierung**

```java
public class SomeClass {
  
  private int someAttribute;
  private static SomeClass singleton = null;
  
  // private constructor
  private SomeClass (int x) {
    this.someAttribute = x;
  }
  
  // getInstance method returns reference to the singleton object
  public static synchronized SomeClass getInstance (int x) {
    if (SomeClass.singleton == null) SomeClass.singleton = new SomeClass(x);
    return SomeClass.singleton;
  }
}
```



## Structural Design Patterns (Strukturmuster)

### Proxy Pattern

### Composite Pattern


## Behavioral Design Patterns (Verhaltensmuster)

### Oberserver Pattern

### Command Pattern





---
### References

Gamma, E., Helm, R., Johnson, R., and Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software.* Addison-Wesley.
