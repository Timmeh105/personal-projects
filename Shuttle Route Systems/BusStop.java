import java.util.ArrayList;

// class for a bus stop used to build bus routes and user trips
public class BusStop {
    private final String name; // name of the stop
    private final String route; // name of route associated with this stop
    private BusStop previous; // previous stop on the route/ trip
    private BusStop next; // next stop on the route/ trip

    public BusStop(String name, String route, BusStop prev, BusStop next){
        this.name = name;
        this.route = route;
        this.previous = prev;
        this.next = next;
    }

    public String getName(){
        return name;
    }

    public String getRoute(){
        return route;
    }

    public BusStop getPrevious(){
        return previous;
    }

    public BusStop getNext(){
        return next;
    }

    public void setPrevious(BusStop b){
        previous = b;
    }

    public void setNext(BusStop b){
        next = b;
    }

    public String toString(){
        return "Route: " + route + " Bus Stop: " + name;
    }

    // two bus stops are equal if they have the same stop name
    public boolean equals(Object o){
        if(!(o instanceof BusStop)){
            return false;
        }
        BusStop other = (BusStop) o;
        return other.getName().equals(this.getName());
    }
}
