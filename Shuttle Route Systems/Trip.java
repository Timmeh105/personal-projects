public class Trip {
    private BusStop start; // starting stop of the trip

    public Trip(){
        this.start = null;
    }

    // returns the start of the trip
    public BusStop getStart(){
        return start;
    }

    public void addStop(String stopName, String routeName){
        BusStop newStop = new BusStop(stopName, routeName, null, null);
        // if we start with empty trip create start
        if (start == null) {
            start = newStop;
        }
        // the case when the trip is not empty
        else {
            BusStop lastStop = start;
            // pushes to the last stop
            while (lastStop.getNext() != null) {
                lastStop = lastStop.getNext();
            }
            newStop.setPrevious(lastStop);
            lastStop.setNext(newStop);
        }
    }

    public BusStop removeStop(String name, String route){
        // if we start with empty trip
        if (start == null) {
            return null;
        }
        BusStop currentStop = start;
        // cycle stops and search for the stop we want to remove
        while (!currentStop.getName().equals(name)) {
            currentStop = currentStop.getNext();
            // reached the end of the list, and we have not found a stop
            if (currentStop == null) {
                return null;
            }
        }
        BusStop prevStop = currentStop.getPrevious();
        BusStop nextStop = currentStop.getNext();
        // checks if the stop we are removing is the first stop
        if (prevStop != null) {
            prevStop.setNext(nextStop);
        }
        // checks if the stop we are removing is the last stop
        if (nextStop != null) {
            nextStop.setPrevious(prevStop);
        }
        // if we removed start then update what start points to point to the next stop
        if (currentStop == start) {
            start = nextStop;
        }
        return currentStop;
    }

    // displays the trip (forwards) from start to end
    public void displayTrip(){
        String res = "";
        if(start == null){
            res += "[NULL]";
        }
        else {
            res += start.toString();
            BusStop c = start.getNext();
            while (c != null) {
                res += "\n " + c.toString();
                c = c.getNext();
            }
        }
        System.out.println(res);
    }

    // displays the trip (backwards) from end to start
    public void displayTripBackwards(){
        String res = "";
        if(start == null){
            res += "[NULL]";
        }
        else {
            BusStop c = start;
            while(c.getNext() != null){
                c = c.getNext();
            }
            while (c != null) {
                res += "\n " + c.toString();
                c = c.getPrevious();
            }
        }
        System.out.println(res);
    }
}

