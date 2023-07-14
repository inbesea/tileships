package com.javapoet.automation;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.nio.file.Paths;

public class GenerateResources extends ApplicationAdapter {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        new Lwjgl3Application(new GenerateResources(), config);
    }

    public void create(){
        FileHandle sfxPath = new FileHandle(Path.get("core/assets/sfx").toFile());
        FileHandle[] sfxFiles = sfxPath.list("mp3");
    }
}