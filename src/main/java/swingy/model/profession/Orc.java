package swingy.model.profession;

import java.util.Random;

public class Orc implements VillainBuilder {
    private Profession villain;
    private int modifier;

    public Orc(int ID, int modifier) {
        this.modifier = modifier;
        this.villain = new Profession();
        villain.setID(ID);
    }
    
    public void buildType() {
        villain.setType("Orc");
    }

    public void buildXP() {
        villain.setXP(0);
    }

    public void buildLvl() {
        villain.setLvl(this.modifier);
    }

    public void buildMaxHP() {
        villain.setMaxHP(23 + this.modifier * 2);
    }

    public void buildHP() {
        villain.setHP(23 + this.modifier * 2);
    }

    public void buildAtk() {
        villain.setAtk(13 + this.modifier * 2);
    }

    public void buildDef() {
        villain.setDef(8 + this.modifier * 2);
    }

    public Profession getVillain() {
        return this.villain;
    }
}
