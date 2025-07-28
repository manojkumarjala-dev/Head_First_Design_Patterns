package Chapter3;

public class Chapter3 {
    public static void main(String[] args) {
        AbsBeverage beverage1 = new HouseBlend("House Blend Coffee", Level.GRANDE);
        //System.out.println(beverage1.getDescription() + " $" + beverage1.getCost());

        AbsBeverage beverage2 = new Decaf("Decaf Coffee", Level.TALL);
        //System.out.println(beverage2.getDescription() + " $" + beverage2.getCost());

        beverage1 = new Whip(beverage1, "Whip");
        beverage1 = new Mocha(beverage1, "Mocha");

        System.out.printf("%s $%.2f\n", beverage1.getDescription(), beverage1.getCost());

        beverage2 = new Mocha(beverage2, "Mocha");
        beverage2 = new Mocha(beverage2, "Mocha");

        System.out.printf("%s $%.2f\n", beverage2.getDescription(), beverage2.getCost());


    }
}
