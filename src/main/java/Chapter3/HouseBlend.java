package Chapter3;

public class HouseBlend extends AbsBeverage {

    public HouseBlend(String description) {
        super(description);
    }

    @Override
    public float getCost() {
        return 0.89f;
    }

}
