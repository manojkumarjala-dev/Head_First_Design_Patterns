package Chapter3;

public class Whip extends AbsCondiment {

    public Whip(AbsBeverage beverage, String description) {
        super(beverage, description);
    }

    @Override
    public float getCost() {
        return 0.40f + beverage.getCost();
    }

}
