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
        super(description);                 // set decorator’s own descriptor part
        if (beverage == null) {
            throw new IllegalArgumentException("Decorator needs a Beverage to wrap");
        }
        this.beverage = beverage;           // store wrapped component
    }

    /** Default implementation: delegate then append our own text. */
    @Override
    public String getDescription() {
        return beverage.getDescription() + ", " + super.getDescription();
    }
}
