package com.shipGame.input;

import com.AppPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shipGame.TileShipGame;

public class InGameMenuHandler {

    private TileShipGame game;
    private Stage stage;
    private Label titleLabel;
    private Label volumeMusicLabel;
    private Label volumeSoundLabel;
    private Label musicOnOffLabel;
    private Label soundOnOffLabel;
    private Music preferencesScreenMusic;

    public InGameMenuHandler(TileShipGame game){
        this.game = game;
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Method called when showing the inGameMenu
     * Initializes the inGameMenu set up the table and change music.
     */
    public void show() {
        preferencesScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenuTune.wav"));
        preferencesScreenMusic.setVolume(game.getPreferences().getMusicVolume());
        preferencesScreenMusic.play();
        preferencesScreenMusic.setLooping(true);

        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // Each time we show this the table is reasserted to update values
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(AppPreferences.getAppPreferences().getIsDebug());
        stage.addActor(table);

        // TODO : Switch skin creation to use the automation version
        Skin skin = new Skin(Gdx.files.internal("skin/neon/skin/neon-ui.json"));

        //volume
        final Slider volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeMusicSlider.setValue( game.getPreferences().getMusicVolume() );
        volumeMusicSlider.addListener(event -> {
            game.getPreferences().setMusicVolume( volumeMusicSlider.getValue() );
            return false;
        });
        // sound volume
        final Slider soundMusicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundMusicSlider.setValue(game.getPreferences().getSoundVolume());
        soundMusicSlider.addListener(new InputListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setSoundVolume(soundMusicSlider.getValue());
                // updateVolumeLabel();
                return false;
            }
        });

        // music on/off
        final CheckBox musicCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked(game.getPreferences().isMusicEnabled());
        musicCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                System.out.println("Checkbox is returning " + enabled + " for .isChecked()");
                game.getPreferences().setMusicEnabled(enabled);
                return false;
            }
        });

        // sound on/off
        final CheckBox soundEffectsCheckbox = new CheckBox(null, skin);
        soundEffectsCheckbox.setChecked(game.getPreferences().isSoundEffectsEnabled());
        soundEffectsCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().setSoundEffectsEnabled(enabled);
                return false;
            }
        });

        // return to main screen button
        final TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(TileShipGame.MENU);

            }
        });

        // Create labels
        titleLabel = new Label( "Preferences", skin );
        volumeMusicLabel = new Label( "Music Volume", skin );
        volumeSoundLabel = new Label( "Sound Volume", skin );
        musicOnOffLabel = new Label( "Music", skin );
        soundOnOffLabel = new Label( "Sound Effect", skin );


        // Add to table
        table.add(titleLabel).colspan(2);
        table.row().pad(10,0,0,10);
        table.add(volumeMusicLabel).left();
        table.add(volumeMusicSlider);
        table.row().pad(10,0,0,10);
        table.add(musicOnOffLabel).left();
        table.add(musicCheckbox);
        table.row().pad(10,0,0,10);
        table.add(volumeSoundLabel).left();
        table.add(soundMusicSlider);
        table.row().pad(10,0,0,10);
        table.add(soundOnOffLabel).left();
        table.add(soundEffectsCheckbox);
        table.row().pad(10,0,0,10);
        table.add(backButton).colspan(2);
    }
}
