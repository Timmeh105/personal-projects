import java.io.*;
import java.util.*;

// program to plan a user route using Emory shuttle system
public class TripPlanner {
    private final ArrayList<BusRoute> routes; // list of Emory shuttle routes
    private final String filePath; // directory where to find txt files of routes
    private String start; // user-defined starting point
    private String end; // user-defined ending point
    private ArrayList<BusRoute> routesStoppingAtStart; // list of routes that stop at start
    private ArrayList<BusRoute> routesStoppingAtEnd; // list of routes that stop at end
    private Trip trip; // user's suggested trip

    // important! text files Loop.txt, C.txt, A.txt, M.txt, E.txt, D.txt must be in the folder for given filePath!
    // edit the call to this constructor below in main to indicate where your files are (or leave as "" if files are in current working directory)
    public TripPlanner(String filePath){
        this.filePath = filePath;
        routes = new ArrayList<BusRoute>();
        routes.add(buildRoute("Loop"));
        routes.add(buildRoute("C"));
        routes.add(buildRoute("A"));
        routes.add(buildRoute("M"));
        routes.add(buildRoute("E"));
        routes.add(buildRoute("D"));
    }

    // loads the stops for each Emory Shuttle Route and builds BusRoute objects for each
    public BusRoute buildRoute(String name){
        BusRoute r = new BusRoute(name);
        Scanner scanner;
        try{
            scanner = new Scanner(new FileInputStream(filePath+name + ".txt"));
        }
        catch(IOException ex){
            System.err.println("*** Invalid filename: " + filePath + name + ".txt");
            return null;
        }
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            r.addStop(line);
        }
        return r;
    }

    // sets start
    public void setStart(String start){
        this.start = start;
    }

    // sets end
    public void setEnd(String end){
        this.end = end;
    }

    // returns list of routes stopping at start
    public ArrayList<BusRoute> getRoutesStoppingAtStart(){
        return routesStoppingAtStart;
    }

    // returns list of routes stopping at end
    public ArrayList<BusRoute> getRoutesStoppingAtEnd(){
        return routesStoppingAtEnd;
    }

    // returns true if start and stop are valid stops; false otherwise
    public boolean verifyInput(){
        routesStoppingAtStart = getRoutesStoppingAt(start);
        routesStoppingAtEnd = getRoutesStoppingAt(end);
        if(routesStoppingAtStart.isEmpty()){
            System.out.println("No routes found stopping at start location!");
            return false;
        }
        if(routesStoppingAtEnd.isEmpty()){
            System.out.println("No routes found stopping at end location!");
            return false;
        }
        return true;
    }

    // prints trip from start to end
    public void displayTrip(){
        trip.displayTrip();
    }

    // returns trip
    public Trip getTrip(){
        return trip;
    }

    public ArrayList<BusRoute> getRoutesStoppingAt(String stopName){
        ArrayList<BusRoute> routesStoppingAt = new ArrayList<BusRoute>();
        // cycle routes
        for (int i = 0; i < routes.size(); i++) {
            BusRoute route = routes.get(i);
            // adds routes with the stops
            if (route.stopsAt(stopName)) {
                routesStoppingAt.add(route);
            }
        }
        return routesStoppingAt;
    }

    public void buildTrip(){
        // this initializes the trip
        trip = new Trip();
        // this finds a common route
        for (int i = 0; i < routesStoppingAtStart.size(); i++) {
            for (int j = 0; j < routesStoppingAtEnd.size(); j++) {
                // case where we do it in one route
                if (routesStoppingAtStart.get(i).getName().equals(routesStoppingAtEnd.get(j).getName())) {
                    // this adds stops to the trip from start to end on the common route
                    BusStop currentStop = routesStoppingAtStart.get(i).getStart();
                    boolean addingStops = false;
                    // pushes currentStop from root start to trip start
                    while (!currentStop.getName().equals(start)) {
                        currentStop = currentStop.getNext();
                    }
                    // currentStart starts art trip start and adds every route along start to end
                    while (!currentStop.getName().equals(end)) {
                        trip.addStop(currentStop.getName(), routesStoppingAtStart.get(i).getName());
                        currentStop = currentStop.getNext();
                    }
                    trip.addStop(currentStop.getName(), routesStoppingAtStart.get(i).getName());
                    return;
                }
            }
        }
        // case where two routes are needed
        BusRoute first = routesStoppingAtStart.get(0);
        BusRoute second = routesStoppingAtEnd.get(0);
        String transfer = first.getTransferPoints(second).get(0);
        BusStop currentStop = first.getStart();
        // push from route start to trip start
        while (!currentStop.getName().equals(start)) {
            currentStop = currentStop.getNext();
        }
        // pushes it from trip start to transfer
        while (!currentStop.getName().equals(transfer)) {
            trip.addStop(currentStop.getName(), first.getName());
            currentStop = currentStop.getNext();
        }
        // adding last stop
        trip.addStop(currentStop.getName(), first.getName());
        currentStop = second.getStart();
        // looped from second route start to transfer
        while (!currentStop.getName().equals(transfer)) {
            currentStop = currentStop.getNext();
        }
        // looped from transfer point to the end of the trip
        while (!currentStop.getName().equals(end)) {
            trip.addStop(currentStop.getName(), second.getName());
            currentStop = currentStop.getNext();
        }
        trip.addStop(currentStop.getName(), second.getName());
    }
    
    public static void main(String[] args) {
        // 1. Build Emory Bus Routes
        TripPlanner tp = new TripPlanner("");

        // 2. Take in start and end points from user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Emory Trip Planner!");
        // Read user input for starting and ending location names
        System.out.print("Enter your starting location: ");
        tp.setStart(scanner.nextLine());
        System.out.print("Enter your ending location: ");
        tp.setEnd(scanner.nextLine());

        // 3. Print bus routes stopping at start and end locations
        if (!tp.verifyInput()) {
            return;
        }
        System.out.println("Routes stopping at start location: " + tp.getRoutesStoppingAtStart());
        System.out.println("Routes stopping at end location: " + tp.getRoutesStoppingAtEnd());

        // 3. Build linked list for trip
        tp.buildTrip();

        // 4. print step by step directions for user
        System.out.println("Your Trip:");
        tp.displayTrip();
    }
}
