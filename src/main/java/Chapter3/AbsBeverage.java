package Chapter3;

public abstract class AbsBeverage {
    private final String description;
    private final Level level;


    public AbsBeverage(String description, Level level) {
        this.description = description;
        this.level = level;
    }
    public String getDescription(){
        return level.toString() +" "+ description;
    }
    public String getBaseDescription() {
        return description; // Only the description, no level, mainly for condiments
    }

    public Level getLevel() {
        return level;
    }

    public abstract float getCost();

}
