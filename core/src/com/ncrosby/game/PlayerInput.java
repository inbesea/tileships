package com.ncrosby.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import static com.ncrosby.game.util.generalUtil.clamp;


public class PlayerInput {

    /**
     * This handles clicking on the screen.
     * currently it lacks handling clicking on UI elements, but we need to make this work simply for now.
     * Instead of clicking on UI, clicking can only move the robot.
     * TODO: Handle clicking on UI/Grabbing blocks\
     * Ultimately this will need to move blocks around, not move the robot.
     */
    public static void clickMoveRobot(OrthographicCamera camera, Ship playerShip){
        // Gets the click location
        Vector3 touchPos = new Vector3();
        System.out.println("X before unproject : " + Gdx.input.getX());
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos); // Moves clicked point to camera location

//        ShipTile tile = getTile
    }

    /**
     * Handle key presses from GameScreen
     * This will update the robot's position based on keys pressed.
     *
     * @param robot - robot Rectangle to update
     */
    public static void handleKeyPressed(Player robot){
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            robot.setY(robot.getY() + 200 * Gdx.graphics.getDeltaTime())  ;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            robot.setX(robot.getX() - 200 * Gdx.graphics.getDeltaTime())  ;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            robot.setX(robot.getX() + 200 * Gdx.graphics.getDeltaTime())  ;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            robot.setY(robot.getY() - 200 * Gdx.graphics.getDeltaTime())  ;
        }
    }

    /**
     * This algo updates the camera on the player based on the difference between
     * the player and the camera
     *
     * @param player
     * @param camera
     */
    public static void updateCameraOnPlayer(Player player, OrthographicCamera camera){
        float lerp = 3.1f;
        Vector3 cameraPos = camera.position;
        Vector3 playerPos = new Vector3(player.position.x, player.position.y, 0);

        System.out.println(cameraPos + " and " + playerPos);

        // Give the position of the camera no update if player is close enough to camera.
        Vector3 diff = new Vector3(playerPos);
        Vector3 diff2 = new Vector3(cameraPos);
        diff = diff.sub(diff2);

        if(Math.abs(diff.x) > 60){
            cameraPos.x += (playerPos.x - cameraPos.x) *
                    lerp * Gdx.graphics.getDeltaTime();
        }
        if(Math.abs(diff.y) > 60) {
            cameraPos.y += (playerPos.y - cameraPos.y) *
                    lerp * Gdx.graphics.getDeltaTime();
        }

//                    cameraPos.y += (playerPos.y - cameraPos.y) *
//                    lerp * Gdx.graphics.getDeltaTime();
//                    cameraPos.x += (playerPos.x - cameraPos.x) *
//                    lerp * Gdx.graphics.getDeltaTime();
    }
}
