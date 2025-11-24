package io.Prototipo.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.Prototipo.Prototipo;
import io.Prototipo.components.PlayerComponent;
import io.Prototipo.controller.KeyboardController;

public class DoorCheckSystem extends EntitySystem{

    KeyboardController controller;
    private ImmutableArray<Entity> players;
    private Prototipo prototipo;

    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);

    public DoorCheckSystem(KeyboardController c, Prototipo p){
        controller = c;
        prototipo = p;
    }

    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) { 

        int readyPlayers = 0;

        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i);
            PlayerComponent pc = pm.get(player);
            if (pc.isAtDoor) {
                readyPlayers++;
            }
        }

        if (readyPlayers >= 2 && controller.down || readyPlayers>=2 && controller.s) {
            prototipo.changeScreen(Prototipo.WIN);
        }
        
    }

}
