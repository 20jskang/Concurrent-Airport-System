//Name: Junesuh Kang
//Student Number: 1239956

public class Operator extends Thread{
    private final Airport airport;

    public Operator(Airport airport) {
        this.airport = airport;
    }

    public void run() {
        while (true) {

            Params.sleep(Params.OPEN_DURATION);

            airport.close();

            Params.sleep(Params.CLOSED_DURATION);

            airport.open();
        }
    }
}
