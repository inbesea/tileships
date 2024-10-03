package org.bitbucket.noahcrosby.shipGame.Components;

import com.badlogic.ashley.core.Component;

public class CollectorComponent implements Component {
    public int collectability = 50;

    public CollectorComponent(){}

    public CollectorComponent(int collectability){
        this.collectability = collectability;
    }

}
