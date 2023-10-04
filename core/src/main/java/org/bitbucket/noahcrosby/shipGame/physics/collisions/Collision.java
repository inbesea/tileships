package org.bitbucket.noahcrosby.shipGame.physics.collisions;

import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;

public class Collision {
    GameObject colliderA;
    GameObject colliderB;

    public Collision(GameObject a, GameObject b){
        this.colliderA = a;
        this.colliderB = b;
    }

    public boolean hasType(ID id){
        return colliderA.getID() == id || colliderB.getID() == id;
    }
}
