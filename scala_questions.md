# Scala Questions

## Explain the difference: 
* `map` vs `foreach`.
  > * Method signature is different
  > * `map` is a transformer and `foreach` is a finisher(side effects)
  
* Give a single example where map and another one where foreach is to be preferred.
  > * example use of `map` List("madrid", "sevilla", "barcelona").map(_.toUpperCase)
  > * example use of `foreach` List("madrid", "sevilla", "barcelona").foreach(println)
  
* what does the projection to a for-loop look like for these two
  > I dont undertand this question

## Type inference
do not use the repls
```scala
    val foo = if (true) 5
```

* what type is foo? Explain why ?
  > The parent of which Int and Unit inherit.
    Because it seeks that the superior type in inheritance is compatible with both types

## How to model multiple constructors in Scala ?
  > Creating methods named `def this(...)` and referenced to the main constructor with reserved word `this(...)`
  > For instance:
  > ```scala
  > // Main Constructor
  >   class Pet(name: String, category: String) {
  >      // Other Constructor
  >      def this(name: String) = {
  >        this(name, "DEFAULT")
  >      }
  >   }
  > ``` 
  > Or using the method apply
  > ```scala
  > // Main Constructor
  > class Pet(name: String, category: String)
  > 
  > object Pet {
  >   def apply(name: String, category: String): Pet = new Pet(name, category)
  >   // Other constructor
  >   def apply(name: String): Pet = new Pet(name, "DEFAULT")
  > }
  > ``` 
## What is a “Companion Object” ?
  > a class with one instance, is a singleton
## Case classes
* What do they provide for free ?
  > toString(), hashCode(), equals() methods java, apply() and unapply() methods scala.
* What should you not do when using a case class ?
  > when you need POJO like Java or abstract data use case class, for other things like logic, mutable  state use normal classes
* What are they commonly used for ?
  > Data representation

## Futures

a)
```scala
val f1 = Future(Thread.sleep(100))
val f2 = Future(Thread.sleep(100))

val a = for {
 _ <- f1
 _ <- f2
} yield { () }

Await.result(a, Duration(150, TimeUnit.MILLISECONDS))

```

b)
```scala
val b = for {
 _ <- Future( Thread.sleep(100))
 _ <- Future( Thread.sleep(100))
} yield { () }

Await.result(b, Duration(150, TimeUnit.MILLISECONDS))
```

* what will be the output of a) ?
  > ()
* what will be the the output of b) ?
  > Exception
* In what regard does a differ to b ?
  > * "a" futures are declared and executed immediately both
  > * "b" futures are sequenced using flatmap
* When to use a) when to use b)
  > * "a" if you don't care about the outcome of the first future, you can launch them in parallel
  > * "b" if you care about the outcome of the first future to trigger another future