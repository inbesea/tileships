package com.ncrosby.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.ncrosby.game.tileShipGame;

import static com.ncrosby.game.PlayerInput.clickMoveRobot;
import static com.ncrosby.game.PlayerInput.handleKeyPressed;

public class GameScreen implements Screen {
    final tileShipGame game;
    Texture redTile;
    Rectangle robot;
    public Texture robotTexture;
    OrthographicCamera camera;

    public GameScreen(final tileShipGame game){
        this.game = game;

        //Load images needed for the game to run.
        redTile = new Texture(Gdx.files.internal("ShipTile_Red.png"));
        robotTexture = new Texture(Gdx.files.internal("RobotV1.png"));


        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // init player
        robot = new Rectangle(64,64,64,64);

        // init ship

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

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        //game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
        game.batch.draw(robotTexture, robot.getX(), robot.getY(), 64, 64);
        game.batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
            clickMoveRobot(camera, robot);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            handleKeyPressed(robot);
        }
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
}
