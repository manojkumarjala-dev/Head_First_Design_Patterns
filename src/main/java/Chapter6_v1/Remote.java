package Chapter6_v1;

public class Remote {
    private Command slot1;
    private Command slot2;
    Remote(){
        slot1 = null;
        slot2 = null;
    }

    public void setCommand(Command command1, Command command2){
        this.slot1 = command1;
        this.slot2 = command2;
    }

    public void clickSlot1() {
        if (slot1 != null) {
            slot1.execute();
        }
    }

    public void clickSlot2() {
        if (slot2 != null) {
            slot2.execute();
        }
    }

}
