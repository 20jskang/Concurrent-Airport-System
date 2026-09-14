// SWEN90004 2026 Assignment 1a
//
// The Params class provides system and timing parameters and utility functions
//
// Nic Geard - ngeard@unimelb.edu.au
// 6 March 2026
//Name: Junesuh Kang
//Student Number: 1239956

import java.util.Random;

public class Params {
	
    private static final Random rand = new Random();

    // --- System parameters ------
    
    // the number of runways at Semaphore airport
    static int RUNWAYS = 1;
//    static int RUNWAYS = 3;
    
    // the number of gates at Semaphora airport
//    static int GATES = 3;
    static int GATES = 5;
    
    // --- Timing parameters ------
    
    // the minimum and maximum duration between arriving aircraft    
//    static int ARRIVAL_MIN = 500;
//    static int ARRIVAL_MAX = 1500;
//    static int ARRIVAL_MIN = 100;
//    static int ARRIVAL_MAX = 300;
//    static int ARRIVAL_MIN = 50;
//    static int ARRIVAL_MAX = 150;
    static int ARRIVAL_MIN = 150;
    static int ARRIVAL_MAX = 600;

    // the length of time an aircraft arriving at a closed airport will wait
    // before making a further request to land
    static int WAIT_ON_CLOSURE = 1000;
    
    // the minimum and maximum duration for an aircraft to land
    static int LANDING_MIN = 100;
    static int LANDING_MAX = 300;
    
    // the minimum and maximum duration for an aircraft to turnaround
    // (ie, spend at a gate between landing and takeoff)
    static int TURNAROUND_MIN = 500;
    static int TURNAROUND_MAX = 1000;
//    static int TURNAROUND_MIN = 200;
//    static int TURNAROUND_MAX = 400;
    
    // the minimum and maximum duration for an aircraft to takeoff
    static int TAKEOFF_MIN = 100;
    static int TAKEOFF_MAX = 300;
    
    // the duration that an airport is open or closed
    static int OPEN_DURATION = 5000;
    static int CLOSED_DURATION = 2000;
    
    // --- Sleep functions ------
    
    // cause the calling thread to sleep for a specified duration
    static void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // cause the calling thread to sleep for a random duration in the range [min, max]
    static void sleepRandom(int min, int max) {
        sleep(min + rand.nextInt(max - min));
    }

    // --- Logging function ------
    
    // cause the calling thread to write out the specified message
    static void log(String msg) {
        System.out.printf("[%s] %s%n",
                Thread.currentThread().getName(), msg);
    }
}
