
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.ArrayUtils;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;

/**
 * NOTE : If this fails with an error code it may be the name of the file having illegal chars somehow?
 *
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
    private FileHandle[] musicFiles;
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        new Lwjgl3Application(new GenerateResources(), config);
    }

    @Override
    public void create(){
        // Creates class
        typeSpecBuilder = TypeSpec.classBuilder(("Resources"))
            .addModifiers((Modifier.PUBLIC))
            .addField(AssetManager.class, "assetManager", Modifier.PUBLIC, Modifier.STATIC);

        // Holds loading statements added into method
        loadAssetMethodSpecBuilder = MethodSpec.methodBuilder("loadAssets")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addStatement("assetManager = new $T()", AssetManager.class);

        // Holds update statements added into method
        updateAssetMethodSpecBuilder = MethodSpec.methodBuilder("updateAssets")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC);


        // Get files
        updateAssetMethodSpecBuilder.beginControlFlow("if(assetManager.update())");
        handleFileTypeLoading("sfx", new String[]{"mp3", "wav"}, new String[]{"Sound Effects"}, Sound.class);
        handleFileTypeLoading("texture", new String[]{"png"}, new String[]{"Textures"}, Texture.class);
        handleFileTypeLoading("music", new String[]{"wav"}, new String[]{"Music"}, Music.class);
        updateAssetMethodSpecBuilder.endControlFlow();

        typeSpecBuilder.addMethod(loadAssetMethodSpecBuilder.build());
        typeSpecBuilder.addMethod(updateAssetMethodSpecBuilder.build());

        // Styles are set here and the file is modeled.
        JavaFile javaFile = JavaFile.builder("org.bitbucket.noahcrosby.javapoet", typeSpecBuilder.build())
            .indent("    ")
            .build();

        // Create output file here
        FileHandle target = new FileHandle(Paths.get("core/src/main/java/org/bitbucket/noahcrosby/javapoet/Resources.java").toFile());
        target.writeString(javaFile.toString(), false);

        // Done building Resources.java
        Gdx.app.exit();
    }

    /**
     * Add all we can pass the file location, the file type, and the locations.
     * @param filePostFix
     * @param fileExtensions
     * @param fileLocations
     */
    private void handleFileTypeLoading(String filePostFix, String[] fileExtensions, String[] fileLocations, Class classType) {
        /*
         * We need to implement this so
         * I want to be able to feed the appended type, type extension, and the locations to add a new type of file to this system.
         *
         * Look for all types of files in all locations.
         */
        // Get file location, ready file handle array
        FileHandle assetPath = new FileHandle(Paths.get("assets/" + fileLocations).toFile());
        FileHandle[] assetFiles = new FileHandle[0];

        // Populate filehandle array for locations and types.
        for (String fileLocation : fileLocations) {
            assetPath = new FileHandle(Paths.get("assets/" + fileLocation).toFile());
            for (String fileExtension : fileExtensions) {
                // Get files with each extension from this file location
                assetFiles = ArrayUtils.addAll(assetFiles, assetPath.list(fileExtension));
            }
        }

        // Create fields
        for(FileHandle file : assetFiles){
            typeSpecBuilder.addField(classType, toVariableName(upperFirstChar(file.nameWithoutExtension()) + upperFirstChar(filePostFix)),
                Modifier.PUBLIC, Modifier.STATIC);
        }

        // Create Loading Statements
        for(FileHandle file : assetFiles){
            loadAssetMethodSpecBuilder.addStatement("assetManager.load($S, $T.class)",
                file.parent().name() + "/" + file.name(), // Might not work, good debug location
                classType);
        }

        // Create Get Statements
        for(FileHandle file : assetFiles){
            updateAssetMethodSpecBuilder.addStatement("$L = assetManager.get($S)",
                toVariableName(upperFirstChar(file.nameWithoutExtension() + upperFirstChar(filePostFix))),
                file.parent().name() + "/" + file.name());
        }

    }

    /**
     * Cleans up names to remove illegal file name features.
     *
     * @param name  name to clean
     * @return  cleanedup name string
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

