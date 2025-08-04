package Chapter6_v2;

public class Remote {
    Command[] slots;
    Remote(int slots){
        this.slots = new Command[slots];
    }
    void setSlot(Command command, int slotNumber){
        this.slots[slotNumber] = command;
    }

    void clickSlot(int slotNumber){
        Command c = this.slots[slotNumber];
        if(c!=null){
            c.execute();
        }
        else{
            System.out.println("Unassigned slot");
        }
    }
}
