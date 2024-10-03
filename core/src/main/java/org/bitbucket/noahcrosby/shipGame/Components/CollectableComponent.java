package org.bitbucket.noahcrosby.shipGame.Components;

import com.badlogic.ashley.core.Component;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.util.SecondOrderDynamics;

import java.util.ArrayList;

public class CollectableComponent implements Component {

    private ArrayList<ID> collectors = new ArrayList<>();
    public int collectability = 0; // When this is higher this collectable can be gather more easily
//    private SecondOrderDynamics sodX, sodY;

    public CollectableComponent(){}

    public CollectableComponent(ID collectorID){
        collectors.add(collectorID);
    }

    public CollectableComponent(ArrayList<ID> collectorIDs){
        setCollectors(collectorIDs);
    }

    public void setCollectors(ArrayList<ID> collectors){
        this.collectors = collectors;
    }

    public void addCollectorType(ID collectorID){
        collectors.add(collectorID);
    }

    public ArrayList<ID> getCollectors(){
        return collectors;
    }

}
