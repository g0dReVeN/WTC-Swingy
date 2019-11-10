package swingy.model.artifact;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
public class Weapon extends Artifact {
    Random random = new Random();

    public Weapon() {
        this.name = "";
        this.lvl = 0;
        this.boost = 0;
    }

    public Weapon(String type, int lvl, int current) {
        this.boost = current + random.nextInt(10);

        Map<String, String> classType = new HashMap<String, String>();

        classType.put("Human", "Great Sword");
        classType.put("Elf", "Great Bow");
        classType.put("Dwarf", "Great Axe");

        this.name = classType.get(type);

        this.lvl = lvl;

        this.boost = current + random.nextInt(10);
    }
}
