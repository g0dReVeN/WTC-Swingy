package swingy.controller;

import swingy.model.profession.Profession;
import swingy.model.profession.HeroBuilder;
import swingy.model.profession.VillainBuilder;

public class ProfessionController {
    private HeroBuilder heroBuilder;
    private VillainBuilder villainBuilder;

    public ProfessionController(HeroBuilder heroBuilder) {
        this.heroBuilder = heroBuilder;
    }

    public ProfessionController(VillainBuilder villainBuilder) {
        this.villainBuilder = villainBuilder;
    }

    public Profession getHero() {
        return this.heroBuilder.getHero();
    }

    public Profession getVillain() {
        return this.villainBuilder.getVillain();
    }

    public void buildHero() {
        this.heroBuilder.buildArtifact();
        this.heroBuilder.buildType();
        this.heroBuilder.buildXP();
        this.heroBuilder.buildLvl();
        this.heroBuilder.buildLvlUp();
        this.heroBuilder.buildMaxHP();
        this.heroBuilder.buildHP();
        this.heroBuilder.buildAtk();
        this.heroBuilder.buildDef();
    }

    public void buildVillain() {
        this.villainBuilder.buildType();
        this.villainBuilder.buildXP();
        this.villainBuilder.buildLvl();
        this.villainBuilder.buildMaxHP();
        this.villainBuilder.buildHP();
        this.villainBuilder.buildAtk();
        this.villainBuilder.buildDef();
    }
}