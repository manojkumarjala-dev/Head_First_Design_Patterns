package ParkingLotManager;
import java.util.List;
import java.util.Optional;

public class ParkingLot {
    List<ASlot> slots;
    ParkingLot(List<ASlot> slots){
        this.slots = slots;
    }

    Optional<ASlot> checkAvailability(AVehicle vehicle){
        for(ASlot slot: slots){
            if(!slot.isOccupied && slot.vehicleType == vehicle.vehicleType){
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }

    Optional<Ticket> parkVehicle(AVehicle vehicle){
        Optional<ASlot> availableSlot = checkAvailability(vehicle);
        if(availableSlot.isPresent()){
            ASlot slot = availableSlot.get();
            slot.isOccupied = true;
            return Optional.of(new Ticket(vehicle,slot));
        }
        return Optional.empty();
    }

    public boolean exitVehicle(Ticket ticket, PaymentStrategy paymentStrategy) {
        if (ticket.endTime != null) return false;

        ticket.endTime = System.currentTimeMillis();
        long duration = (ticket.endTime - ticket.startTime) / 1000;
        double amount = calculateAmount(duration);

        paymentStrategy.pay(amount);
        ticket.slot.isOccupied = false;
        return true;
    }

    private double calculateAmount(long durationSeconds) {
        return Math.max(10, durationSeconds * 0.05);
    }


}
