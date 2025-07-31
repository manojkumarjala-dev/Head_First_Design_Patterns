package Chapter5;

public class Chapter5 {
    public static void main(String[] args){
        Singleton instance1 = Singleton.getInstance();

        Singleton instance2 = Singleton.getInstance();

        if(instance2 == instance1){
            System.out.println("Both are same instance");
        }
        else{
            System.out.println("Not same");
        }

        System.out.println(instance1.hashCode());
        System.out.println(instance2.hashCode());

    }
}
