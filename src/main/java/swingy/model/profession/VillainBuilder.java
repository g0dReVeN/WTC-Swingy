package swingy.model.profession;

public interface VillainBuilder {
    public void buildType();
    public void buildXP();
    public void buildLvl();
    public void buildMaxHP();
    public void buildHP();
    public void buildAtk();
    public void buildDef();
    public Profession getVillain();
}
