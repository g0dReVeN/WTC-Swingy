package swingy.controller;

import swingy.model.profession.*;

import java.util.ArrayList;
import java.util.Random;

public class MapController {
    public int[][] map;
    public int size;
    public int heroI, heroJ;
    public ArrayList<Profession> villains;
    private final Random random = new Random();

    public MapController(int size) {
        this.villains = new ArrayList<>();
//        Profession villain;
        VillainBuilder villainB;
        ProfessionController profDirector;

        this.size = size;
        this.map = new int[size][size];

        this.heroJ = this.heroI = size / 2;
        this.map[this.heroI][this.heroJ] = -1;

        for (int i = 1; i <= 0.3 * size * size; i++) {
            villainB = new Orc(i, random.nextInt(size + 2) + 1);
            profDirector = new ProfessionController(villainB);

            profDirector.buildVillain();

//            villain = profDirector.getVillain();
            villains.add(profDirector.getVillain());
        }
        this.fillVillains();
    }

    private void fillVillains() {
        int i,j;

        for (Profession villain : this.villains) {
            do {
                i = random.nextInt(this.size);
                j = random.nextInt(this.size);
            } while (this.map[i][j] != 0);
            this.map[i][j] = villain.getID();
        }
    }

    public void goNorth() {
        this.map[this.heroI][this.heroJ] = 0;
        --this.heroI;
        this.map[this.heroI][this.heroJ] = -1;
    }

    public void goSouth() {
        this.map[this.heroI][this.heroJ] = 0;
        ++this.heroI;
        this.map[this.heroI][this.heroJ] = -1;
    }

    public void goWest() {
        this.map[this.heroI][this.heroJ] = 0;
        --this.heroJ;
        this.map[this.heroI][this.heroJ] = -1;
    }

    public void goEast() {
        this.map[this.heroI][this.heroJ] = 0;
        ++this.heroJ;
        this.map[this.heroI][this.heroJ] = -1;
    }

    public Profession getVillain(int ID) {
        for (Profession villain : this.villains) {
            if (villain.getID() == ID)
                return villain;
        }
        return villains.get(0);
    }

    public int[][] getMap() {
        return this.map;
    }
}
