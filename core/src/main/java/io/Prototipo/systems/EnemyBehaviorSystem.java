package io.Prototipo.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import io.Prototipo.components.B2DBodyComponent;
import io.Prototipo.components.EnemyComponent;
import io.Prototipo.components.StateComponent;

public class EnemyBehaviorSystem extends IteratingSystem{

    ComponentMapper<EnemyComponent> em;
    ComponentMapper<B2DBodyComponent> bodm;
	ComponentMapper<StateComponent> sm;

    public EnemyBehaviorSystem(){
        super(Family.all(EnemyComponent.class).get());
        em = ComponentMapper.getFor(EnemyComponent.class);
        bodm = ComponentMapper.getFor(B2DBodyComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2DBodyComponent b2body = bodm.get(entity);
        EnemyComponent enemyCom = em.get(entity);
		StateComponent state = sm.get(entity);

        if(b2body.body.getLinearVelocity().y < 0){
			state.set(StateComponent.STATE_FALLING);
		}
		
		if(b2body.body.getLinearVelocity().y == 0){
			if(state.get() == StateComponent.STATE_FALLING){
				state.set(StateComponent.STATE_NORMAL);
			}
			if(b2body.body.getLinearVelocity().x != 0){
				state.set(StateComponent.STATE_MOVING);
			} 
		}

        if(enemyCom.id == EnemyComponent.NORMAL){
            if(state.get()!=StateComponent.STATE_FALLING){
                b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, -5f, 0.2f),b2body.body.getLinearVelocity().y);
            }

        }


    }

}
