package org.bitbucket.noahcrosby.shipGame.EntitySystems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.AppPreferences;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.Components.CollectableComponent;
import org.bitbucket.noahcrosby.shipGame.Components.CollectorComponent;
import org.bitbucket.noahcrosby.shipGame.Components.IDComponent;
import org.bitbucket.noahcrosby.shipGame.Components.PositionComponent;
import org.bitbucket.noahcrosby.shipGame.Money;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

/**
 * Gets collectables, and collector objects.
 * Updates their positions, or collected state.
 */
public class CollectableUpdateSystem extends EntitySystem {
    private ImmutableArray<Entity> collectableEntities;
    private Entity collectable, collector;

    Vector2 collectablePosition, collectorPosition;

    float deltaTime;


    private ComponentMapper<CollectableComponent> collectableMapper = ComponentMapper
        .getFor(CollectableComponent.class);
    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);


    ImmutableArray<Entity> collectors;

    public CollectableUpdateSystem(){}

    public void addedToEngine(Engine engine) {
        collectableEntities = engine.getEntitiesFor(
            Family.all(CollectableComponent.class, PositionComponent.class).get());

        collectors = engine.getEntitiesFor(
            Family.all(IDComponent.class, CollectorComponent.class, PositionComponent.class).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        this.deltaTime = deltaTime;
        updateCollectablesBasedOnIDs();
    }

    private void updateCollectablesBasedOnIDs() {

        for (int i = 0 ; i < collectableEntities.size() ; i++){
            collectable = collectableEntities.get(i);
            getCollectablePosition();

            // Get collectable's collector object
            // 1.
            for(int n = 0; n < collectors.size() ; n++){
                collector = collectors.get(n);

                if(!IDsMatch()){continue;} // Skip non-matchers
                getCollectorPosition();

                int closeness = entitiesAreClose();
                if(closeness == 0){
                    collectCurrent();
                } else if (closeness == 1){
                    MoveCloser();
                } else { // Does not need updated
                    stopMovement();
                }

            }
        }

    }

    private void getCollectorPosition() {
        collectorPosition = collector.getComponent(PositionComponent.class).position;
    }

    private void getCollectablePosition() {
        collectablePosition = collectable.getComponent(PositionComponent.class).position;
    }

    private void stopMovement() {

    }

    private void collectCurrent() {
        TileShipGame.engine.removeEntity(collectable);

        Money.addCoin();
        Resources.PickupCoinSfx.play(AppPreferences.getAppPreferences().getSoundVolume(),
            generalUtil.getRandomNumber(0.9f, 1.1f),
            0);
    }

    private void MoveCloser() {
        float moveSpeed = 0.1f;
        // Move towards a point right?
        collectablePosition.x += (collectorPosition.x - collectablePosition.x) * moveSpeed;
        collectablePosition.y += (collectorPosition.y - collectablePosition.y) * moveSpeed;

//        collectorPosition
    }

    private int entitiesAreClose() {
        int attractableDistance = collectable.getComponent(CollectableComponent.class).collectability + collector.getComponent(CollectorComponent.class).collectability;
        Float distanceBetween = collectablePosition.dst(collectorPosition);
        if(distanceBetween < 10) return 0;
        if(distanceBetween < attractableDistance) return 1;
        else return 2;
    }


    private boolean IDsMatch() {
        return collector.getComponent(IDComponent.class).id == collectable.getComponent(CollectableComponent.class).getCollectors().get(0);
    }
}
