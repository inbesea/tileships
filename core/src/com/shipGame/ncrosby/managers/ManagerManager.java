package com.shipGame.ncrosby.managers;

import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Player;

import java.util.ArrayList;

/**
 * Holds reference to the player and other universal objects that are needed.
 */
public class ManagerManager implements ManagerManagerInterface{

    // Singleton boilerplate
    private static ManagerManager INSTANCE;
    private ManagerManager(){
    }
    public static ManagerManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ManagerManager();
        }
        return INSTANCE;
    }

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public ArrayList<GameObject> getObjects(ID id) {
        return null;
    }

    @Override
    public ArrayList<GameObject> getObjects(ArrayList<ID> ids) {
        return null;
    }

    @Override
    public boolean deleteObject(GameObject gameObject) {
        return false;
    }
}
