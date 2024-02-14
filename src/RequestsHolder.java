import java.util.ArrayList;
import java.util.List;
public class RequestsHolder {
    static List<Request> requests = new ArrayList<>();
    public static void addARequest(Request request){
        requests.add(request);
    }
    public static void removeARequest(Request request){
        requests.remove(request);
    }

}
