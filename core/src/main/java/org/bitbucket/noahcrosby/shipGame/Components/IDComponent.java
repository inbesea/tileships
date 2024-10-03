package org.bitbucket.noahcrosby.shipGame.Components;

import com.badlogic.ashley.core.Component;
import org.bitbucket.noahcrosby.shipGame.ID;

public class IDComponent implements Component {

    public ID id;

    public IDComponent(ID id){
        this.id = id;
    }
}
