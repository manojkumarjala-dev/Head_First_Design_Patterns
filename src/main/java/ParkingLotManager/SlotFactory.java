package ParkingLotManager;

public class SlotFactory {
    public static ASlot createSlot(VehicleType vehicleType, String id) {
        switch (vehicleType) {
            case FOUR_WHEELER:
                return new CarSlot(vehicleType, id);
            case TWO_WHEELER:
                return new BikeSlot(vehicleType, id);
            default:
                throw new IllegalArgumentException("Unsupported vehicle type");
        }
    }
}