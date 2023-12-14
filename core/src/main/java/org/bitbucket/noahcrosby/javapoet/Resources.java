package org.bitbucket.noahcrosby.javapoet;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Resources {
    public static AssetManager assetManager;

    public static Sound BuildTileSoundSfx;

    public static Sound CollectTileSoundSfx;

    public static Sound RainSfx;

    public static Sound DropSfx;

    public static Sound PickUpTileQuick0Sfx;

    public static Sound PlaceTileSoundSfx;

    public static Sound SelectionBuzzLoopedSfx;

    public static Sound TilePlacementSfx;

    public static Texture AsteroidBlueTexture;

    public static Texture AsteroidGreenTexture;

    public static Texture AsteroidOrangeTexture;

    public static Texture AsteroidPurpleTexture;

    public static Texture AsteroidRedTexture;

    public static Texture AsteroidSilverTexture;

    public static Texture AsteroidYellowTexture;

    public static Texture ConstallationMapTexture;

    public static Texture CraftingIconTexture;

    public static Texture CraftingIconPressedTexture;

    public static Texture HoverIndicatorTexture;

    public static Texture RobotV2Texture;

    public static Texture ShipTileBlueTexture;

    public static Texture ShipTileGreenTexture;

    public static Texture ShipTileOrangeTexture;

    public static Texture ShipTilePurpleTexture;

    public static Texture ShipTileSilverTexture;

    public static Texture ShipTileYellowTexture;

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
        assetManager.load("Sound Effects/drop.wav", Sound.class);
        assetManager.load("Sound Effects/PickUpTileQuick0.wav", Sound.class);
        assetManager.load("Sound Effects/PlaceTileSound.wav", Sound.class);
        assetManager.load("Sound Effects/SelectionBuzzLooped.wav", Sound.class);
        assetManager.load("Sound Effects/tilePlacement.wav", Sound.class);
        assetManager.load("Textures/asteroid_blue.png", Texture.class);
        assetManager.load("Textures/asteroid_green.png", Texture.class);
        assetManager.load("Textures/asteroid_orange.png", Texture.class);
        assetManager.load("Textures/asteroid_purple.png", Texture.class);
        assetManager.load("Textures/asteroid_red.png", Texture.class);
        assetManager.load("Textures/asteroid_silver.png", Texture.class);
        assetManager.load("Textures/asteroid_yellow.png", Texture.class);
        assetManager.load("Textures/constallationMap.png", Texture.class);
        assetManager.load("Textures/craftingIcon.png", Texture.class);
        assetManager.load("Textures/craftingIconPressed.png", Texture.class);
        assetManager.load("Textures/HoverIndicator.png", Texture.class);
        assetManager.load("Textures/RobotV2.png", Texture.class);
        assetManager.load("Textures/ShipTileBlue.png", Texture.class);
        assetManager.load("Textures/ShipTileGreen.png", Texture.class);
        assetManager.load("Textures/ShipTileOrange.png", Texture.class);
        assetManager.load("Textures/ShipTilePurple.png", Texture.class);
        assetManager.load("Textures/ShipTileSilver.png", Texture.class);
        assetManager.load("Textures/ShipTileYellow.png", Texture.class);
        assetManager.load("Textures/ShipTile_Core.png", Texture.class);
        assetManager.load("Textures/ShipTile_Red.png", Texture.class);
        assetManager.load("Textures/ShipTile_Strong.png", Texture.class);
        assetManager.load("Textures/ToBeCollapsed.png", Texture.class);
        assetManager.load("Music/MainMenu Extended Messingaround.wav", Music.class);
        assetManager.load("Music/MainMenuTune.wav", Music.class);
    }

    public static void updateAssets() {
        if(assetManager.update()) {
            BuildTileSoundSfx = assetManager.get("Sound Effects/buildTileSound.mp3");
            CollectTileSoundSfx = assetManager.get("Sound Effects/collectTileSound.mp3");
            RainSfx = assetManager.get("Sound Effects/rain.mp3");
            DropSfx = assetManager.get("Sound Effects/drop.wav");
            PickUpTileQuick0Sfx = assetManager.get("Sound Effects/PickUpTileQuick0.wav");
            PlaceTileSoundSfx = assetManager.get("Sound Effects/PlaceTileSound.wav");
            SelectionBuzzLoopedSfx = assetManager.get("Sound Effects/SelectionBuzzLooped.wav");
            TilePlacementSfx = assetManager.get("Sound Effects/tilePlacement.wav");
            AsteroidBlueTexture = assetManager.get("Textures/asteroid_blue.png");
            AsteroidGreenTexture = assetManager.get("Textures/asteroid_green.png");
            AsteroidOrangeTexture = assetManager.get("Textures/asteroid_orange.png");
            AsteroidPurpleTexture = assetManager.get("Textures/asteroid_purple.png");
            AsteroidRedTexture = assetManager.get("Textures/asteroid_red.png");
            AsteroidSilverTexture = assetManager.get("Textures/asteroid_silver.png");
            AsteroidYellowTexture = assetManager.get("Textures/asteroid_yellow.png");
            ConstallationMapTexture = assetManager.get("Textures/constallationMap.png");
            CraftingIconTexture = assetManager.get("Textures/craftingIcon.png");
            CraftingIconPressedTexture = assetManager.get("Textures/craftingIconPressed.png");
            HoverIndicatorTexture = assetManager.get("Textures/HoverIndicator.png");
            RobotV2Texture = assetManager.get("Textures/RobotV2.png");
            ShipTileBlueTexture = assetManager.get("Textures/ShipTileBlue.png");
            ShipTileGreenTexture = assetManager.get("Textures/ShipTileGreen.png");
            ShipTileOrangeTexture = assetManager.get("Textures/ShipTileOrange.png");
            ShipTilePurpleTexture = assetManager.get("Textures/ShipTilePurple.png");
            ShipTileSilverTexture = assetManager.get("Textures/ShipTileSilver.png");
            ShipTileYellowTexture = assetManager.get("Textures/ShipTileYellow.png");
            ShipTileCoreTexture = assetManager.get("Textures/ShipTile_Core.png");
            ShipTileRedTexture = assetManager.get("Textures/ShipTile_Red.png");
            ShipTileStrongTexture = assetManager.get("Textures/ShipTile_Strong.png");
            ToBeCollapsedTexture = assetManager.get("Textures/ToBeCollapsed.png");
            MainMenuExtendedMessingaroundMusic = assetManager.get("Music/MainMenu Extended Messingaround.wav");
            MainMenuTuneMusic = assetManager.get("Music/MainMenuTune.wav");
        }
    }
}
