public class RequestExperience implements ExperienceStrategy{
    private int request;
    public RequestExperience(int request) {
        this.request = request;
    }
    @Override
    public int calculateExperience() {
        return request;
    }

}
