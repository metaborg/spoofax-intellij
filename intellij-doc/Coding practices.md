# Coding practices

## General

### Classes
A class that you don't intend to derive from:

* Must be marked `final`.
* Must not have any `protected` members. Instead, make them `private`.

A class that you _do_ intend to derive from:

* Must not have any `public` constructors. Instead, make them `protected`.

A class with only `static` members:

* Must be marked `final`.
* Must have a single parameterless `private` constructor.
* Must not have any instance members.


### Fields
A defined field:

* Should be initialized in a constructor.
* Must be marked `final` when it is not changed outside a constructor.
* Must have the `@Nullable` annotation when it might be null.

A used field:

* Must be preceded by `super.` when it's an instance field of an ancestor class.
* Must be preceded by `this.` when it's an instance field of this class.


### Constants
Constants and magic values:

* Must be defined as `static final` fields.


## Dependency injection
For dependency injection, prefer constructor injection.


### Constructor injection
The constructor used for dependency injection:

* Must only contain parameter types that are injectable.
* Must be `/* package private */`.
* Must have the `@Inject` annotation.


### Singleton class
Traditionally a singleton class has a private constructor and a static `INSTANCE` field to get the singleton instance. With dependency injection, the class:

* Must have the `@Singleton` annotation.
* Must have only one constructor, that conforms to the [Constructor injection](#Constructor injection) practices.
* Must not have an `INSTANCE` static field or any other static fields that can be used to get the singleton instance.
* Must be bound (to singleton) in the DI-module.

Use injection to get the singleton instance in another class.
