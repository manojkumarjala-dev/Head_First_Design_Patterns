package Chapter8_template;

public class Chapter8_template {
    public static void main(String[] args){
        Template concreteClass1 = new ConcreteClass1();
        Template concreteClass2 = new ConcreteClass2();

        concreteClass1.aggregateFunction();
        concreteClass2.aggregateFunction();
    }
}
