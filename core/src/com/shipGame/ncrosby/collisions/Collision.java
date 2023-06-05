package com.shipGame.ncrosby.collisions;

import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;

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
