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

/**
 * This class automates resource management and retrieval.
 * When updating, adding or removing a resource this will need to be ran so core/src/com/javapoet/Resources.java is updated.
 *
 * It uses Java Poet, a builder pattern that can add methods, fields, etc. and output that to a usable java file.
 */
public class GenerateResources extends ApplicationAdapter {

    private String sfxPrefix = "sfx";

    protected TypeSpec.Builder typeSpecBuilder;
    protected MethodSpec.Builder loadAssetMethodSpecBuilder;
    protected MethodSpec.Builder updateAssetMethodSpecBuilder;

    private FileHandle[] sfxFiles;
    private FileHandle[] textureFiles;
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        new Lwjgl3Application(new GenerateResources(), config);
    }

    @Override
    public void create(){
        typeSpecBuilder = TypeSpec.classBuilder(("Resources"))
                .addModifiers((Modifier.PUBLIC))
                .addField(AssetManager.class, "assetManager", Modifier.PUBLIC, Modifier.STATIC);

        loadAssetMethodSpecBuilder = MethodSpec.methodBuilder("loadAssets")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("assetManager = new $T()", AssetManager.class);

        updateAssetMethodSpecBuilder = MethodSpec.methodBuilder("updateAssets")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);


        // Get files
        FileHandle sfxPath = new FileHandle(Paths.get("assets/Sound Effects").toFile());
        sfxFiles = sfxPath.list("mp3");

        FileHandle texturePath = new FileHandle(Paths.get("assets/Textures").toFile());
        textureFiles = texturePath.list("png");


        CreateFields();
        CreateLoadingStatements();
        CreateGetStatements();

        typeSpecBuilder.addMethod(loadAssetMethodSpecBuilder.build());
        typeSpecBuilder.addMethod(updateAssetMethodSpecBuilder.build());

        // Styles are set here and the file is modeled.
        JavaFile javaFile = JavaFile.builder("com.javapoet", typeSpecBuilder.build())
                .indent("    ")
                .build();

        // Create output file here
        FileHandle target = new FileHandle(Paths.get("core/src/com/javapoet/Resources.java").toFile());
        target.writeString(javaFile.toString(), false);

        // Done building Resources.java
        Gdx.app.exit();
    }

    private void CreateFields() {
        // Create field with file name
        for (FileHandle sfxFile : sfxFiles){
            typeSpecBuilder.addField(Sound.class, toVariableName(sfxPrefix + upperFirstChar(sfxFile.nameWithoutExtension())), Modifier.PUBLIC, Modifier.STATIC);
        }
        for (FileHandle textureFile : textureFiles){
            typeSpecBuilder.addField(Texture.class, toVariableName(upperFirstChar(textureFile.nameWithoutExtension()) + "Texture"), Modifier.PUBLIC, Modifier.STATIC);
        }
    }
    private void CreateLoadingStatements() {
        // Add loading statement
        for (FileHandle sfxFile : sfxFiles) {
            loadAssetMethodSpecBuilder.addStatement("assetManager.load($S, $T.class)", "Sound Effects/" + sfxFile.name(), Sound.class);
        }
        for (FileHandle textureFile : textureFiles) {
            loadAssetMethodSpecBuilder.addStatement("assetManager.load($S, $T.class)", "Textures/" + textureFile.name(), Texture.class);
        }
    }

    private void CreateGetStatements() {
        updateAssetMethodSpecBuilder.beginControlFlow("if(assetManager.update())");
        for (FileHandle sfxFile : sfxFiles) {
            updateAssetMethodSpecBuilder.addStatement("$L = assetManager.get($S)", toVariableName(sfxPrefix + upperFirstChar(sfxFile.nameWithoutExtension())), "Sound Effects/" + sfxFile.name());
        }
        for (FileHandle textureFile : textureFiles) {
            updateAssetMethodSpecBuilder.addStatement("$L = assetManager.get($S)", toVariableName(upperFirstChar(textureFile.nameWithoutExtension()) + "Texture"), "Textures/" + textureFile.name());
        }
        updateAssetMethodSpecBuilder.endControlFlow();
    }

    /**
     * Cleans up names to remove illegal file name features.
     *
     * @param name - name to clean
     * @return - cleaned-up name string
     */
    private static String toVariableName(String name) {
        // Dashes, and periods are not allowed.
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

    /**
     * Capitalizes first character of the passed string.
     *
     * @param string
     * @return
     */
    private static String upperFirstChar(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    /**
     * Add sfx files to the resources.java file
     * @param typeSpecBuilder
     * @param methodSpecBuilder
     */
    private void CreateSoundFX(TypeSpec.Builder typeSpecBuilder, MethodSpec.Builder methodSpecBuilder){
        // Get files
        FileHandle sfxPath = new FileHandle(Paths.get("assets/Sound Effects").toFile());
        FileHandle[] sfxFiles = sfxPath.list("mp3");

        // Create field with file name
        for (FileHandle sfxFile : sfxFiles){
            typeSpecBuilder.addField(Sound.class, toVariableName(sfxPrefix + upperFirstChar(sfxFile.nameWithoutExtension())), Modifier.PUBLIC, Modifier.STATIC);
        }
        // Add loading statement
        for (FileHandle sfxFile : sfxFiles) {
            methodSpecBuilder.addStatement("assetManager.load($S, $T.class)", "Sound Effects/" + sfxFile.name(), Sound.class);
        }

        methodSpecBuilder.addStatement("assetManager.finishLoading()");

        for (FileHandle sfxFile : sfxFiles) {
            methodSpecBuilder.addStatement("$L = assetManager.get($S)", toVariableName(sfxPrefix + upperFirstChar(sfxFile.nameWithoutExtension())), "Sound Effects/" + sfxFile.name());
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