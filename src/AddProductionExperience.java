public class AddProductionExperience implements ExperienceStrategy{
    private int production;
    public AddProductionExperience(int production) {
        this.production = production;
    }
    @Override
    public int calculateExperience() {
        return production;
    }
}
