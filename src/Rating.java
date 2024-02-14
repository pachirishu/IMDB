public class Rating{
    String username;
    int rating;
    String comment;

    public Rating(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public void displayInfo(){
        System.out.println("Username: " + username);
        System.out.println("Rating: " + rating);
        System.out.println("Comment: " + comment);
    }

}
