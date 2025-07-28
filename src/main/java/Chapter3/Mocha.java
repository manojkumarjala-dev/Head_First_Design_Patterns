package Chapter3;

public class Mocha extends AbsCondiment {
    public Mocha(AbsBeverage beverage, String description) {
        super(beverage, description);
    }

    @Override
    public float getCost() {
        if (beverage.getLevel() == Level.TALL) {
            return 0.20f + beverage.getCost();
        } else if (beverage.getLevel() == Level.GRANDE) {
            return 0.25f + beverage.getCost();
        } else {
            return 0.30f + beverage.getCost();
        }
    }

}
