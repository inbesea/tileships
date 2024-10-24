package org.bitbucket.noahcrosby.shipGame.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

public class PlayerInput extends InputAdapter {
    boolean movingUp = false, movingDown = false, movingRight = false, movingLeft = false;

    /**
     * Holy cow pie this is a terrible class.
     * We could move out the camera lerping, and updating the player location pretty easily.
     * We've just kept it this way for convenience. If it becomes a problem, we can change it.
     */

    public static float cameraFollow = 0.95f;
    private ShipTile left,right;

    /**
     * Handle key presses from GameScreen
     * This will update the player's position based on keys pressed.
     *
     * TODO : Migrate this to SimpleTouch.java
     *
     * @param player - player Rectangle to update
     */
    public void handleKeyPressed(Player player, OrthographicCamera camera){

        Vector3 playerPos = new Vector3(player.getX(), player.getY(), 0);
        Ship ship = player.getPlayerShip();

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            movingUp = true;
            // Get vector with position of moving up position
            Vector2 possibleUpPosition = new Vector2(
                    (playerPos.x),
                    playerPos.y  + (player.getPlayerSpeed() * Gdx.graphics.getDeltaTime()));
            Vector2 possibleRightCorner = new Vector2(possibleUpPosition.x + player.getWidth(),possibleUpPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleUpPosition, possibleRightCorner);

            if(!isOffShip){
                player.setY(possibleUpPosition.y)  ;
            } else {
//                System.out.println("Bumping up!");
            }
        } else {
            movingUp = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            movingLeft = true;
            Vector2 possibleLeftPosition = new Vector2(
                    (playerPos.x) - (player.getPlayerSpeed() * Gdx.graphics.getDeltaTime()),
                    playerPos.y);
            Vector2 possibleRightCorner = new Vector2(possibleLeftPosition.x + player.getWidth(),possibleLeftPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleLeftPosition, possibleRightCorner);

            if(!isOffShip){
                player.setX(possibleLeftPosition.x)  ;
            }
            else {
//                System.out.println("Bumping left!");
            }
        } else {
            movingLeft = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            movingRight = true;
            // Lets check if the new position is off the ship. Get possible position and opposite corner
            Vector2 possibleRightPosition = new Vector2(
                    (playerPos.x) + (player.getPlayerSpeed() * Gdx.graphics.getDeltaTime()),
                    playerPos.y);
            Vector2 possibleRightCorner = new Vector2(possibleRightPosition.x + player.getWidth(),possibleRightPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleRightPosition, possibleRightCorner);

            if(!isOffShip){
                player.setX(possibleRightPosition.x)  ;
            }
            else {
//                System.out.println("Bumping right!");
            }
        } else {
            movingRight = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            movingDown = true;
            Vector2 possibleDownPosition = new Vector2(playerPos.x,playerPos.y - player.getPlayerSpeed() * Gdx.graphics.getDeltaTime());
            Vector2 possibleDownCorner = new Vector2(possibleDownPosition.x + player.getWidth(),possibleDownPosition.y); //
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleDownPosition, possibleDownCorner);

            if(!isOffShip){
                player.setY(possibleDownPosition.y);
            }
            else {
//                System.out.println("Bumping down!");
            }
        } else {
            movingDown = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            localZoomClamp(false, camera);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            localZoomClamp(true, camera);
        }

        // We want to lock the tiles the player is standing on, and unlock tiles he leaves.
        lockAndUnlockTiles(player);
    }

    private void lockAndUnlockTiles(Player player) {
        // We need to be careful not to unlock tiles that need to be locked for other reasons.
        // Not sure how you would do that.
        // Is player on the tile in question?
        Ship playerShip = player.getPlayerShip();

        if(left != null) left.setLocked(false);
        if(right != null) right.setLocked(false);

        // If the tile is already locked we won't set it to these vars.
        // This way already locked tiles won't unlock when arriving/leaving.
        ShipTile newLeft = playerShip.returnTile(player.getX(), player.getY());
        ShipTile newRight = playerShip.returnTile(player.getX() + player.getWidth(), player.getY());

        // Dont playerlock or save tiles that are already locked.
        if(newLeft != null &&
            !newLeft.isLocked())left = newLeft.setLocked(true);
        if(newRight != null &&
            !newRight.isLocked())right = newRight.setLocked(true);
    }

    /**
     * Updates zoom level and clamps within acceptable clamp levels
     * Meant for player use
     * @param zoomIn
     * @param camera
     */
    private static void localZoomClamp(boolean zoomIn, OrthographicCamera camera){
        int zoomAmount;
        if(zoomIn){zoomAmount = TileShipGame.zoomSpeed * -1;}
        else{zoomAmount = TileShipGame.zoomSpeed;}

        float newValue = camera.zoom + (zoomAmount * Gdx.graphics.getDeltaTime());

        if (newValue >= TileShipGame.zoomMax)
            newValue = TileShipGame.zoomMax;
        else newValue = Math.max(newValue, TileShipGame.zoomMin);

        camera.zoom = newValue;
    }

    /**
     * This algo updates the camera on the player based on the difference between
     * the player and the camera
     *
     * @param player - Player object
     * @param camera - Reference to camera context
     */
    public void updateCameraOnPlayer(Player player, OrthographicCamera camera){
        float lerp = setLerpOnLookahead();
        Vector3 cameraPos = camera.position;
        Vector3 playerPos = new Vector3(player.getX(), player.getY(), 0);
        playerPos = applyLookahead(playerPos);

        // Fix huge time deltas when not focused on game screen
        float delta = Gdx.graphics.getDeltaTime();
        if(delta > 0.5f){
            Gdx.app.debug("updateCameraOnPlayer", "Fixing delta " + delta + " reducing to 0.5f");
            delta = 0.5f;
        }

        // Give the position of the camera no update if player is close enough to camera.
        Vector3 diff = new Vector3(playerPos);
        Vector3 diff2 = new Vector3(cameraPos);
        diff = diff.sub(diff2);

        // Issue is probably with this deltaTime check? If the delta is too big what happens?
        if(Math.abs(diff.x) > cameraFollow){
            cameraPos.x += (playerPos.x - cameraPos.x) *
                    lerp * delta;
        }
        if(Math.abs(diff.y) > cameraFollow) {
            cameraPos.y += (playerPos.y - cameraPos.y) *
                    lerp * delta;
        }
    }

    private float setLerpOnLookahead() {
        float slowLerp = 1.1f;
        float quickLerp = 1.8f;
        boolean currentlyMoving = Gdx.input.isKeyPressed(Input.Keys.W)||
            Gdx.input.isKeyPressed(Input.Keys.S)||
            Gdx.input.isKeyPressed(Input.Keys.D)||
            Gdx.input.isKeyPressed(Input.Keys.A);

        if(currentlyMoving){
            return quickLerp;
        } else {
            return quickLerp;
        }
    }

    private Vector3 applyLookahead(Vector3 playerPos) {
        int lookahead = 100;
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            playerPos.y += lookahead;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
            playerPos.y -= lookahead;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            playerPos.x += lookahead;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            playerPos.x -= lookahead;
        }
        return playerPos;
    }
}
