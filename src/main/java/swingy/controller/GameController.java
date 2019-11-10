package swingy.controller;

import swingy.model.artifact.*;
import swingy.model.profession.*;
import swingy.Swingy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class GameController {
    public Profession hero;
    public HeroBuilder heroB;
    public ProfessionController profDirector;
    public MapController mapDirector;
    public DatabaseController dbDirector = new DatabaseController();
    public Scanner scan = new Scanner(System.in);
    
    public void newHero(int option, String name) throws SQLException {
        switch (option) {
            case 1: heroB = new Human(name);
                    break;
            case 2: heroB = new Elf(name);
                    break;
            case 3: heroB = new Dwarf(name);
                    break;
            default: throw new IllegalArgumentException();
        }
        profDirector = new ProfessionController(heroB);
        profDirector.buildHero();
        hero = profDirector.getHero();

        dbDirector.createHero(hero, name);

        mapDirector = new MapController(10 - (1 % 2));
    }
    
    public void loadHero(int heroID) throws SQLException {
        dbDirector.getResultSet().absolute(heroID);
        switch (dbDirector.getResultSet().getString("class")) {
            case "Human": heroB = new Human(dbDirector.getResultSet().getString("name"));
                break;
            case "Elf": heroB = new Elf(dbDirector.getResultSet().getString("name"));
                break;
            case "Dwarf": heroB = new Dwarf(dbDirector.getResultSet().getString("name"));
                break;
            default: throw new IllegalArgumentException();
        }
        profDirector = new ProfessionController(heroB);
        profDirector.buildHero();
        hero = profDirector.getHero();

        hero.setXP(dbDirector.getResultSet().getInt("XP"));
        hero.setLvl(dbDirector.getResultSet().getInt("lvl"));
        hero.setLvlUp(dbDirector.getResultSet().getInt("lvlUp"));
        hero.setMaxHP(dbDirector.getResultSet().getInt("maxHP"));
        hero.setHP(dbDirector.getResultSet().getInt("HP"));
        hero.setAtk(dbDirector.getResultSet().getInt("atk"));
        hero.setDef(dbDirector.getResultSet().getInt("def"));
        hero.getWeapon().setName(dbDirector.getResultSet().getString("wname"));
        hero.getWeapon().setLvl(dbDirector.getResultSet().getInt("wlvl"));
        hero.getWeapon().setBoost(dbDirector.getResultSet().getInt("wboost"));
        hero.getArmour().setName(dbDirector.getResultSet().getString("aname"));
        hero.getArmour().setLvl(dbDirector.getResultSet().getInt("alvl"));
        hero.getArmour().setBoost(dbDirector.getResultSet().getInt("aboost"));
        hero.getHelm().setName(dbDirector.getResultSet().getString("hname"));
        hero.getHelm().setLvl(dbDirector.getResultSet().getInt("hlvl"));
        hero.getHelm().setBoost(dbDirector.getResultSet().getInt("hboost"));

        mapDirector = new MapController((hero.getLvl() - 1) * 5 + 10 - (hero.getLvl() % 2));
    }
}
