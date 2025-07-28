package Chapter3;

public class Whip extends AbsCondiment {

    public Whip(AbsBeverage beverage, String description) {
        super(beverage, description);
    }

    @Override
    public float getCost() {
        if (beverage.getLevel() == Level.TALL) {
            return 0.10f + beverage.getCost();
        } else if (beverage.getLevel() == Level.GRANDE) {
            return 0.15f + beverage.getCost();
        } else {
            return 0.20f + beverage.getCost();
        }
    }

}
