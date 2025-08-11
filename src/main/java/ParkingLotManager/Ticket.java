package ParkingLotManager;

public class Ticket {
    AVehicle vehicle;
    ASlot slot;
    long startTime;
    Long endTime;
    String id;
    Ticket(AVehicle vehicle, ASlot slot){
        this.slot = slot;
        this.vehicle = vehicle;
        this.endTime = null;
        this.id = "ID-"+System.currentTimeMillis();
    }
}
