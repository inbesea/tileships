package com.javapoet;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Resources {
    public static AssetManager assetManager;

    public static Sound sfxBuildTileSound;

    public static Sound sfxCollectTileSound;

    public static Sound sfxRain;

    public static Texture AsteroidPurpleTexture;

    public static Texture HoverIndicatorTexture;

    public static Texture RobotV1Texture;

    public static Texture RobotV2Texture;

    public static Texture ShipTileCoreTexture;

    public static Texture ShipTileRedTexture;

    public static Texture ShipTileStrongTexture;

    public static Texture ToBeCollapsedTexture;

    public static Music MainMenuExtendedMessingaroundMusic;

    public static Music MainMenuTuneMusic;

    public static void loadAssets() {
        assetManager = new AssetManager();
        assetManager.load("Sound Effects/buildTileSound.mp3", Sound.class);
        assetManager.load("Sound Effects/collectTileSound.mp3", Sound.class);
        assetManager.load("Sound Effects/rain.mp3", Sound.class);
        assetManager.load("Textures/asteroid_purple.png", Texture.class);
        assetManager.load("Textures/HoverIndicator.png", Texture.class);
        assetManager.load("Textures/RobotV1.png", Texture.class);
        assetManager.load("Textures/RobotV2.png", Texture.class);
        assetManager.load("Textures/ShipTile_Core.png", Texture.class);
        assetManager.load("Textures/ShipTile_Red.png", Texture.class);
        assetManager.load("Textures/ShipTile_Strong.png", Texture.class);
        assetManager.load("Textures/ToBeCollapsed.png", Texture.class);
        assetManager.load("Music/MainMenu Extended Messingaround.wav", Music.class);
        assetManager.load("Music/MainMenuTune.wav", Music.class);
    }

    public static void updateAssets() {
        if(assetManager.update()) {
            sfxBuildTileSound = assetManager.get("Sound Effects/buildTileSound.mp3");
            sfxCollectTileSound = assetManager.get("Sound Effects/collectTileSound.mp3");
            sfxRain = assetManager.get("Sound Effects/rain.mp3");
            AsteroidPurpleTexture = assetManager.get("Textures/asteroid_purple.png");
            HoverIndicatorTexture = assetManager.get("Textures/HoverIndicator.png");
            RobotV1Texture = assetManager.get("Textures/RobotV1.png");
            RobotV2Texture = assetManager.get("Textures/RobotV2.png");
            ShipTileCoreTexture = assetManager.get("Textures/ShipTile_Core.png");
            ShipTileRedTexture = assetManager.get("Textures/ShipTile_Red.png");
            ShipTileStrongTexture = assetManager.get("Textures/ShipTile_Strong.png");
            ToBeCollapsedTexture = assetManager.get("Textures/ToBeCollapsed.png");
            MainMenuExtendedMessingaroundMusic = assetManager.get("Music/MainMenu Extended Messingaround.wav");
            MainMenuTuneMusic = assetManager.get("Music/MainMenuTune.wav");
        }
    }
}
