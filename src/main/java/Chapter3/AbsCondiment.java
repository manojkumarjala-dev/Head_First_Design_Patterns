package Chapter3;

/** Abstract decorator: shares Beverage type and keeps a reference to the wrapped drink. */
public abstract class AbsCondiment extends AbsBeverage {

    /** the component we’re decorating */
    protected final AbsBeverage beverage;

    /**
     * @param beverage   drink being wrapped (must not be null)
     * @param description text this decorator adds (e.g. "Mocha")
     */
    protected AbsCondiment(AbsBeverage beverage, String description) {
        super(description, beverage.getLevel());                 // set decorator’s own descriptor part
        this.beverage = beverage;           // store wrapped component
    }

    /** Making use of getBaseDescription to print only the condiment without the size */
    @Override
    public String getDescription() {
        return beverage.getDescription() + ", " + getBaseDescription();
    }
}
