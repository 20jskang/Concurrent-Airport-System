//Name: Junesuh Kang
//Student Number: 1239956

public class Aircraft extends Thread {

    private final Airport airport;

    public Aircraft(Airport airport) {
        this.airport = airport;
    }

    public void run(){
        try{
            while(!airport.requestLanding()){
                Thread.sleep(Params.WAIT_ON_CLOSURE);
            }

            Params.sleepRandom(Params.LANDING_MIN, Params.LANDING_MAX);

            airport.releaseRunwayAfterLanding();

            // Simulate turnaround at gate
            Params.sleepRandom(Params.TURNAROUND_MIN, Params.TURNAROUND_MAX);

            airport.requestTakeoff();

            // Simulate takeoff roll
            Params.sleepRandom(Params.TAKEOFF_MIN, Params.TAKEOFF_MAX);

            airport.releaseAfterTakeoff();

        } catch (InterruptedException e) {}
    }
}
