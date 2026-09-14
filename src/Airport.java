//Name: Junesuh Kang
//Student Number: 1239956

public class Airport {

    // Airport state constants — used instead of a boolean to clearly
    // distinguish between actively closing and fully closed
    private static final int OPEN = 0;
    private static final int CLOSING = 1;
    private static final int CLOSED = 2;

    // Current state of the airport
    private int state;
    // Number of runways and gates not currently in use
    private int freeRunways;
    private int freeGates;
    // Aircraft that have entered requestLanding() but not yet acquired resources
    private int waitingToLand;
    // Aircraft currently parked at a gate
    private int aircraftOnGround;

    public Airport() {
        this.state = OPEN;
        this.freeRunways = Params.RUNWAYS;
        this.freeGates = Params.GATES;
        this.waitingToLand = 0;
        this.aircraftOnGround = 0;
    }

    // Called by an Aircraft thread requesting to land.
    // Returns true if landing was granted, false if airport is closed.
    // Aircraft must obtain both a runway and a gate before landing begins.
    public synchronized boolean requestLanding() {
        Params.log("Requesting landing");

        // Reject immediately if airport is not open
        if (state != OPEN){
            Params.log("Cannot land, airport closed");
            return false;
        }

        // Register intent — used by requestTakeoff() to implement arrivals priority
        waitingToLand++;

        // Wait until both a runway and a gate are free
        while(freeRunways == 0 || freeGates == 0){
            try {
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                waitingToLand--;
                return false;
            }
        }

        // Recheck state — airport may have begun closing while we were waiting
        if (state != OPEN){
            waitingToLand--;
            Params.log("Cannot land, airport closed");
            notifyAll();
            return false;
        }

        // Atomically acquire both resources and register aircraft on ground
        waitingToLand--;
        freeGates--;
        freeRunways--;
        aircraftOnGround++;
        Params.log("Landing");
        return true;
    }

    // Called by an Aircraft after landing is complete.
    // Releases the runway but keeps the gate for turnaround.
    public synchronized void releaseRunwayAfterLanding(){
        freeRunways++;
        Params.log("At gate");
        notifyAll(); // free runway may unblock another landing or takeoff
    }

    // Called by an Aircraft at a gate that wants to depart.
    // Arrivals priority: yields to waiting arrivals if they can actually land
    // (i.e. a free gate exists). If no gates are free, proceeds regardless.
    public synchronized void requestTakeoff(){
        Params.log("Requesting takeoff");

        while(freeRunways == 0 || (waitingToLand > 0 && state == OPEN && freeGates > 0)) {
            try{
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        freeRunways--;
        Params.log("Taking off");
    }

    // Called by an Aircraft after takeoff is complete.
    // Releases both the runway and the gate, logs departure.
    public synchronized void releaseAfterTakeoff() {
        Params.log("Departed");
        freeRunways++;
        freeGates++;
        aircraftOnGround--;
        notifyAll(); // freed resources may unblock arrivals or departures
    }

    // Called by the Operator to begin closing the airport.
    // Prevents new landings, waits for all aircraft on ground to depart,
    // then sets state to CLOSED.
    public synchronized void close() {
        Params.log("Operator begins closing airport.");
        state = CLOSING;
        notifyAll(); // wake any aircraft blocked in requestLanding so they can recheck state

        Params.log("Waiting for all aircraft to depart...");
        // Block until every aircraft that landed has taken off
        while (aircraftOnGround > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        state = CLOSED;
        Params.log("Airport is now fully closed.");
    }

    // Called by the Operator to reopen the airport after the closed period.
    public synchronized void open() {
        state = OPEN;
        Params.log("Airport is open. Normal operation.");
        notifyAll(); // wake aircraft that were retrying outside the monitor
    }

}