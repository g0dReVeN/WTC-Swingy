package swingy.controller;

import swingy.model.artifact.*;
import swingy.model.profession.Profession;

import java.util.Random;

public class MechanicsController {
    private static Random random = new Random();

    public static boolean winGame(MapController mapDirector) {
        return (mapDirector.heroI == 0 || mapDirector.heroJ == 0 || mapDirector.heroI == mapDirector.size - 1 || mapDirector.heroJ == mapDirector.size - 1);
    }

    public static boolean isNotOccupied(int direction, MapController mapDirector) {
        switch (direction) {
            case 1:   return mapDirector.getMap()[mapDirector.heroI - 1][mapDirector.heroJ] == 0;
            case 2:   return mapDirector.getMap()[mapDirector.heroI + 1][mapDirector.heroJ] == 0;
            case 3:   return mapDirector.getMap()[mapDirector.heroI][mapDirector.heroJ - 1] == 0;
            case 4:   return mapDirector.getMap()[mapDirector.heroI][mapDirector.heroJ + 1] == 0;
            default: throw new IllegalArgumentException();
        }
    }

    public static void move(int direction, MapController mapDirector) {
        switch (direction) {
            case 1: mapDirector.goNorth();
                    break;
            case 2: mapDirector.goSouth();
                    break;
            case 3: mapDirector.goWest();
                    break;
            case 4: mapDirector.goEast();
                    break;
            default: throw new IllegalArgumentException();
        }
    }

    public static Profession findVillain(int direction, MapController mapDirector) {
        switch (direction) {
            case 1: return mapDirector.getVillain(mapDirector.getMap()[mapDirector.heroI - 1][mapDirector.heroJ]);
            case 2: return mapDirector.getVillain(mapDirector.getMap()[mapDirector.heroI + 1][mapDirector.heroJ]);
            case 3: return mapDirector.getVillain(mapDirector.getMap()[mapDirector.heroI][mapDirector.heroJ - 1]);
            case 4: return mapDirector.getVillain(mapDirector.getMap()[mapDirector.heroI][mapDirector.heroJ + 1]);
            default: throw new IllegalArgumentException();
        }
    }

    private static void lvlHero(Profession hero) {
        hero.setLvl(hero.getLvl() + 1);
        hero.setMaxHP(hero.getMaxHP() + 5);
        hero.setDef(hero.getDef() + 2);
        hero.setAtk(hero.getAtk() + 2);
    }

    private static void earnXP(Profession hero, int modifier) {
        int rate = modifier > hero.getLvl() ? modifier - 1 == hero.getLvl() ? 1 : 2 : 0;
        hero.setXP(hero.getXP() + (int)(400.0 * (1.0 + 0.3 * rate)));
        if (hero.getXP() >= hero.getLvlUp()) {
            lvlHero(hero);
            hero.setLvlUp((int)(hero.getLvl() * 1000 + Math.pow((hero.getLvl() - 1), 2) * 450));
        }
    }

    public static boolean run() {
        return random.nextInt(2) > 0;
    }

    public static boolean fight(int direction, MapController mapDirector, Profession hero) {
        Profession villain = findVillain(direction, mapDirector);
        int crit;

        while (villain.getHP() > 0 && hero.getHP() > 0) {
            crit = random.nextInt(100) >= 90 ? 2 : 1;
            villain.setHP(villain.getHP() + villain.getDef() - (hero.getAtk() + hero.getWeapon().getBoost()) * crit);
            if (villain.getHP() <= 0)
                break;
            hero.setHP(hero.getHP() + hero.getDef() + hero.getHelm().getBoost() + hero.getArmour().getBoost() - villain.getAtk());
        }
        if (hero.getHP() > 0) {
            earnXP(hero, villain.getLvl());
            return true;
        }
        return false;
    }

    public static void equipWeapon(Profession hero, Weapon weapon) {
        hero.setWeapon(weapon);
    }

    public static void equipArmour(Profession hero, Armour armour) {
        hero.setArmour(armour);
    }

    public static void equipHelm(Profession hero, Helm helm) {
        hero.setHelm(helm);
    }
}
