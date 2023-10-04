package org.bitbucket.noahcrosby.shipGame.managers;

import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;

import java.util.ArrayList;

public interface ManagerManagerInterface {

    //Grab all objects with matching ID
    public abstract ArrayList<GameObject> getObjects(ID id);
    public abstract ArrayList<GameObject> getObjects(ArrayList<ID> ids);

    // Remove a specific object from the game based on its reference
    // NOTE : This should be absolute. There should be no other instances of the object anywhere.
    public abstract boolean deleteObject(GameObject gameObject);
}
