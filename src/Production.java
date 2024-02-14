import com.google.gson.annotations.SerializedName;
import java.util.List;

abstract class Production implements Comparable, Subject{
    @SerializedName("title")
    String name;
    @SerializedName("type")
    String type;
    @SerializedName("directors")
    List<String> directors;
    @SerializedName("actors")
    List<String> actors;
    @SerializedName("genres")
    List<Genre> genres;
    @SerializedName("ratings")
    List<Rating> ratings;
    @SerializedName("plot")
    String plot;
    @SerializedName("averageRating")
    Double averageRating;
    public abstract void displayInfo();
    public int compareTo(Object o){
        Production p = (Production) o;
        return this.name.compareTo(p.name);
    }
    public Production() {
        this.name = null;
        this.type = null;
        this.directors = null;
        this.actors = null;
        this.genres = null;
        this.ratings = null;
        this.plot = null;
        this.averageRating = null;
    }

    public Production(String title, String type, List<String> directors, List<String> cast, List<Genre> genres, List<Rating> ratings, String subject, Double metascore) {
        this.name = title;
        this.type = type;
        this.directors = directors;
        this.actors = cast;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = subject;
        this.averageRating = metascore;
    }
}
