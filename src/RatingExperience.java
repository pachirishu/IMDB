public class RatingExperience implements ExperienceStrategy{
    private int rating;
    public RatingExperience(int rating) {
        this.rating = rating;
    }
    @Override
    public int calculateExperience() {
        return rating;
    }
}
