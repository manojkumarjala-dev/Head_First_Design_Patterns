package Chapter6_v2_3;

import java.util.Stack;

public class Remote {
    Command[] slots;
    Stack<Command> history;
    Remote(int slots){
        this.slots = new Command[slots];
        this.history = new Stack<Command>();
    }
    void setSlot(Command command, int slotNumber){
        this.slots[slotNumber] = command;
    }

    void clickSlot(int slotNumber){
        Command c = this.slots[slotNumber];
        if(c!=null){
            history.push(c);
            c.execute();
        }
        else{
            System.out.println("Unassigned slot");
        }
    }

    void undo(){
        if(!this.history.isEmpty()){
            Command c = this.history.pop();
            c.undo();
        }
        else{
            System.out.println("Nothing to undo");
        }
    }
}
