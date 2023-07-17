package com.javapoet.automation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;

public class GenerateResources extends ApplicationAdapter {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        new Lwjgl3Application(new GenerateResources(), config);
    }

    @Override
    public void create(){

//        FileHandle atlasPath = new FileHandle(Paths.get("assets/Atlas").toFile());
//        FileHandle[] atlasFiles = atlasPath.list("atlas");
//        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(("Resources"))
                .addModifiers((Modifier.PUBLIC))
                .addField(AssetManager.class, "assetManager", Modifier.PUBLIC, Modifier.STATIC);

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("loadAssets")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("assetManager = new $T()", AssetManager.class);

        CreateSoundFX(typeSpecBuilder, methodSpecBuilder);
        CreateTextures(typeSpecBuilder, methodSpecBuilder);

//        for (TextureAtlas.AtlasRegion region : textureAtlas.getRegions()) {
//            typeSpecBuilder.addField(TextureAtlas.AtlasRegion.class, toVariableName("region" + upperFirstChar(region.name)), Modifier.PUBLIC, Modifier.STATIC);
//        }

//        methodSpecBuilder.addStatement("assetManager.load(\"Atlas/textures.Atlas\", $T.class)", TextureAtlas.class);

        methodSpecBuilder.addStatement("assetManager.finishLoading()");

//        methodSpecBuilder.addStatement("$T textureAtlas = assetManager.get(\"Atlas/textures.Atlas\")", TextureAtlas.class);
//        for (TextureAtlas.AtlasRegion atlasRegion : regions) {
//            methodSpecBuilder.addStatement("$L = textureAtlas.findRegion($S)", toVariableName("region" + upperFirstChar(atlasRegion.name)), atlasRegion.name);
//        }

        typeSpecBuilder.addMethod(methodSpecBuilder.build());
        JavaFile javaFile = JavaFile.builder("com.javapoet", typeSpecBuilder.build())
                .indent("    ")
                .build();

        // Create output here
        FileHandle target = new FileHandle(Paths.get("core/src/com/javapoet/Resources.java").toFile());
        target.writeString(javaFile.toString(), false);

        Gdx.app.exit();
    }

    private static String toVariableName(String name) {
        name = name.replaceAll("^[./]*", "").replaceAll("[\\\\/\\-\\s]", "_").replaceAll("['\"]", "");
        String[] splits = name.split("_");
        StringBuilder builder = new StringBuilder(splits[0]);
        for (int i = 1; i < splits.length; i++) {
            String split = splits[i];
            builder.append(Character.toUpperCase(split.charAt(0)));
            builder.append(split.substring(1));
        }

        return builder.toString();
    }

    private static String upperFirstChar(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    private void CreateSoundFX(TypeSpec.Builder typeSpecBuilder, MethodSpec.Builder methodSpecBuilder){
        // Get files
        FileHandle sfxPath = new FileHandle(Paths.get("assets/Sound Effects").toFile());
        FileHandle[] sfxFiles = sfxPath.list("mp3");

        // Create field with file name
        for (FileHandle sfxFile : sfxFiles){
            typeSpecBuilder.addField(Sound.class, toVariableName("sfx" + upperFirstChar(sfxFile.nameWithoutExtension())), Modifier.PUBLIC, Modifier.STATIC);
        }
        // Add loading statement
        for (FileHandle sfxFile : sfxFiles) {
            methodSpecBuilder.addStatement("assetManager.load($S, $T.class)", "Sound Effects/" + sfxFile.name(), Sound.class);
        }

        methodSpecBuilder.addStatement("assetManager.finishLoading()");

        for (FileHandle sfxFile : sfxFiles) {
            methodSpecBuilder.addStatement("$L = assetManager.get($S)", toVariableName("sfx" + upperFirstChar(sfxFile.nameWithoutExtension())), "Sound Effects/" + sfxFile.name());
        }
    }

    private void CreateTextures(TypeSpec.Builder typeSpecBuilder, MethodSpec.Builder methodSpecBuilder) {
        FileHandle texturePath = new FileHandle(Paths.get("assets/Textures").toFile());
        FileHandle[] textureFiles = texturePath.list("png");

        // Create field with file name
        for (FileHandle textureFile : textureFiles){
            typeSpecBuilder.addField(Texture.class, toVariableName(upperFirstChar(textureFile.nameWithoutExtension()) + "Texture"), Modifier.PUBLIC, Modifier.STATIC);
        }
        // Add loading statement
        for (FileHandle textureFile : textureFiles) {
            methodSpecBuilder.addStatement("assetManager.load($S, $T.class)", "Textures/" + textureFile.name(), Texture.class);
        }

        methodSpecBuilder.addStatement("assetManager.finishLoading()");

        for (FileHandle textureFile : textureFiles) {
            methodSpecBuilder.addStatement("$L = assetManager.get($S)", toVariableName(upperFirstChar(textureFile.nameWithoutExtension()) + "Texture"), "Textures/" + textureFile.name());
        }
    }
}