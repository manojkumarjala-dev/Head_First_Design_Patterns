package Chapter6_v1;

public class LightOnCommand implements Command{
    private final Light light;
    LightOnCommand(Light light){
        this.light = light;
    }
    @Override
    public void execute() {
        this.light.turnOn();
    }
}
