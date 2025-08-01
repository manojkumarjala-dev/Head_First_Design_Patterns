package Chapter6_v1;

public class Chapter6_v1 {
    public static void main(String[] args){

        Light light = new Light("Light turned on", "Light turned off");
        Remote remote = new Remote();

        LightOnCommand lightOnCommand = new LightOnCommand(light);
        LightOffCommand lightOffCommand = new LightOffCommand(light);

        remote.setCommand(lightOnCommand, lightOffCommand);

        remote.clickSlot1();
        remote.clickSlot2();

    }
}
