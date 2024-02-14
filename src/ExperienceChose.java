public class ExperienceChose {
    private ExperienceStrategy strategy;
    public void setExperienceStrategy(ExperienceStrategy strategy) {
        this.strategy = strategy;
    }
    public int calculateExperience() {
        return strategy.calculateExperience();
    }
}
