package swingy.model.profession;

public interface HeroBuilder {
    public void buildArtifact();
    public void buildType();
    public void buildXP();
    public void buildLvl();
    public void buildLvlUp();
    public void buildMaxHP();
    public void buildHP();
    public void buildAtk();
    public void buildDef();
    public Profession getHero();
}
