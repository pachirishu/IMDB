import java.time.LocalDateTime;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
public class Request implements Subject{
    @SerializedName("type")
    private RequestTypes requestType;
    @SerializedName("createdDate")
    private LocalDateTime requestTime;
    @SerializedName("username")
    String user;
    @SerializedName("movieTitle")
    String productionTitle;
    @SerializedName("actorName")
    String actorName;
    @SerializedName("to")
    String userSolver;
    @SerializedName("description")
    String problem;

    private List<Observer> observers = new ArrayList<>();
    private String latestRequest;

    public Request() {
        this.requestType = null;
        this.requestTime = null;
        this.productionTitle = null;
        this.actorName = null;
        this.problem = null;
        this.user = null;
        this.userSolver = null;
    }
    public Request(RequestTypes requestType, LocalDateTime requestTime, String user, String productionTitle, String actorName, String problem, String userSolver) {
        this.requestType = requestType;
        this.requestTime = requestTime;
        this.productionTitle = productionTitle;
        this.actorName = actorName;
        this.problem = problem;
        this.user = user;
        this.userSolver = userSolver;
    }


    public RequestTypes getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestTypes requestType) {
        this.requestType = requestType;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    // Observer pattern

    public void uploadRequest(String request) {
        latestRequest = request;
        notifyObservers(request);
    }
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String request) {
        for (Observer observer : observers) {
            observer.update(request);
        }
    }

    void displayInfo(){
        System.out.println("Request type: " + getRequestType());
        System.out.println("Request time: " + getRequestTime());
        System.out.println("Production title: " + productionTitle);
        System.out.println("Actor name: " + actorName);
        System.out.println("Problem: " + problem);
        System.out.println("User: " + user);
        System.out.println("User solver: " + userSolver);
    }


}
