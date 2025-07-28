package Chapter3;

public abstract class AbsBeverage {
    private final String description;

    public AbsBeverage(String description) {
        this.description = description;
    }
    public String getDescription(){
        return description;
    }

    public abstract float getCost();

}
