
/*
 * Author: Yash Agarwal
 * CS 210, Spring 21
 * Purpose: This file tries to find a solution to the famous
 * Traveling Salesperson problem using three approaches, heuristic, 
 * backtracking and an improved backtracking algorithm. It takes in a .mtx file 
 * as the first command line argument and the command to be performed as the second.
 * It uses the Graph object from the DGraph file to perform these tasks and prints
 * out the path and the cost and the time depending on the command.
 * 
 * Valid commands- HEURISTIC, BACKTRACK, MINE, TIME
 * 
 * Valid input- PathTo/infile.mtx [HEURISTIC, BACKTRACK, MINE, TIME]
 * 
 * Sample output for HEURUSTIC AND BACKTRACK for the example.mtx file
 * 
 * cost = 10.0, visitOrder = [1, 2, 3]
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PA11Main {
    public static void main(String[] args) {
        DGraph obj = null;
        try {
            obj = readFile(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }
        handleCommand(args[1], obj);

    }

    /*
     * handles the command that has been passed and calls the
     * respective function to perform that task
     * 
     * @param- command- the command as a string
     * obj- the Graph object
     * return- none
     */
    public static void handleCommand(String command, DGraph obj) {
        if (command.equals("HEURISTIC")) {
            Trip heuristicTrip = heuristic(obj);
            System.out.println(heuristicTrip.toString(obj));
        }
        if (command.equals("BACKTRACK")) {
            Trip backtrackingTrip = new Trip(obj.getNumNodes());
            // trip begins from city 1
            backtrackingTrip.chooseNextCity(1);
            // using an empty trip as the third parameter
            backtrackingTrip = backtracking(obj, backtrackingTrip,
                    new Trip(obj.getNumNodes()));
            System.out.println(backtrackingTrip.toString(obj));
        }

         if (command.equals("MINE")) {
         Trip mineTrip = new Trip(obj.getNumNodes());
         mineTrip.chooseNextCity(1);
         mineTrip = mine(obj, mineTrip, new Trip(obj.getNumNodes()));
         System.out.println(mineTrip.toString(obj));
         }
        if (command.equals("TIME")) {
            timeCommand(obj);
        }
    }

    /*
     * This function performs the task when TIME is called.
     * It performs all the three approaches and prints the costs
     * and the time each took.
     * 
     * @param- obj- the graph object
     * return- none
     */
    public static void timeCommand(DGraph obj) {
        long startTime = System.nanoTime();
        Trip heuristicTrip = heuristic(obj);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("heuristic: cost = " + heuristicTrip.tripCost(obj)
                + ", " + duration + " milliseconds");

        Trip backtrackingTrip = new Trip(obj.getNumNodes());
        backtrackingTrip.chooseNextCity(1);
        backtrackingTrip = backtracking(obj, backtrackingTrip,
                new Trip(obj.getNumNodes()));
        startTime = System.nanoTime();
        Trip backtrackingTrip1 = new Trip(obj.getNumNodes());
        backtrackingTrip1.chooseNextCity(1);
        backtrackingTrip1 = backtracking(obj, backtrackingTrip1,
                new Trip(obj.getNumNodes()));
        endTime = System.nanoTime();
        double backtrackDuration = (endTime - startTime) / 1000000;

        Trip mineTrip = new Trip(obj.getNumNodes());
        mineTrip.chooseNextCity(1);
        mineTrip = mine(obj, mineTrip, new Trip(obj.getNumNodes()));
        startTime = System.nanoTime();
        Trip mineTrip1 = new Trip(obj.getNumNodes());
        mineTrip1.chooseNextCity(1);
        mineTrip1 = mine(obj, mineTrip1, new Trip(obj.getNumNodes()));
        endTime = System.nanoTime();
        double mineDuration = (endTime - startTime) / 1000000;
        System.out.println("mine: cost = " + mineTrip1.tripCost(obj) + ","
                + mineDuration + " milliseconds");
        System.out.println(
                "backtracking: cost = " + backtrackingTrip.tripCost(obj) + ", "
                        + backtrackDuration + " milliseconds");
    }

    /*
     * function to read in the .mtx file and store it as a graph using
     * the DGraph class.
     * 
     * @param-loc- location of the .mtx file
     * return- DGraph- the graph object with the vertices
     * and the edges
     */
    public static DGraph readFile(String loc) throws IOException {
        File fl = new File(loc);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fl));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean comment = true;
        String line = br.readLine();
        // comments begin with %
        while (comment) {
            line = br.readLine();
            comment = line.startsWith("%");
        }
        String[] str = line.split("( )+");
        // number of cities
        int nRows = (Integer.valueOf(str[0].trim())).intValue();
        DGraph obj = new DGraph(nRows);
        while (true) {
            line = br.readLine();
            if (line == null)
                break;
            str = line.split("( )+");
            obj.addEdge((Integer.valueOf(str[0].trim())).intValue(),
                    (Integer.valueOf(str[1].trim())).intValue(),
                    (Double.valueOf(str[2].trim())).doubleValue());
        }
        return obj;
    }

    /*
     * this function uses the heuristic or the greedy approach
     * to find the path the traveler can take
     * 
     * @param- graph- the graph object
     * return- Trip - the trip the traveler takes
     */
    public static Trip heuristic(DGraph graph) {
        int numCities = graph.getNumNodes();
        Trip trip = new Trip(numCities);
        trip.chooseNextCity(1);
        int currentCity = 1;
        Double minDistance = Double.MAX_VALUE;
        int chooseCity = 2;
        for (int k = 2; k <= numCities; k++) {
            for (int city : graph.getNeighbors(currentCity)) {
                // finds the closest neighbor of the current city
                if (trip.isCityAvailable(city)
                        && graph.getWeight(currentCity, city) < minDistance) {
                    minDistance = graph.getWeight(currentCity, city);
                    chooseCity = city;
                }
            }
            // adds the closest city to the trip
            trip.chooseNextCity(chooseCity);
            currentCity = chooseCity;
            minDistance = Double.MAX_VALUE;
        }
        return trip;
    }

    /*
     * this function uses the backtracking approach to find the
     * shortest path for the traveler to take. It takes all possible
     * routes in consideration and find the shortest route.
     * 
     * @param- graph- the graph object
     * currentTrip- the partial trip that is currently being processed
     * minTrip- the shortest trip that has yet been found
     */
    public static Trip backtracking(DGraph graph, Trip currentTrip,
            Trip minTrip) {
        // complete trip
        if (currentTrip.citiesLeft().isEmpty()) {
            if (currentTrip.tripCost(graph) < minTrip.tripCost(graph))
                minTrip.copyOtherIntoSelf(currentTrip);
            return null;
        }
        if (currentTrip.tripCost(graph) < minTrip.tripCost(graph))
            for (int k : currentTrip.citiesLeft()) {
                // checks every city left and every possible route
                currentTrip.chooseNextCity(k);
                backtracking(graph, currentTrip, minTrip);
                currentTrip.unchooseLastCity();
            }
        return minTrip;
    }

    /*
     * this function builds on the backtracking approach to make it faster
     * and more efficient. It still uses recursive backtracking but adds
     * more pruning and makes more efficient method calls.
     * 
     * @param- graph- the graph object
     * currentTrip- the partial trip that is currently being processed
     * minTrip- the shortest trip that has yet been found
     */
    public static Trip mine(DGraph graph, Trip currentTrip, Trip minTrip) {
        double minTripCost = minTrip.tripCost(graph);
        if (currentTrip.citiesLeft().isEmpty()) {
            if (currentTrip.tripCost(graph) < minTrip.tripCost(graph))
                minTrip.copyOtherIntoSelf(currentTrip);
            return null;
        }
        if (currentTrip.tripCost(graph) < minTrip.tripCost(graph))
            // calls the tripCost() function once since it doesn't change
            // during the loop but is referenced multiple times.
            minTripCost = minTrip.tripCost(graph);
            for (int k : currentTrip.citiesLeft()) {
                currentTrip.chooseNextCity(k);
                // if the cost of the current partial trip is already more than
                // the minTrip cost then discard that route
                if (currentTrip.tripCost(graph) > minTripCost) {
                    currentTrip.unchooseLastCity();
                    continue;
                }
                backtracking(graph, currentTrip, minTrip);
                currentTrip.unchooseLastCity();
            }
        return minTrip;
    }
}
