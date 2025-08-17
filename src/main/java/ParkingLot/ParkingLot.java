package ParkingLot;

import java.util.*;

/** RUN DEMO **/
public class ParkingLot {
    public static void main(String[] args) throws InterruptedException {
        Lot lot = new Lot();
        VehicleSlotFactory factory = new VehicleSlotFactory();

        // Add capacity
        lot.addSlot(factory.createSlot(SlotType.TWO_WHEELER, "TW-1"));
        lot.addSlot(factory.createSlot(SlotType.TWO_WHEELER, "TW-2"));
        lot.addSlot(factory.createSlot(SlotType.FOUR_WHEELER, "FW-1"));
        lot.addSlot(factory.createSlot(SlotType.FOUR_WHEELER, "FW-2"));

        // Vehicles
        Vehicle car1  = new Car(VehicleType.FOUR_WHEELER);
        Vehicle car2  = new Car(VehicleType.FOUR_WHEELER);
        Vehicle bike1 = new Bike(VehicleType.TWO_WHEELER);

        // Park
        Ticket t1 = lot.park(car1);  // should get FW
        Ticket t2 = lot.park(car2);  // should get FW
        Ticket t3 = lot.park(bike1); // should get TW (or FW if TW full)

        // Show availability
        lot.printAvailability();

        // Simulate time
        Thread.sleep(50);

        // UnPark with pricing + payment
        PricingStrategy pricing = new FlatHourlyPricing(100); // $/hour (example)
        PaymentStrategy payment = new CreditCardPaymentStrategy();

        if (t1 != null) lot.unPark(t1, pricing, payment);
        if (t2 != null) lot.unPark(t2, pricing, payment);
        if (t3 != null) lot.unPark(t3, pricing, payment);

        lot.printAvailability();
    }
}

/** DOMAIN **/
enum VehicleType { TWO_WHEELER, FOUR_WHEELER }
enum SlotType    { TWO_WHEELER, FOUR_WHEELER }

final class Slot {
    final SlotType type;
    final String id;
    boolean occupied;

    Slot(SlotType type, String id) { this.type = type; this.id = id; }
    @Override public String toString() { return id + "(" + type + ")"; }
}

record Ticket(String ticketId, Slot slot, long startTime, VehicleType vehicleType) { }

abstract class Vehicle {
    final VehicleType type;
    Vehicle(VehicleType type) { this.type = type; }
}
final class Car  extends Vehicle { Car (VehicleType t){ super(t);} }
final class Bike extends Vehicle { Bike(VehicleType t){ super(t);} }

/** FACTORY **/
class VehicleSlotFactory {
    Slot createSlot(SlotType type, String id) { return new Slot(type, id); }
}

/** LOT (single floor) **/
class Lot {
    // Simple availability by slot type
    private final Map<SlotType, Deque<Slot>> free = new EnumMap<>(SlotType.class);
    private final Map<SlotType, Set<Slot>>   used = new EnumMap<>(SlotType.class);
    private final Map<String, Ticket> tickets = new HashMap<>();
    private final Random rnd = new Random();

    // Compatibility: first try native slot; allow TW -> FW upgrade; disallow FW -> TW
    private static final Map<VehicleType, List<SlotType>> COMP = Map.of(
            VehicleType.TWO_WHEELER, List.of(SlotType.TWO_WHEELER, SlotType.FOUR_WHEELER),
            VehicleType.FOUR_WHEELER, List.of(SlotType.FOUR_WHEELER)
    );

    Lot() {
        for (SlotType st : SlotType.values()) {
            free.put(st, new ArrayDeque<>());
            used.put(st, new HashSet<>());
        }
    }

    void addSlot(Slot s) { free.get(s.type).addLast(s); }

    /** Allocates and marks occupied. Returns ticket or null if full. */
    Ticket park(Vehicle v) {
        for (SlotType st : COMP.get(v.type)) {
            Slot s = free.get(st).pollFirst();
            if (s != null) {
                s.occupied = true;
                used.get(st).add(s);
                Ticket t = new Ticket(newTicketId(), s, System.currentTimeMillis(), v.type);
                tickets.put(t.ticketId(), t);
                System.out.println("Parked " + v.type + " at " + s + " -> Ticket " + t.ticketId());
                return t;
            }
        }
        System.out.println("No slot available for " + v.type);
        return null;
    }

    /** Frees slot, computes fee, takes payment. */
    void unPark(Ticket t, PricingStrategy pricing, PaymentStrategy pay) {
        Ticket real = tickets.remove(t.ticketId());
        if (real == null) { System.out.println("Invalid ticket: " + t.ticketId()); return; }

        long end = System.currentTimeMillis();
        long fee = pricing.calculate(real.vehicleType(), real.startTime(), end);

        System.out.println("Unparking " + real.vehicleType() + " from " + real.slot() +
                " | Fee: " + fee);
        boolean ok = pay.makePayment(fee);
        if (!ok) { System.out.println("Payment failed."); return; }

        // free slot
        Slot s = real.slot();
        used.get(s.type).remove(s);
        s.occupied = false;
        free.get(s.type).addLast(s);
        System.out.println("Slot freed: " + s);
    }

    void printAvailability() {
        System.out.println("Availability:");
        for (SlotType st : SlotType.values()) {
            System.out.println("  " + st + " -> free=" + free.get(st).size() + ", used=" + used.get(st).size());
        }
    }

    private String newTicketId() { return "T-" + (100000 + rnd.nextInt(900000)); }
}

/** PRICING + PAYMENT **/
interface PricingStrategy {
    long calculate(VehicleType vt, long startMillis, long endMillis);
}

class FlatHourlyPricing implements PricingStrategy {
    private final long ratePerHour;
    FlatHourlyPricing(long ratePerHour) { this.ratePerHour = ratePerHour; }
    public long calculate(VehicleType vt, long start, long end) {
        long minutes = Math.max(1, (end - start) / 60000); // round down; ensure >=1
        long hours = (minutes + 59) / 60; // ceil to hour
        // Could vary by vehicle type; keep simple
        return hours * ratePerHour;
    }
}

interface PaymentStrategy { boolean makePayment(long amount); }

class CreditCardPaymentStrategy implements PaymentStrategy {
    public boolean makePayment(long amount) {
        System.out.println("Charging credit card: " + amount);
        return true;
    }
}



/*
outline of parkingLot
ParkingLot -> ParkingSpaces
car comes to lot & checkins -> availabilityCheck -> return slot & ticket
park my vehicle
exit - give ticket and pay the money


Q's
1. Entry & exit? 1 each
2. Multiple vehicles
3. different
4. floors?
5 display board? yes

REQ:
ParkingLot supports parking of different vehicles
Single floor parking lot
Multiple payment types & multiple pricing tiers based on vehicle
Display board to show the availability



1. handling different vehicles - abstraction
2. handling different slot types - Abstraction
3. handling different Payment strategies - Strategy
3. Handling displayBoard - Observer

Relationships:
ParkingLot will have slots, simulate entry and exit
User will have Payment Strategies

 */
