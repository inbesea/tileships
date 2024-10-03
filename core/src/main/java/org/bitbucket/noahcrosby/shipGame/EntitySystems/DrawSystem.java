package org.bitbucket.noahcrosby.shipGame.EntitySystems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import org.bitbucket.noahcrosby.shipGame.Components.PositionComponent;
import org.bitbucket.noahcrosby.shipGame.Components.TextureComponent;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.util.Animator;

/**
 * Draws textures and animations
  */
public class DrawSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ComponentMapper<Animator> animatorMapper = ComponentMapper.
        getFor(Animator.class);
    private ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);

    public DrawSystem(){}

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.one(TextureComponent.class, Animator.class, PositionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(int i = 0 ; i < entities.size() ; i++){
            Entity entity = entities.get(i);

            Animator animator = animatorMapper.get(entity);
            TextureComponent textureComponent = textureMapper.get(entity);
            PositionComponent positionComponent = positionMapper.get(entity);

            if(positionComponent == null)continue; // need position to draw

            if(animator != null) {
                animator.render(TileShipGame.batch);
            }
            if(textureComponent != null) {
                TileShipGame.batch.draw(textureComponent.texture, positionComponent.position.x, positionComponent.position.y);
            }
            // Does not take size into consideration yet. Todo : add size.
        }
    }
}
