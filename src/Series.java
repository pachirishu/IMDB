import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
public class Series extends Production{
    @SerializedName("releaseYear")
    int releaseYear;
    @SerializedName("numSeasons")
    int numSeasons;
    @SerializedName("seasons")
    private Map<String, List<Episode>> seasons;

    private List<Observer> users = new ArrayList<>();
    private String latestRating;
    public Series() {
        super();
        this.releaseYear = 0;
        this.numSeasons = 0;
        this.seasons = null;
    }
    public Series(String title, String type, List<String> directors, List<String> cast, List<Genre> genres, List<Rating> ratings, String subject, Double metascore, int releaseYear, int numSeasons, Map<String, List<Episode>> seasons) {
        super(title, type, directors, cast, genres, ratings, subject, metascore);
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
        this.seasons = seasons;
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
            System.out.println("AverageRating: " + averageRating);
        if(releaseYear != 0)
            System.out.println("Release Year: " + releaseYear);
        if(numSeasons != 0)
            System.out.println("Seasons: " + numSeasons);
        if(seasons != null) {
            for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
                System.out.println(entry.getKey() + ": ");
                for (Episode episode : entry.getValue()) {
                    episode.displayInfo();
                }
            }
        }
    }

    public void uploadSeries(String seriesRating) {
        latestRating = seriesRating;
        notifyObservers(seriesRating);
    }

    public void registerObserver(Observer observer) {
        users.add(observer);
    }

    public void removeObserver(Observer observer) {
        users.remove(observer);
    }

    public void notifyObservers(String seriesRating) {
        for (Observer observer : users) {
            observer.update(seriesRating);
        }
    }
}
