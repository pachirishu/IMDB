import com.google.gson.annotations.SerializedName;

import java.util.*;
public abstract class Staff<T> extends User<T> implements StaffInterface{
    @SerializedName("requests")
    List<Request> requests;
    @SerializedName("Contribution")
    SortedSet<T> addedProductions;
    public Staff(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, List<Request> requests, SortedSet<T> addedProductions) {
        super(info, accountType, username, experience, notifications, favorites);
        this.requests = requests;
        this.addedProductions = addedProductions;
    }
    abstract void addFavorite(T favorite);
    abstract void removeFavorite(T favorite);
    abstract void updateExperience(int exp);
    abstract boolean logout();
    public abstract void addProductionSystem(Production p);
    public abstract void addActorSystem(Actor a);
    public abstract void removeProductionSystem(String name);
    public abstract void removeActorSystem(String name);
    public abstract void updateProduction(Production p);
    public abstract void updateActor(Actor a);
    public abstract void solveRequest(Request request);

}
