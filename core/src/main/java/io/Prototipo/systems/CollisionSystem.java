package io.Prototipo.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.Prototipo.components.CollisionComponent;
import io.Prototipo.components.PlayerComponent;
import io.Prototipo.components.TypeComponent;

public class CollisionSystem extends IteratingSystem {
	ComponentMapper<CollisionComponent> cm;
	ComponentMapper<PlayerComponent> pm;

	public CollisionSystem() {
		// only need to worry about player collisions
		super(Family.all(CollisionComponent.class, PlayerComponent.class).get());

		cm = ComponentMapper.getFor(CollisionComponent.class);
		pm = ComponentMapper.getFor(PlayerComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// get player collision component
		CollisionComponent cc = cm.get(entity);
		PlayerComponent pc = pm.get(entity);
		Entity collidedEntity = cc.collisionEntity;
		if (collidedEntity != null) {
			TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
			if (type != null) {
				switch (type.type) {
					case TypeComponent.ENEMY:
						// do player hit enemy thing
						System.out.println("player hit enemy");
						pc.isDead = true;
						System.out.println(pc.isDead);
						break;
					case TypeComponent.SCENERY:
						// do player hit scenery thing
						System.out.println("player hit scenery");
						break;
					case TypeComponent.OTHER:
						// do player hit other thing
						System.out.println("player hit other");
						break; 
					case TypeComponent.DOOR:
						
						break;
					case TypeComponent.PIT:
						pc.isDead = true;
						System.out.println("Tocou na lava");
						System.out.println(pc.isDead);
				}
				if (type.type != TypeComponent.DOOR) {
					cc.collisionEntity = null; // collision handled reset component
				}
			}
		}

	}
}
