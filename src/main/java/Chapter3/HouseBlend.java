package Chapter3;

public class HouseBlend extends AbsBeverage {

    public HouseBlend(String description, Level level) {
        super(description, level);
    }

    @Override
    public float getCost() {
        if(getLevel() == Level.TALL) {
            return 0.89f;
        } else if(getLevel() == Level.GRANDE) {
            return 1.05f;
        } else {
            return 1.25f;
        }
    }

}
