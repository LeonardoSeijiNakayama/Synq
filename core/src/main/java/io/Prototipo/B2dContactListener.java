package io.Prototipo;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import io.Prototipo.components.CollisionComponent;
import io.Prototipo.components.DoorComponent;
import io.Prototipo.components.PlayerComponent;
import io.Prototipo.components.TypeComponent;

public class B2dContactListener implements ContactListener {

	Engine engine;
	World world;

	public B2dContactListener(Engine engine, World world) {
		this.engine = engine;
		this.world = world;
	}

	@Override
	public void beginContact(Contact contact) {
		System.out.println("Contact");
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		System.out.println(fa.getBody().getType() + " has hit " + fb.getBody().getType());

		if (fa.getBody().getUserData() instanceof Entity) {
			Entity ent = (Entity) fa.getBody().getUserData();
			entityCollision(ent, fb);
			return;
		} else if (fb.getBody().getUserData() instanceof Entity) {
			Entity ent = (Entity) fb.getBody().getUserData();
			entityCollision(ent, fa);
			return;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	private void entityCollision(Entity ent, Fixture otherFixture) {
		if (!(otherFixture.getBody().getUserData() instanceof Entity))
			return;

		Entity otherEnt = (Entity) otherFixture.getBody().getUserData();

		// Força detecção se uma das entidades for o jogador
		PlayerComponent playerA = ent.getComponent(PlayerComponent.class);
		PlayerComponent playerB = otherEnt.getComponent(PlayerComponent.class);

		if (playerA != null) {
			CollisionComponent col = ent.getComponent(CollisionComponent.class);
			if (col != null) {
				col.collisionEntity = otherEnt;
			}
		} else if (playerB != null) {
			CollisionComponent col = otherEnt.getComponent(CollisionComponent.class);
			if (col != null) {
				col.collisionEntity = ent;
			}
		}

		// Detecção de porta
		TypeComponent typeA = ent.getComponent(TypeComponent.class);
		TypeComponent typeB = otherEnt.getComponent(TypeComponent.class);
		if (typeA != null && typeB != null) {
			if (typeA.type == TypeComponent.PLAYER && typeB.type == TypeComponent.DOOR) {
				PlayerComponent pc = ent.getComponent(PlayerComponent.class);
				DoorComponent dc = otherEnt.getComponent(DoorComponent.class);
				if (pc != null && dc != null && pc.id == dc.id) {
					pc.isAtDoor = true;
				}
			} else if (typeB.type == TypeComponent.PLAYER && typeA.type == TypeComponent.DOOR) {
				PlayerComponent pc = otherEnt.getComponent(PlayerComponent.class);
				DoorComponent dc = ent.getComponent(DoorComponent.class);
				if (pc != null && dc != null && pc.id == dc.id) {
					pc.isAtDoor = true;
				}
			}

			
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa.getBody().getUserData() instanceof Entity && fb.getBody().getUserData() instanceof Entity) {
			Entity entA = (Entity) fa.getBody().getUserData();
			Entity entB = (Entity) fb.getBody().getUserData();

			TypeComponent typeA = entA.getComponent(TypeComponent.class);
			TypeComponent typeB = entB.getComponent(TypeComponent.class);

			if (typeA != null && typeB != null) {
				if (typeA.type == TypeComponent.PLAYER && typeB.type == TypeComponent.DOOR) {
					PlayerComponent pc = entA.getComponent(PlayerComponent.class);
					DoorComponent dc = entB.getComponent(DoorComponent.class);
					if (pc != null && dc != null) {
						if (pc.id == dc.id) {
							pc.isAtDoor = false;
						}
					}
				} else if (typeB.type == TypeComponent.PLAYER && typeA.type == TypeComponent.DOOR) {
					PlayerComponent pc = entB.getComponent(PlayerComponent.class);
					DoorComponent dc = entA.getComponent(DoorComponent.class);
					if (pc != null && dc != null) {
						if (pc.id == dc.id) {
							pc.isAtDoor = false;
						}
					}
				}

				
			}
		}

	}

}
