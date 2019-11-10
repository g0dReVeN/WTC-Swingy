package swingy.model.profession;

import swingy.model.artifact.*;

public class Human implements HeroBuilder {
    private Profession hero;

    public Human(String name) {
        this.hero = new Profession();
        hero.setName(name);
    }

    public void buildArtifact() {
        hero.setWeapon(new Weapon());
        hero.setArmour(new Armour());
        hero.setHelm(new Helm());
    }

    public void buildType() {
        hero.setType("Human");
    }

    public void buildXP() {
        hero.setXP(0);
    }

    public void buildLvl() {
        hero.setLvl(1);
    }

    public void buildLvlUp() {
        hero.setLvlUp(1000);
    }

    public void buildMaxHP() {
        hero.setMaxHP(25);
    }

    public void buildHP() {
        hero.setHP(25);
    }

    public void buildAtk() {
        hero.setAtk(25);
    }

    public void buildDef() {
        hero.setDef(25);
    }

    public Profession getHero() {
        return this.hero;
    }
}
