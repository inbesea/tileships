package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;

public class GoalChecker implements Listener<Ship> {
    private static final int WIN_TILE_COUNT = 8;
    Boolean won = false;


    public GoalChecker() {

    }

    /**
     * Checks for a win.
     */
    public Boolean getWon() {
        // This sucks, we can check if win when new information is available.
        // That way we aren't constantly checking for win each frame.
        return won;
    }

    /**
     * Renders the win elements. Sounds,visuals etc.
     */
    public void renderWin() {

    }

    @Override
    public void receive(Signal<Ship> signal, Ship ship) {
        // Alrighty, we just got the ship. Let's check for a win.
        // We only got the whole ship, and only care about the count of tiles in the ship.
        if(ship.getExistingTiles().size > WIN_TILE_COUNT){
            won = true;
        }
    }
}
