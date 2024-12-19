package org.bitbucket.noahcrosby.javapoet;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Resources {
    public static AssetManager assetManager;

    public static Sound RainSfx;

    public static Sound CollectTileSoundSfx;

    public static Sound BuildTileSoundSfx;

    public static Sound PowerUpSfx;

    public static Sound DropSfx;

    public static Sound TilePlacementSfx;

    public static Sound MovingTileSoundSfx;

    public static Sound PickupCoinSfx;

    public static Sound PlaceTileSoundSfx;

    public static Sound ExplosionSfx;

    public static Sound PickUpTileQuick0Sfx;

    public static Sound SelectionBuzzLoopedSfx;

    public static Texture SprayPaintBigTileTexture;

    public static Texture AsteroidSilverTexture;

    public static Texture ShipTileGreenTexture;

    public static Texture GlassAsteroidTexture;

    public static Texture ShipTileOrangeTexture;

    public static Texture ShipTileCoreTexture;

    public static Texture ExitTileTexture;

    public static Texture WoodAsteroidTexture;

    public static Texture HoverIndicatorTexture;

    public static Texture BigMetalTileTexture;

    public static Texture BigWoodTileTexture;

    public static Texture WoodTileTexture;

    public static Texture AsteroidYellowTexture;

    public static Texture ShipTilePurpleTexture;

    public static Texture RobotV2Texture;

    public static Texture CraftingIconPressedTexture;

    public static Texture MetalTileTexture;

    public static Texture AsteroidPurpleTexture;

    public static Texture AsteroidOrangeTexture;

    public static Texture GlassTileTexture;

    public static Texture ShipTileBlueTexture;

    public static Texture PowerTilePoweredTexture;

    public static Texture SprayPaintTileTexture;

    public static Texture ShipTileRedTexture;

    public static Texture AncientAsteroidTexture;

    public static Texture ToBeCollapsedTexture;

    public static Texture ConstallationMapTexture;

    public static Texture CraftingIconTexture;

    public static Texture ShipTileStrongTexture;

    public static Texture InfoPlateTexture;

    public static Texture PowerTileUnpoweredTexture;

    public static Texture SprayPaintAsteroidTexture;

    public static Texture AsteroidBlueTexture;

    public static Texture AsteroidGreenTexture;

    public static Texture AsteroidRedTexture;

    public static Texture ShipTileYellowTexture;

    public static Texture ShipTileSilverTexture;

    public static Texture FurnaceTexture;

    public static Music MainMenuTuneMusic;

    public static Music MainMenuExtendedMessingaroundMusic;

    public static void loadAssets() {
        assetManager = new AssetManager();
        assetManager.load("Sound Effects/rain.mp3", Sound.class);
        assetManager.load("Sound Effects/collectTileSound.mp3", Sound.class);
        assetManager.load("Sound Effects/buildTileSound.mp3", Sound.class);
        assetManager.load("Sound Effects/powerUp.wav", Sound.class);
        assetManager.load("Sound Effects/drop.wav", Sound.class);
        assetManager.load("Sound Effects/tilePlacement.wav", Sound.class);
        assetManager.load("Sound Effects/MovingTileSound.wav", Sound.class);
        assetManager.load("Sound Effects/pickupCoin.wav", Sound.class);
        assetManager.load("Sound Effects/PlaceTileSound.wav", Sound.class);
        assetManager.load("Sound Effects/explosion.wav", Sound.class);
        assetManager.load("Sound Effects/PickUpTileQuick0.wav", Sound.class);
        assetManager.load("Sound Effects/SelectionBuzzLooped.wav", Sound.class);
        assetManager.load("Textures/SprayPaintBigTile.png", Texture.class);
        assetManager.load("Textures/asteroid_silver.png", Texture.class);
        assetManager.load("Textures/ShipTileGreen.png", Texture.class);
        assetManager.load("Textures/glassAsteroid.png", Texture.class);
        assetManager.load("Textures/ShipTileOrange.png", Texture.class);
        assetManager.load("Textures/ShipTile_Core.png", Texture.class);
        assetManager.load("Textures/ExitTile.png", Texture.class);
        assetManager.load("Textures/WoodAsteroid.png", Texture.class);
        assetManager.load("Textures/HoverIndicator.png", Texture.class);
        assetManager.load("Textures/BigMetalTile.png", Texture.class);
        assetManager.load("Textures/BigWoodTile.png", Texture.class);
        assetManager.load("Textures/WoodTile.png", Texture.class);
        assetManager.load("Textures/asteroid_yellow.png", Texture.class);
        assetManager.load("Textures/ShipTilePurple.png", Texture.class);
        assetManager.load("Textures/RobotV2.png", Texture.class);
        assetManager.load("Textures/craftingIconPressed.png", Texture.class);
        assetManager.load("Textures/MetalTile.png", Texture.class);
        assetManager.load("Textures/asteroid_purple.png", Texture.class);
        assetManager.load("Textures/asteroid_orange.png", Texture.class);
        assetManager.load("Textures/GlassTile.png", Texture.class);
        assetManager.load("Textures/ShipTileBlue.png", Texture.class);
        assetManager.load("Textures/PowerTile_Powered.png", Texture.class);
        assetManager.load("Textures/SprayPaintTile.png", Texture.class);
        assetManager.load("Textures/ShipTile_Red.png", Texture.class);
        assetManager.load("Textures/ancientAsteroid.png", Texture.class);
        assetManager.load("Textures/ToBeCollapsed.png", Texture.class);
        assetManager.load("Textures/constallationMap.png", Texture.class);
        assetManager.load("Textures/craftingIcon.png", Texture.class);
        assetManager.load("Textures/ShipTile_Strong.png", Texture.class);
        assetManager.load("Textures/infoPlate.png", Texture.class);
        assetManager.load("Textures/PowerTile_Unpowered.png", Texture.class);
        assetManager.load("Textures/SprayPaintAsteroid.png", Texture.class);
        assetManager.load("Textures/asteroid_blue.png", Texture.class);
        assetManager.load("Textures/asteroid_green.png", Texture.class);
        assetManager.load("Textures/asteroid_red.png", Texture.class);
        assetManager.load("Textures/ShipTileYellow.png", Texture.class);
        assetManager.load("Textures/ShipTileSilver.png", Texture.class);
        assetManager.load("Textures/furnace.png", Texture.class);
        assetManager.load("Music/MainMenuTune.wav", Music.class);
        assetManager.load("Music/MainMenu Extended Messingaround.wav", Music.class);
    }

    public static void updateAssets() {
        if(assetManager.update()) {
            RainSfx = assetManager.get("Sound Effects/rain.mp3");
            CollectTileSoundSfx = assetManager.get("Sound Effects/collectTileSound.mp3");
            BuildTileSoundSfx = assetManager.get("Sound Effects/buildTileSound.mp3");
            PowerUpSfx = assetManager.get("Sound Effects/powerUp.wav");
            DropSfx = assetManager.get("Sound Effects/drop.wav");
            TilePlacementSfx = assetManager.get("Sound Effects/tilePlacement.wav");
            MovingTileSoundSfx = assetManager.get("Sound Effects/MovingTileSound.wav");
            PickupCoinSfx = assetManager.get("Sound Effects/pickupCoin.wav");
            PlaceTileSoundSfx = assetManager.get("Sound Effects/PlaceTileSound.wav");
            ExplosionSfx = assetManager.get("Sound Effects/explosion.wav");
            PickUpTileQuick0Sfx = assetManager.get("Sound Effects/PickUpTileQuick0.wav");
            SelectionBuzzLoopedSfx = assetManager.get("Sound Effects/SelectionBuzzLooped.wav");
            SprayPaintBigTileTexture = assetManager.get("Textures/SprayPaintBigTile.png");
            AsteroidSilverTexture = assetManager.get("Textures/asteroid_silver.png");
            ShipTileGreenTexture = assetManager.get("Textures/ShipTileGreen.png");
            GlassAsteroidTexture = assetManager.get("Textures/glassAsteroid.png");
            ShipTileOrangeTexture = assetManager.get("Textures/ShipTileOrange.png");
            ShipTileCoreTexture = assetManager.get("Textures/ShipTile_Core.png");
            ExitTileTexture = assetManager.get("Textures/ExitTile.png");
            WoodAsteroidTexture = assetManager.get("Textures/WoodAsteroid.png");
            HoverIndicatorTexture = assetManager.get("Textures/HoverIndicator.png");
            BigMetalTileTexture = assetManager.get("Textures/BigMetalTile.png");
            BigWoodTileTexture = assetManager.get("Textures/BigWoodTile.png");
            WoodTileTexture = assetManager.get("Textures/WoodTile.png");
            AsteroidYellowTexture = assetManager.get("Textures/asteroid_yellow.png");
            ShipTilePurpleTexture = assetManager.get("Textures/ShipTilePurple.png");
            RobotV2Texture = assetManager.get("Textures/RobotV2.png");
            CraftingIconPressedTexture = assetManager.get("Textures/craftingIconPressed.png");
            MetalTileTexture = assetManager.get("Textures/MetalTile.png");
            AsteroidPurpleTexture = assetManager.get("Textures/asteroid_purple.png");
            AsteroidOrangeTexture = assetManager.get("Textures/asteroid_orange.png");
            GlassTileTexture = assetManager.get("Textures/GlassTile.png");
            ShipTileBlueTexture = assetManager.get("Textures/ShipTileBlue.png");
            PowerTilePoweredTexture = assetManager.get("Textures/PowerTile_Powered.png");
            SprayPaintTileTexture = assetManager.get("Textures/SprayPaintTile.png");
            ShipTileRedTexture = assetManager.get("Textures/ShipTile_Red.png");
            AncientAsteroidTexture = assetManager.get("Textures/ancientAsteroid.png");
            ToBeCollapsedTexture = assetManager.get("Textures/ToBeCollapsed.png");
            ConstallationMapTexture = assetManager.get("Textures/constallationMap.png");
            CraftingIconTexture = assetManager.get("Textures/craftingIcon.png");
            ShipTileStrongTexture = assetManager.get("Textures/ShipTile_Strong.png");
            InfoPlateTexture = assetManager.get("Textures/infoPlate.png");
            PowerTileUnpoweredTexture = assetManager.get("Textures/PowerTile_Unpowered.png");
            SprayPaintAsteroidTexture = assetManager.get("Textures/SprayPaintAsteroid.png");
            AsteroidBlueTexture = assetManager.get("Textures/asteroid_blue.png");
            AsteroidGreenTexture = assetManager.get("Textures/asteroid_green.png");
            AsteroidRedTexture = assetManager.get("Textures/asteroid_red.png");
            ShipTileYellowTexture = assetManager.get("Textures/ShipTileYellow.png");
            ShipTileSilverTexture = assetManager.get("Textures/ShipTileSilver.png");
            FurnaceTexture = assetManager.get("Textures/furnace.png");
            MainMenuTuneMusic = assetManager.get("Music/MainMenuTune.wav");
            MainMenuExtendedMessingaroundMusic = assetManager.get("Music/MainMenu Extended Messingaround.wav");
        }
    }
}
