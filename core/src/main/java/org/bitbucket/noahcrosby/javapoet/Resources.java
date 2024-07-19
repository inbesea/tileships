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

    public static Sound MovingTileSoundSfx;

    public static Sound PickUpTileQuick0Sfx;

    public static Sound PlaceTileSoundSfx;

    public static Sound SelectionBuzzLoopedSfx;

    public static Sound TilePlacementSfx;

    public static Texture AncientAsteroidTexture;

    public static Texture AsteroidBlueTexture;

    public static Texture AsteroidGreenTexture;

    public static Texture AsteroidOrangeTexture;

    public static Texture AsteroidPurpleTexture;

    public static Texture AsteroidRedTexture;

    public static Texture AsteroidSilverTexture;

    public static Texture AsteroidYellowTexture;

    public static Texture BigMetalTileTexture;

    public static Texture BigWoodTileTexture;

    public static Texture ConstallationMapTexture;

    public static Texture CraftingIconTexture;

    public static Texture CraftingIconPressedTexture;

    public static Texture FurnaceTexture;

    public static Texture GlassAsteroidTexture;

    public static Texture GlassTileTexture;

    public static Texture HoverIndicatorTexture;

    public static Texture MetalTileTexture;

    public static Texture PowerTilePoweredTexture;

    public static Texture PowerTileUnpoweredTexture;

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

    public static Texture SprayPaintBigTileTexture;

    public static Texture SprayPaintTileTexture;

    public static Texture ToBeCollapsedTexture;

    public static Texture WoodAsteroidTexture;

    public static Texture WoodTileTexture;

    public static Music MainMenuExtendedMessingaroundMusic;

    public static Music MainMenuTuneMusic;

    public static void loadAssets() {
        assetManager = new AssetManager();
        assetManager.load("Sound Effects/buildTileSound.mp3", Sound.class);
        assetManager.load("Sound Effects/collectTileSound.mp3", Sound.class);
        assetManager.load("Sound Effects/rain.mp3", Sound.class);
        assetManager.load("Sound Effects/drop.wav", Sound.class);
        assetManager.load("Sound Effects/MovingTileSound.wav", Sound.class);
        assetManager.load("Sound Effects/PickUpTileQuick0.wav", Sound.class);
        assetManager.load("Sound Effects/PlaceTileSound.wav", Sound.class);
        assetManager.load("Sound Effects/SelectionBuzzLooped.wav", Sound.class);
        assetManager.load("Sound Effects/tilePlacement.wav", Sound.class);
        assetManager.load("Textures/ancientAsteroid.png", Texture.class);
        assetManager.load("Textures/asteroid_blue.png", Texture.class);
        assetManager.load("Textures/asteroid_green.png", Texture.class);
        assetManager.load("Textures/asteroid_orange.png", Texture.class);
        assetManager.load("Textures/asteroid_purple.png", Texture.class);
        assetManager.load("Textures/asteroid_red.png", Texture.class);
        assetManager.load("Textures/asteroid_silver.png", Texture.class);
        assetManager.load("Textures/asteroid_yellow.png", Texture.class);
        assetManager.load("Textures/BigMetalTile.png", Texture.class);
        assetManager.load("Textures/BigWoodTile.png", Texture.class);
        assetManager.load("Textures/constallationMap.png", Texture.class);
        assetManager.load("Textures/craftingIcon.png", Texture.class);
        assetManager.load("Textures/craftingIconPressed.png", Texture.class);
        assetManager.load("Textures/furnace.png", Texture.class);
        assetManager.load("Textures/glassAsteroid.png", Texture.class);
        assetManager.load("Textures/GlassTile.png", Texture.class);
        assetManager.load("Textures/HoverIndicator.png", Texture.class);
        assetManager.load("Textures/MetalTile.png", Texture.class);
        assetManager.load("Textures/PowerTile_Powered.png", Texture.class);
        assetManager.load("Textures/PowerTile_Unpowered.png", Texture.class);
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
        assetManager.load("Textures/SprayPaintBigTile.png", Texture.class);
        assetManager.load("Textures/SprayPaintTile.png", Texture.class);
        assetManager.load("Textures/ToBeCollapsed.png", Texture.class);
        assetManager.load("Textures/WoodAsteroid.png", Texture.class);
        assetManager.load("Textures/WoodTile.png", Texture.class);
        assetManager.load("Music/MainMenu Extended Messingaround.wav", Music.class);
        assetManager.load("Music/MainMenuTune.wav", Music.class);
    }

    public static void updateAssets() {
        if(assetManager.update()) {
            BuildTileSoundSfx = assetManager.get("Sound Effects/buildTileSound.mp3");
            CollectTileSoundSfx = assetManager.get("Sound Effects/collectTileSound.mp3");
            RainSfx = assetManager.get("Sound Effects/rain.mp3");
            DropSfx = assetManager.get("Sound Effects/drop.wav");
            MovingTileSoundSfx = assetManager.get("Sound Effects/MovingTileSound.wav");
            PickUpTileQuick0Sfx = assetManager.get("Sound Effects/PickUpTileQuick0.wav");
            PlaceTileSoundSfx = assetManager.get("Sound Effects/PlaceTileSound.wav");
            SelectionBuzzLoopedSfx = assetManager.get("Sound Effects/SelectionBuzzLooped.wav");
            TilePlacementSfx = assetManager.get("Sound Effects/tilePlacement.wav");
            AncientAsteroidTexture = assetManager.get("Textures/ancientAsteroid.png");
            AsteroidBlueTexture = assetManager.get("Textures/asteroid_blue.png");
            AsteroidGreenTexture = assetManager.get("Textures/asteroid_green.png");
            AsteroidOrangeTexture = assetManager.get("Textures/asteroid_orange.png");
            AsteroidPurpleTexture = assetManager.get("Textures/asteroid_purple.png");
            AsteroidRedTexture = assetManager.get("Textures/asteroid_red.png");
            AsteroidSilverTexture = assetManager.get("Textures/asteroid_silver.png");
            AsteroidYellowTexture = assetManager.get("Textures/asteroid_yellow.png");
            BigMetalTileTexture = assetManager.get("Textures/BigMetalTile.png");
            BigWoodTileTexture = assetManager.get("Textures/BigWoodTile.png");
            ConstallationMapTexture = assetManager.get("Textures/constallationMap.png");
            CraftingIconTexture = assetManager.get("Textures/craftingIcon.png");
            CraftingIconPressedTexture = assetManager.get("Textures/craftingIconPressed.png");
            FurnaceTexture = assetManager.get("Textures/furnace.png");
            GlassAsteroidTexture = assetManager.get("Textures/glassAsteroid.png");
            GlassTileTexture = assetManager.get("Textures/GlassTile.png");
            HoverIndicatorTexture = assetManager.get("Textures/HoverIndicator.png");
            MetalTileTexture = assetManager.get("Textures/MetalTile.png");
            PowerTilePoweredTexture = assetManager.get("Textures/PowerTile_Powered.png");
            PowerTileUnpoweredTexture = assetManager.get("Textures/PowerTile_Unpowered.png");
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
            SprayPaintBigTileTexture = assetManager.get("Textures/SprayPaintBigTile.png");
            SprayPaintTileTexture = assetManager.get("Textures/SprayPaintTile.png");
            ToBeCollapsedTexture = assetManager.get("Textures/ToBeCollapsed.png");
            WoodAsteroidTexture = assetManager.get("Textures/WoodAsteroid.png");
            WoodTileTexture = assetManager.get("Textures/WoodTile.png");
            MainMenuExtendedMessingaroundMusic = assetManager.get("Music/MainMenu Extended Messingaround.wav");
            MainMenuTuneMusic = assetManager.get("Music/MainMenuTune.wav");
        }
    }
}
