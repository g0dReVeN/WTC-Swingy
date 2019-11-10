package swingy.model.profession;

import lombok.Getter;
import lombok.Setter;
import swingy.model.artifact.*;

import javax.validation.constraints.Size;

@Getter
@Setter
public class Profession {
    private Weapon weapon;
    private Armour armour;
    private Helm helm;

    @Size(min = 1, max = 8)
    private String name;
    @Size(min = 3, max = 6)
    private String type;

    private int ID;
    private int XP;
    private int lvl;
    private int lvlUp;
    private int HP;
    private int maxHP;
    private int atk;
    private int def;
}
