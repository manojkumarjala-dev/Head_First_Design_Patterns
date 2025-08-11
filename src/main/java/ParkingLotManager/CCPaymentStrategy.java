package ParkingLotManager;

public class CCPaymentStrategy implements PaymentStrategy{
    @Override
    public void pay(double amount){
        System.out.println("Payment done via CC");
    }
}
