import java.util.ArrayList;

// a class for a circular, doubly-linked list representing a bus route
public class BusRoute {
    private final String name; // name of the route
    private BusStop start; // starting bus stop (head of the linked list)

    public BusRoute(String name){
        this.name = name;
        start = null;
    }

    // return route name
    public String getName(){
        return name;
    }

    // return starting bus stop
    public BusStop getStart(){
        return start;
    }

    public void addStop(String name){
        BusStop newStop = new BusStop(name, this.name, null, null);
        // case where we start with an empty route
        if (start == null) {
            start = newStop;
            start.setNext(start);
            start.setPrevious(start);
        }
        // normal case adds to the end
        else {
            BusStop last = start.getPrevious();
            last.setNext(newStop);
            newStop.setPrevious(last);
            newStop.setNext(start);
            start.setPrevious(newStop);
        }
    }

    public void addStop(String name, String prevStop){
        BusStop current = start;
        // case where we have an empty list
        if (current == null) {
            return;
        }
        // if we do not have previous stop can't add new stop
        if (!this.stopsAt(prevStop)) {
            return;
        }
        // cycle stops
        while (current != start.getPrevious()) {
            // once we find the previous stop we can add the new stop
            if (current.getName().equals(prevStop)) {
                // create the new stop and set its links
                BusStop newStop = new BusStop(name, this.name, current, current.getNext());
                BusStop nextStop = current.getNext();
                // this sets the previous stop to point to the next stop
                current.setNext(newStop);
                // this sets the next stop to point backwards to the next stop
                nextStop.setPrevious(newStop);
                return;
            }
            current = current.getNext();
        }
        // checking the last stop bc while loop did not cover this case
        if (current.getName().equals(prevStop)) {
            // creates the new stop and sets its next anf previous links
            BusStop newStop = new BusStop(name, this.name, current, current.getNext());
            BusStop nextStop = current.getNext();
            // current is the previous stop setting it to point to the new stop that is ahead of current
            current.setNext(newStop);
            // setting other stop to point backwards to the new stop
            nextStop.setPrevious(newStop);
        }
    }

    public BusStop removeStop(String name){
        BusStop current = start;
        // case where we are working with an empty list
        if (start == null) {
            return null;
        }
        // case where we do not stop at the stop we want to remove
        if (!this.stopsAt(name)) {
            return null;
        }
        // cycle stops
        while (current != start.getPrevious()) {
            // found stop we want to remove
            if (current.getName().equals(name)) {
                // for first two lines, retrieve the stop before and after
                BusStop prevStop = current.getPrevious();
                BusStop nextStop = current.getNext();
                // set these to point at each other instead of the stop we are removing
                prevStop.setNext(nextStop);
                nextStop.setPrevious(prevStop);
                // if we remove start then we want to update start to point at the new first stop
                if (current == start) {
                    start = nextStop;
                }
                return current;
            }
            current = current.getNext();
        }
        // checks very last node bc while loop does not cover it
        if (current.getName().equals(name)) {
            BusStop prevStop = current.getPrevious();
            BusStop nextStop = current.getNext();
            prevStop.setNext(nextStop);
            nextStop.setPrevious(prevStop);
            // if we remove start then we want to update start to point at the new first stop
            if (current == start) {
                start = nextStop;
            }
            // case where start is the only thing (one element bus route)
            if (current == start) {
                start = null;
            }
            return current;
        }
        return null;
    }

    public BusStop getNextStop(String stopName){
        BusStop current = start;
        // case where the route is empty
        if (start == null) {
            return current;
        }
        // checks if the stop cycled through is the stop
        while (current != start.getPrevious()) {
            if (current.getName().equals(stopName)) {
                return current.getNext();
            }
            current = current.getNext();
        }
        // checks the last stop
        if (current.getName().equals(stopName)) {
            return current.getNext();
        }
        return null;
    }

    public boolean stopsAt(String stopName){
        BusStop current = start;
        // case where route is empty
        if (start == null) {
            return false;
        }
        // checks if the first stop is the one we are looking for
        if (current.getName().equals(stopName)) {
            return true;
        }
        // cycle through every stop and checks if the stop is the one we are looking for
        while (current != start.getPrevious()) {
            if (current.getName().equals(stopName)) {
                return true;
            }
            current = current.getNext();
        }
        // checks the last stop
        if (current.getName().equals(stopName)) {
            return true;
        }
        // if we get this far, the stop is never found
        return false;
    }

    public ArrayList<String> getTransferPoints(BusRoute otherRoute){
        ArrayList<String> transferPoints = new ArrayList<>();
        BusStop current = start;
        // case where route is empty
        if (current == null) {
            return transferPoints;
        }
        // cycling through stops and checks if the other route stops there
        while (current != start.getPrevious()) {
            // if the route stops here adds the stop to transferPoints
            if (otherRoute.stopsAt(current.getName())) {
                transferPoints.add(current.getName());
            }
            current = current.getNext();
        }
        // checks the last stop
        if (otherRoute.stopsAt(current.getName())) {
            transferPoints.add(current.getName());
        }
        return transferPoints;
    }

    // represents route using name of the route
    @Override
    public String toString() {
        return "Bus Route " + name;
    }

    // displays the route (forwards) from start to the last stop before start again
    public void displayRoute(){
        String res = "";
        if(start == null){
            res += "[NULL]";
        }
        else {
            res += start.toString();
            BusStop c = start.getNext();
            while (c != start) {
                res += " -> " + c.toString();
                c = c.getNext();
            }
        }
        System.out.println(res);
    }

    // displays the route (backwards) from start to the last stop before start again to the second stop
    public void displayRouteBackwards(){
        String res = "";
        if(start == null){
            res += "[NULL]";
        }
        else {
            res += start.toString();
            BusStop c = start.getPrevious();
            while (c != start) {
                res += " -> " + c.toString();
                c = c.getPrevious();
            }
        }
        System.out.println(res);
    }
}