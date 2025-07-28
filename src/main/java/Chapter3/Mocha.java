package Chapter3;

public class Mocha extends AbsCondiment {
    public Mocha(AbsBeverage beverage, String description) {
        super(beverage, description);
    }

    @Override
    public float getCost() {
        return 0.20f + beverage.getCost();
    }

}
