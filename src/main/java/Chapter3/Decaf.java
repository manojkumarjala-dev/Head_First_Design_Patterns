package Chapter3;

public class Decaf extends AbsBeverage {

    public Decaf(String description, Level level) {
        super(description, level);
    }

    @Override
    public float getCost() {
        if (getLevel() == Level.TALL) {
            return 0.99f;
        } else if (getLevel() == Level.GRANDE) {
            return 1.15f;
        } else {
            return 1.35f;
        }
    }
}
