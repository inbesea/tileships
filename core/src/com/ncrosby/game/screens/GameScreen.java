package com.ncrosby.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.ncrosby.game.*;
import com.ncrosby.game.player.SimpleTouch;

import java.util.Objects;

import static com.ncrosby.game.PlayerInput.*;

public class GameScreen implements Screen {
    final tileShipGame game;
    Sprite sprite;
    Texture redTile;
    private final Player robot;
    OrthographicCamera camera;
    SimpleTouch st;

    private final Array<GameObject> gameObjects;
    private final Ship playerShip;

    public GameScreen(final tileShipGame game){
        this.game = game;
        game.setGameScreen(this); // Give this to be disposed at exit

        st = new SimpleTouch();
        Gdx.input.setInputProcessor(st);

        //Load images needed for the game to run.
        redTile = new Texture(Gdx.files.internal("ShipTile_Red.png"));


        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // init player
        // Give player the game reference
        robot = new Player(new Vector2(64,64), ID.Player ,camera, this.game);

        // init ship
        playerShip = new Ship(new Vector2(200,200), ID.Ship, camera);

        // Place init game objects into the array
        gameObjects = new Array<>();
        gameObjects.add(robot);
        gameObjects.add(playerShip);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch
        // Loops through the game objects and draws them.
        // Uses the game and camera context to handle drawing properly.
        game.batch.begin();
        for(GameObject go: gameObjects){
            drawGameObject(go); // Call helper to draw object
        }
        drawGameObject(robot);// Draw last to be on top of robot
        // Draw hud at this step
        game.batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
            clickMoveRobot(camera, playerShip);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            handleKeyPressed(robot);
        }
        updateCameraOnPlayer(robot, camera);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }

    /**
     * Helper method holds rendering logic, takes game object
     * Expects to be called after game.batch.begin() and with a batch that has the correct camera location context
     */
    private void drawGameObject(GameObject gameObject){
        // Get the texture of the game object and draw it based on the GameScreen Camera.
        String t = gameObject.getTexture();

        if(!Objects.equals(t, "none") && !Objects.equals(t, "")){ // If ID has associated string
            Texture texture = new Texture(Gdx.files.internal(t));
            Vector2 size = gameObject.getSize();
            game.batch.draw(texture, gameObject.getX(), gameObject.getY(), size.x, size.y);
        }
        gameObject.render(this.game);
    }
}
