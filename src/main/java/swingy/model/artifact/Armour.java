package swingy.model.artifact;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
public class Armour extends Artifact {
    Random random = new Random();

    public Armour() {
        this.name = "";
        this.lvl = 0;
        this.boost = 0;
    }

    public Armour(String type, int lvl, int current) {
        this.boost = current + random.nextInt(10);
        Map<String, String> classType = new HashMap<String, String>();

        classType.put("Human", "Chain mail");
        classType.put("Elf", "Greaves");
        classType.put("Dwarf", "Mithril");

        this.name = classType.get(type);

        this.lvl = lvl;
    }
}