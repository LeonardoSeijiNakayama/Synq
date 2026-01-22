package io.Prototipo.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import io.Prototipo.components.B2DBodyComponent;
import io.Prototipo.components.PlayerComponent;
import io.Prototipo.components.StateComponent;
import io.Prototipo.controller.KeyboardController;
import io.Prototipo.loader.Assets;
import io.Prototipo.views.MainScreen;

public class PlayerControlSystem extends IteratingSystem {
	ComponentMapper<PlayerComponent> pm;
	ComponentMapper<B2DBodyComponent> bodm;
	ComponentMapper<StateComponent> sm;
	KeyboardController controller;
	private AssetManager am;
	private MainScreen parent;

	public PlayerControlSystem(KeyboardController keyCon, AssetManager am, MainScreen ms) {
		super(Family.all(PlayerComponent.class).get());
		controller = keyCon;
		pm = ComponentMapper.getFor(PlayerComponent.class);
		bodm = ComponentMapper.getFor(B2DBodyComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
		parent = ms;
		this.am = am;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		B2DBodyComponent b2 = bodm.get(entity);
		StateComponent st = sm.get(entity);
		PlayerComponent pc = pm.get(entity);

		float vx = b2.body.getLinearVelocity().x;
		float vy = b2.body.getLinearVelocity().y;

		final float EPS_VX = 0.05f;
		final float EPS_VY = 0.05f;

		boolean grounded = Math.abs(vy) < EPS_VY;

        if(controller.r){
            parent.resetWorld(parent.currentPhase);
        }

		if(controller.esc){
			parent.returnToMenu();
		}

		if (controller.left || controller.a) {
			moveLeft(pc, b2);
			pc.left = true;
		} else if (controller.right || controller.d) {
			moveRight(pc, b2);
			pc.left = false;
		} else {
			b2.body.setLinearVelocity(MathUtils.lerp(vx, 0, 0.1f), b2.body.getLinearVelocity().y);
		}

		if ((controller.up || controller.w || controller.shift) && grounded) {
			st.set(StateComponent.STATE_JUMP_WINDUP);
			st.isLooping = false;
			st.time = 0;
			st.auxTimer = 0.1f;
		}

		if (st.get() == StateComponent.STATE_JUMP_WINDUP) {
			if(parent.parent.getPreferences().isSoundEffectEnabled()){
				am.get(Assets.SFX_JUMP).play(parent.parent.getPreferences().getSoundVolume());
			}
			jump(pc, b2);

		}

		int desired = st.get();
		boolean loop = true;

		if (!grounded) {
			desired = StateComponent.STATE_FALLING;
			loop = true;
			if (vy > EPS_VY) {
				desired = StateComponent.STATE_JUMPING;
				loop = false;
			}
		} else {
			if (Math.abs(vx) > EPS_VX) {
				desired = StateComponent.STATE_MOVING;
				loop = true;
			} else {
				desired = StateComponent.STATE_NORMAL;
				loop = true;
			}
		}

		if (desired != st.get()) {
			st.set(desired);
			st.isLooping = loop;
		} else {
			st.isLooping = loop;
		}
	}

	public void moveRight(PlayerComponent player, B2DBodyComponent b2body) {

		b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x,
				5f, 0.2f),
				b2body.body.getLinearVelocity().y);

	}

	public void moveLeft(PlayerComponent player, B2DBodyComponent b2body) {

		b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x,
				-5f, 0.2f),
				b2body.body.getLinearVelocity().y);

	}

	public void jump(PlayerComponent player, B2DBodyComponent b2body) {

		b2body.body.applyLinearImpulse(0, 6.5f, b2body.body.getWorldCenter().x, b2body.body.getWorldCenter().y,
				true);
	}

}
