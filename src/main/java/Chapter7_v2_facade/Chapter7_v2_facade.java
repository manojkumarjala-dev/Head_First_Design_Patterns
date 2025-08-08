package Chapter7_v2_facade;

public class Chapter7_v2_facade {
    public static void main(String[] args) {
        Amplifier amp = new Amplifier();
        StreamingPlayer player = new StreamingPlayer();
        Projector projector = new Projector();
        TheaterLights lights = new TheaterLights();
        Screen screen = new Screen();
        PopcornPopper popper = new PopcornPopper();

        HomeTheaterFacade theater = new HomeTheaterFacade(
                amp, player, projector, lights, screen, popper
        );

        theater.watchMovie("Raiders of the Lost Ark");
        System.out.println();
        theater.endMovie();
    }
}
