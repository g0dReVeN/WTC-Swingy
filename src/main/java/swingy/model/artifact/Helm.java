package swingy.model.artifact;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
public class Helm extends Artifact {
    Random random = new Random();

    public Helm() {
        this.name = "";
        this.lvl = 0;
        this.boost = 0;
    }

    public Helm(String type, int lvl, int current) {
        this.boost = current + random.nextInt(10);
        Map<String, String> classType = new HashMap<String, String>();

        classType.put("Human", "Crown of Karma");
        classType.put("Elf", "Diamond Head");
        classType.put("Dwarf", "Dragonbone Helm");
        this.name = classType.get(type);

        this.lvl = lvl;
    }
}
