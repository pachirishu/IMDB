import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
public class Movie  extends Production{
    @SerializedName("duration")
    String duration;
    @SerializedName("releaseYear")
    int releaseYear;

    private List<Observer> users = new ArrayList<>();
    private String latestRating;
    public Movie() {
        super();
        this.duration = null;
        this.releaseYear = 0;
    }
    public Movie(String name, String type,List<String> directors, List<String> cast, List<Genre> genres, List<Rating> ratings, String subject, Double metascore, String duration, int releaseYear) {
        super(name, type, directors, cast, genres, ratings, subject, metascore);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo() {
        if(name != null)
            System.out.println("Title: " + name);
        if(directors != null)
            System.out.println("Directors: " + directors);
        if(actors != null)
            System.out.println("Cast: " + actors);
        if(genres != null)
            System.out.println("Genres: " + genres);
        if(ratings != null) {
            System.out.println("Ratings: ");
            for (Rating rating : ratings) {
                rating.displayInfo();
            }
        }
        if(plot != null)
            System.out.println("Subject: " + plot);
        if(averageRating != null)
            System.out.println("Metascore: " + averageRating);
        if(duration != null)
            System.out.println("Duration: " + duration);
        if(releaseYear != 0)
            System.out.println("Release Date: " + releaseYear);

    }

    public void uploadMovie(String movieRating) {
        latestRating = movieRating;
        notifyObservers(movieRating);
    }

    public void registerObserver(Observer observer) {
        users.add(observer);
    }

    public void removeObserver(Observer observer) {
        users.remove(observer);
    }

    public void notifyObservers(String movieRating) {
        for (Observer observer : users) {
            observer.update(movieRating);
        }
    }




}
