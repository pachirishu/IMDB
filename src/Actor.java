import java.util.List;
import java.util.Map;
public class Actor implements Comparable<Actor>{
    String name;
    List<Map<String, String>> performances;
    String biography;

    public Actor(String name) {
        this.name = name;
        this.performances = null;
        this.biography = "";
    }
    public Actor(String name, List<Map<String, String>> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }
    @Override
    public int compareTo(Actor otherActor) {
        return this.name.compareTo(otherActor.name);
    }
}
