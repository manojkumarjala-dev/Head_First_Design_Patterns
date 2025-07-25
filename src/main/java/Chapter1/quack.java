package Chapter1;

public class quack implements QuackBehavior {
    @Override
    public void makeSound() {
        System.out.println("Quack! Quack!");
    }
}
