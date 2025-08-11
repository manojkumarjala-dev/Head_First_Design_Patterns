package ParkingLotManager;

public abstract class ASlot {
    String id;
    VehicleType vehicleType;
    boolean isOccupied;

    ASlot(VehicleType vehicleType, String id){
        this.vehicleType = vehicleType;
        this.isOccupied = false;
        this.id = id;
    }
}
