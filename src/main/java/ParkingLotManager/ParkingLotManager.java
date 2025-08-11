package ParkingLotManager;

import java.util.*;

public class ParkingLotManager {
    public static void main(String[] args) {
        // Setup slots
        List<ASlot> slots = new ArrayList<>();

        slots.add(SlotFactory.createSlot(VehicleType.FOUR_WHEELER, UUID.randomUUID().toString()));
        slots.add(SlotFactory.createSlot(VehicleType.TWO_WHEELER, UUID.randomUUID().toString()));



        // Create parking lot
        ParkingLot parkingLot = new ParkingLot(slots);

        // Create a vehicle
        AVehicle car = new CCar(UUID.randomUUID().toString(),VehicleType.FOUR_WHEELER);

        // Park the vehicle
        Optional<Ticket> ticketOpt = parkingLot.parkVehicle(car);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            System.out.println("Vehicle parked. Ticket issued.");

        } else {
            System.out.println("No available slot for vehicle.");
        }



        // exiting the vehicle
        PaymentStrategy payment = new CCPaymentStrategy();
        boolean exited = parkingLot.exitVehicle(ticketOpt.get(), payment);
        if (exited) {
            System.out.println("Vehicle exited and payment done.");
        } else {
            System.out.println("Exit failed.");
        }


    }
}