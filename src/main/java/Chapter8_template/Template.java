package Chapter8_template;

abstract class Template {
    void concreteFunction(){
        System.out.println("Concrete function implemented");
    }
    abstract void function1();
    abstract void function2();

    void aggregateFunction(){
        concreteFunction();
        function1();
        function2();
    }
}
