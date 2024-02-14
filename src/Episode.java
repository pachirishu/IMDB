import com.google.gson.annotations.SerializedName;

public class Episode {
    @SerializedName("episodeName")
    String title;
    @SerializedName("duration")
    String duration;

    public Episode(String title, String duration) {
        this.title = title;
        this.duration = duration;
    }

    public void displayInfo(){
        System.out.println("Title: " + title);
        System.out.println("Duration: " + duration);
    }
}
