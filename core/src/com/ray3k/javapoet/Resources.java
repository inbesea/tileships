package com.javapoet;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Resources {
    public static AssetManager assetManager;
    public static Sound sfxBuildTileSound;
    public static Sound sfxCollectTileSound;
    public static Sound sfxRain;
    public static Sprite AsteroidPurpleSprite;
    public static Sprite HoverIndicatorSprite;
    public static Sprite RobotV1Sprite;
    public static Sprite RobotV2Sprite;
    public static Sprite ShipTileCoreSprite;
    public static Sprite ShipTileRedSprite;
    public static Sprite ShipTileStrongSprite;
    public static Sprite ToBeCollapsedSprite;

    public static void loadAssets() {
        assetManager = new AssetManager();
        assetManager.load("sfx/buildTileSound.mp3", Sound.class);
        assetManager.load("sfx/collectTileSound.mp3", Sound.class);
        assetManager.load("sfx/rain.mp3", Sound.class);
        sfxBuildTileSound = assetManager.get("sfx/buildTileSound.mp3");
        sfxCollectTileSound = assetManager.get("sfx/collectTileSound.mp3");
        sfxRain = assetManager.get("sfx/rain.mp3");
        assetManager.load("sfx/asteroid_purple.png", Sprite.class);
        assetManager.load("sfx/HoverIndicator.png", Sprite.class);
        assetManager.load("sfx/RobotV1.png", Sprite.class);
        assetManager.load("sfx/RobotV2.png", Sprite.class);
        assetManager.load("sfx/ShipTile_Core.png", Sprite.class);
        assetManager.load("sfx/ShipTile_Red.png", Sprite.class);
        assetManager.load("sfx/ShipTile_Strong.png", Sprite.class);
        assetManager.load("sfx/ToBeCollapsed.png", Sprite.class);
        AsteroidPurpleSprite = assetManager.get("Sprites/asteroid_purple.png");
        HoverIndicatorSprite = assetManager.get("Sprites/HoverIndicator.png");
        RobotV1Sprite = assetManager.get("Sprites/RobotV1.png");
        RobotV2Sprite = assetManager.get("Sprites/RobotV2.png");
        ShipTileCoreSprite = assetManager.get("Sprites/ShipTile_Core.png");
        ShipTileRedSprite = assetManager.get("Sprites/ShipTile_Red.png");
        ShipTileStrongSprite = assetManager.get("Sprites/ShipTile_Strong.png");
        ToBeCollapsedSprite = assetManager.get("Sprites/ToBeCollapsed.png");
        assetManager.load("Atlas/textures.Atlas", TextureAtlas.class);
        assetManager.finishLoading();
    }
}
