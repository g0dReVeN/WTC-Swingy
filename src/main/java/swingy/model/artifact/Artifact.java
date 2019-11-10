package swingy.model.artifact;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Artifact {
    protected String name;
    protected int boost;
    protected int lvl;
}
