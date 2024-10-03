package org.bitbucket.noahcrosby.shipGame.effects;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

public class EffectsHandler implements Listener<EffectContent> {

    private OrthographicCamera camera;
    int screenshakeDuration = 0;
    int defaultShakeIntensity = 6;
    int defaultShakeDuration = 60;
    private Vector3 originalPosition = new Vector3();


    public EffectsHandler(){

    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void receive(Signal<EffectContent> signal, EffectContent effectsContent) {
        if(effectsContent.getEffectType() == EffectContent.BLOCK_PLACEMENT){

//            camera.position.add(generalUtil.getRandomNumber(-20,20), generalUtil.getRandomNumber(-20,20), 0);
            System.out.println("Setting orig.x to " + camera.position.x + " from " + originalPosition.x);
            screenshakeDuration = defaultShakeDuration;
            this.originalPosition.x = camera.position.x;
            this.originalPosition.y = camera.position.y;
//            camera.
        }
    }

    public void update(){
        if(screenshakeDuration > 0){
            float msDelta = Gdx.graphics.getDeltaTime()*1000;
            screenshakeDuration -= msDelta;
            if(camera != null){
                camera.position.add(generalUtil.getRandomNumber(-defaultShakeIntensity, defaultShakeIntensity), generalUtil.getRandomNumber(-defaultShakeIntensity, defaultShakeIntensity), 0);
                if(screenshakeDuration <= 0){
                    camera.position.x = originalPosition.x;
                    camera.position.y = originalPosition.y;
                }
            }
        }
    }
}
