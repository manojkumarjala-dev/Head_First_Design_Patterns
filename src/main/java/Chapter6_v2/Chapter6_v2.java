package Chapter6_v2;

import Chapter6_v2.Remote;

public class Chapter6_v2 {
    public static void main(String[] args){
        Remote remote = new Remote(4);
        Light kitchenLight = new Light("Kitchen light is on", "Kitchen light is Off", false);
        Fan hallFan = new Fan("Hall fan is on", "Hall fan is off", false);

        Command lightOnCommand = new LightOnCommand(kitchenLight);
        Command lightOffCommand = new LightOffCommand(kitchenLight);

        FanOnCommand fanOnCommand = new FanOnCommand(hallFan);
        FanOffCommand fanOffCommand = new FanOffCommand(hallFan);

        remote.setSlot(lightOffCommand, 0);
        remote.setSlot(lightOnCommand,1);
        remote.setSlot(fanOffCommand,2);
        remote.setSlot(fanOnCommand,3);

        remote.clickSlot(0);
        remote.clickSlot(1);
        remote.clickSlot(2);
        remote.clickSlot(3);


    }
}
