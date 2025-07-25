package Chapter1;

public class Chapter1 {
    public static void main(String[] args) {
       Duck normalDuck = new normalDuck(new flyWithWings(), new quack());
       Duck rocketDuck = new rocketDuck(new flyWithRocket(), new quack());

        normalDuck.flyBehavior.fly();
        normalDuck.quackBehavior.makeSound();

        rocketDuck.flyBehavior.fly();
        rocketDuck.quackBehavior.makeSound();
    }
}
/*
- Declaring fields in an abstract class (like Duck) allows sharing common state and behavior among all subclasses.
- Here, `flyBehavior` and `quackBehavior` are fields that can be assigned different behaviors at runtime.
- The constructor in the abstract class initializes these fields, allowing subclasses to specify their behaviors when instantiated.
- The `Duck` class is abstract, meaning it cannot be instantiated directly. It serves as a base class for specific duck types.
- The `MallableDuck` class extends `Duck`, providing a concrete implementation that can be instantiated.

- Interface methods in Java are implicitly public; implementing methods must also be public to avoid visibility errors.
- You can declare a nested interface inside a concrete class that extends an abstract class; this is valid and can be used for internal contracts or callbacks.
- The design follows good object-oriented principles: abstraction, encapsulation, strategy pattern, separation of concerns, and extensibility.

Q) Can interface also have data fields like abstract class
    No, interfaces in Java cannot have instance data fields like abstract classes. Interfaces can only have:
    Constants (static final fields)
    Abstract methods (implicitly public and abstract)
    Default methods (with implementation)
    Static methods
 */