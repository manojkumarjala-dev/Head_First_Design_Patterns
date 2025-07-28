package Chapter3;

public class Decaf extends AbsBeverage {

    public Decaf(String description) {
        super(description);
    }

    @Override
    public float getCost() {
        return 1.05f;
    }
}
