package com.ncrosby.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class PlayerInput {

    /**
     * This handles clicking on the screen.
     * currently it lacks handling clicking on UI elements, but we need to make this work simply for now.
     * Instead of clicking on UI, clicking can only move the robot.
     * TODO: Handle clicking on UI/Grabbing blocks\
     * Ultimately this will need to move blocks around, not move the robot.
     */
    public static void clickMoveRobot(OrthographicCamera camera, Player robot){
        // Gets the click location
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos); // Moves clicked point to camera location

        robot.setX(touchPos.x - 64 / 2); // Subtracting 64 handles the size of the texture
        robot.setY(touchPos.y - 64 / 2); // Subtracting 64 handles the size of the texture
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
}
