package io.Prototipo;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import io.Prototipo.components.AnimationComponent;
import io.Prototipo.components.B2DBodyComponent;
import io.Prototipo.components.CollisionComponent;
import io.Prototipo.components.DoorComponent;
import io.Prototipo.components.PlayerComponent;
import io.Prototipo.components.StateComponent;
import io.Prototipo.components.TextureComponent;
import io.Prototipo.components.TransformComponent;
import io.Prototipo.components.TypeComponent;
import io.Prototipo.loader.B2DAssetManager;

public class LevelFactory {

    public static final int PLATFORM_SMALL = 1;
    public static final int PLATFORM_NORMAL = 2;
    public static final int PLATFORM_BIG = 3;

    public static final int PIT_SMALL = 4;
    public static final int PIT_NORMAL = 5;
    public static final int PIT_BIG = 6;

    public static final int BOX_NORMAL = 11;
    public static final int BOX_BIG = 12;

    public static final int FLOOR_NORMAL = 14;
    public static final int FLOOR_BIG = 15;

    private PooledEngine engine;
    private BodyFactory bodyFactory;
    private static LevelFactory thisInstance;
    private TextureAtlas atlas;
    private TextureRegion smallPlatTex;
    private TextureRegion normalPlatTex;
    private TextureRegion bigPlatTex;
    private TextureRegion smallPlatVertTex;
    private TextureRegion normalPlatVertTex;
    private TextureRegion bigPlatVertTex;
    private TextureRegion blockTex;
    private TextureRegion groundTex;
    private TextureRegion towerTex;
    private TextureRegion barLeft, barRight, barUp, barDown;
    private TextureRegion barLeftBig, barRightBig, barUpBig, barDownBig;
    private TextureRegion lavaFloor;

    public LevelFactory(PooledEngine e, BodyFactory b, B2DAssetManager assMan) {
        engine = e;
        bodyFactory = b;
        this.atlas = assMan.manager.get("images/Images.atlas", TextureAtlas.class);

        smallPlatTex = atlas.findRegion("small-platform");
        normalPlatTex = atlas.findRegion("normal-platform");
        bigPlatTex = atlas.findRegion("big-platform");
        smallPlatVertTex = atlas.findRegion("small-platform-vertical");
        normalPlatVertTex = atlas.findRegion("normal-platform-vertical");
        bigPlatVertTex = atlas.findRegion("big-platform-vertical");
        blockTex = atlas.findRegion("block");
        groundTex = atlas.findRegion("ground");
        towerTex = atlas.findRegion("tower");
        barDown = atlas.findRegion("bar_down");
        barLeft = atlas.findRegion("bar_vertical");
        barRight = atlas.findRegion("bar_vertical");
        barUp = atlas.findRegion("bar_up");
        barDownBig = atlas.findRegion("bar_down_big");
        barLeftBig = atlas.findRegion("bar_vertical_big");
        barRightBig = atlas.findRegion("bar_vertical_big");
        barUpBig = atlas.findRegion("bar_up_big");
        lavaFloor = atlas.findRegion("lava-floor");
    }

    public static LevelFactory getInstance(PooledEngine e, BodyFactory b, B2DAssetManager am) {
        if (thisInstance == null) {
            thisInstance = new LevelFactory(e, b, am);
        }

        return thisInstance;
    }

    public Entity createPlayer(float x, float y, int id) {

        Entity entity = engine.createEntity();
        B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        AnimationComponent animation = new AnimationComponent();

        stateCom.set(StateComponent.STATE_NORMAL);

        if (id == PlayerComponent.ONE) {
            animation.animations.put(StateComponent.STATE_NORMAL,
                    new Animation<>(0.2f, atlas.findRegions("mulher_idle")));
            animation.animations.put(StateComponent.STATE_MOVING,
                    new Animation<>(0.08f, atlas.findRegions("mulher_walk")));
            animation.animations.put(StateComponent.STATE_JUMPING,
                    new Animation<>(0.15f, atlas.findRegions("mulher_jump")));
            animation.animations.put(StateComponent.STATE_FALLING,
                    new Animation<>(0.1f, atlas.findRegions("mulher_falling")));
            texture.region = animation.animations.get(stateCom.get()).getKeyFrame(0);
        } else if (id == PlayerComponent.TWO) {
            animation.animations.put(StateComponent.STATE_NORMAL,
                    new Animation<>(0.2f, atlas.findRegions("homem_idle")));
            animation.animations.put(StateComponent.STATE_MOVING,
                    new Animation<>(0.08f, atlas.findRegions("homem_walk")));
            texture.region = animation.animations.get(stateCom.get()).getKeyFrame(0);
            animation.animations.put(StateComponent.STATE_JUMPING,
                    new Animation<>(0.15f, atlas.findRegions("homem_jump")));
            animation.animations.put(StateComponent.STATE_FALLING,
                    new Animation<>(0.1f, atlas.findRegions("homem_falling")));
            texture.region = animation.animations.get(stateCom.get()).getKeyFrame(0);

        }
        b2dbody.body = bodyFactory.makeCirclePolyBody(x, y, 1f, BodyFactory.STONE);

        position.position.set(x, y, 10);
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);
        player.id = id;

        // add the components to the entity
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(player);
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);
        entity.add(animation);

        // add the entity to the engine
        engine.addEntity(entity);

        return entity;

    }

    public void createPlatform(float x, float y, int size, boolean vertical) {
        Entity entity = engine.createEntity();
        B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        switch (size) {
            case PLATFORM_SMALL:
                if (vertical) {
                    b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, .75f, 25f, BodyFactory.STONE, BodyType.StaticBody);
                    texture.region = smallPlatVertTex;
                } else {
                    b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 2F, 0.75f, BodyFactory.STONE, BodyType.StaticBody);
                    texture.region = smallPlatTex;
                }
                break;
            case PLATFORM_NORMAL:
                if (vertical) {
                    b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, .75f, 3f, BodyFactory.STONE, BodyType.StaticBody);
                    texture.region = normalPlatVertTex;
                } else {
                    b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 3, 0.75f, BodyFactory.STONE, BodyType.StaticBody);
                    texture.region = normalPlatTex;
                }
                break;
            case PLATFORM_BIG:
                if (vertical) {
                    b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, .75f, 4f, BodyFactory.STONE, BodyType.StaticBody);
                    texture.region = bigPlatVertTex;
                } else {
                    b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 4F, 0.75f, BodyFactory.STONE, BodyType.StaticBody);
                    texture.region = bigPlatTex;
                }
                break;
        }
        TypeComponent type = engine.createComponent(TypeComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        position.position.set(x, y, -10);
        type.type = TypeComponent.GROUND;
        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        entity.add(position);

        engine.addEntity(entity);

    }

    public void createBox(int size) {
        Entity floor = engine.createEntity();
        B2DBodyComponent b2dbodyFloor = engine.createComponent(B2DBodyComponent.class);
        TextureComponent textureFloor = engine.createComponent(TextureComponent.class);
        TransformComponent positionFloor = engine.createComponent(TransformComponent.class);

        // cria entidades e componentes das paredes/teto antes do if
        Entity leftWall = engine.createEntity();
        B2DBodyComponent b2dbodyLW = engine.createComponent(B2DBodyComponent.class);
        TextureComponent textureLW = engine.createComponent(TextureComponent.class);
        TransformComponent positionLW = engine.createComponent(TransformComponent.class);
        // textureLW.region = atlas.findRegion("player");

        Entity rightWall = engine.createEntity();
        B2DBodyComponent b2dbodyRW = engine.createComponent(B2DBodyComponent.class);
        TextureComponent textureRW = engine.createComponent(TextureComponent.class);
        TransformComponent positionRW = engine.createComponent(TransformComponent.class);
        // textureRW.region = atlas.findRegion("player");

        Entity roof = engine.createEntity();
        B2DBodyComponent b2dbodyRoof = engine.createComponent(B2DBodyComponent.class);
        TextureComponent textureRoof = engine.createComponent(TextureComponent.class);
        TransformComponent positionRoof = engine.createComponent(TransformComponent.class);
        // textureRoof.region = atlas.findRegion("player");

        switch (size) {

            case BOX_NORMAL:
                b2dbodyFloor.body = bodyFactory.makeBoxPolyBody(
                        0, -14f, 29, 1f, BodyFactory.STONE, BodyType.StaticBody);
                textureFloor.region = barDown;
                positionFloor.position.set(0, -14f, 1);

                b2dbodyLW.body = bodyFactory.makeBoxPolyBody(
                        -14f, 0, 1f, 27f, BodyFactory.STONE, BodyType.StaticBody);
                textureLW.region = barLeft;
                positionLW.position.set(-14, 0, 1);

                b2dbodyRW.body = bodyFactory.makeBoxPolyBody(
                        14f, 0, 1f, 27f, BodyFactory.STONE, BodyType.StaticBody);
                textureRW.region = barRight;
                positionRW.position.set(14, 0, 1);

                b2dbodyRoof.body = bodyFactory.makeBoxPolyBody(
                        0, 14f, 29, 1f, BodyFactory.STONE, BodyType.StaticBody);
                textureRoof.region = barUp;
                positionRoof.position.set(0, 14, 1);
                break;

            case BOX_BIG:

                b2dbodyFloor.body = bodyFactory.makeBoxPolyBody(
                        0, -18.8f, 38.625f, 1f, BodyFactory.STONE, BodyType.StaticBody);
                textureFloor.region = barDownBig;
                positionFloor.position.set(0, -18.8f, 0);

                b2dbodyLW.body = bodyFactory.makeBoxPolyBody(
                        -18.8f, 0, 1f, 36.625f, BodyFactory.STONE, BodyType.StaticBody);
                textureLW.region = barLeftBig;
                positionLW.position.set(-18.8f, 0, 0);

                b2dbodyRW.body = bodyFactory.makeBoxPolyBody(
                        18.8f, 0f, 1f, 36.625f, BodyFactory.STONE, BodyType.StaticBody);
                textureRW.region = barRightBig;
                positionRW.position.set(18.8f, 0, 0);

                b2dbodyRoof.body = bodyFactory.makeBoxPolyBody(
                        0, 18.8f, 38.625f, 1f, BodyFactory.STONE, BodyType.StaticBody);
                textureRoof.region = barUpBig;
                positionRoof.position.set(0, 18.8f, 0);

                break;

        }

        TypeComponent typeFloor = engine.createComponent(TypeComponent.class);
        typeFloor.type = TypeComponent.SCENERY;
        b2dbodyFloor.body.setUserData(floor);
        floor.add(b2dbodyFloor);
        floor.add(textureFloor);
        floor.add(typeFloor);
        floor.add(positionFloor);

        // LEFT WALL

        TypeComponent typeLW = engine.createComponent(TypeComponent.class);
        typeLW.type = TypeComponent.SCENERY;

        b2dbodyLW.body.setUserData(leftWall);
        leftWall.add(b2dbodyLW);
        leftWall.add(textureLW);
        leftWall.add(typeLW);
        leftWall.add(positionLW);

        // RIGHT WALL
        TypeComponent typeRW = engine.createComponent(TypeComponent.class);
        typeRW.type = TypeComponent.SCENERY;

        b2dbodyRW.body.setUserData(rightWall);
        rightWall.add(b2dbodyRW);
        rightWall.add(textureRW);
        rightWall.add(typeRW);
        rightWall.add(positionRW);

        // ROOF

        TypeComponent typeRoof = engine.createComponent(TypeComponent.class);
        typeRoof.type = TypeComponent.SCENERY;

        b2dbodyRoof.body.setUserData(roof);
        roof.add(b2dbodyRoof);
        roof.add(textureRoof);
        roof.add(typeRoof);
        roof.add(positionRoof);

        // adiciona entidades
        engine.addEntity(roof);
        engine.addEntity(leftWall);
        engine.addEntity(rightWall);
        engine.addEntity(floor);

    }

    public void createTower(float x, float y) {

        Entity entity = engine.createEntity();
        B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);

        texture.region = towerTex;

        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 5, 10, BodyFactory.STONE, BodyType.StaticBody);
        type.type = TypeComponent.GROUND;
        position.position.set(x, y, -9);

        entity.add(position);
        entity.add(b2dbody);
        entity.add(type);
        entity.add(texture);
        engine.addEntity(entity);

    }

    public Entity createDoor(float x, float y, int id) {

        Entity entity = engine.createEntity();
        B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        state.isLooping = true;

        if (id == DoorComponent.ONE) {
            animation.animations.put(StateComponent.STATE_NORMAL,
                    new Animation<>(0.1f, atlas.findRegions("yellow_portal")));
            texture.region = animation.animations.get(state.get()).getKeyFrame(0);
        } else if (id == DoorComponent.TWO) {
            animation.animations.put(StateComponent.STATE_NORMAL,
                    new Animation<>(.1f, atlas.findRegions("purple_portal")));
            texture.region = animation.animations.get(state.get()).getKeyFrame(0);
        }
        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 1.75f, 2f, BodyFactory.STONE, BodyType.StaticBody);

        bodyFactory.makeAllFixturesSensor(b2dbody.body);
        DoorComponent door = engine.createComponent(DoorComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        position.position.set(x, y, -8);
        type.type = TypeComponent.DOOR;
        door.id = id;

        b2dbody.body.setUserData(entity);

        entity.add(door);
        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        entity.add(position);
        entity.add(animation);
        entity.add(state);
        engine.addEntity(entity);
        return entity;

    }

    public void createBlock(float x) {
        Entity block = engine.createEntity();
        B2DBodyComponent b2dbodyBlock = engine.createComponent(B2DBodyComponent.class);
        b2dbodyBlock.body = bodyFactory.makeBoxPolyBody(x, -10.25f, 9.5f, 6.5f, BodyFactory.STONE, BodyType.StaticBody);
        TextureComponent textureBlock = engine.createComponent(TextureComponent.class);
        textureBlock.region = blockTex;
        TypeComponent typeBlock = engine.createComponent(TypeComponent.class);
        typeBlock.type = TypeComponent.GROUND;
        TransformComponent position = engine.createComponent(TransformComponent.class);
        position.position.set(x, -10.25f, -9);

        b2dbodyBlock.body.setUserData(block);
        block.add(position);
        block.add(b2dbodyBlock);
        block.add(textureBlock);
        block.add(typeBlock);

        engine.addEntity(block);

    }

    public void createGround(float x) {
        Entity block = engine.createEntity();
        B2DBodyComponent b2dbodyBlock = engine.createComponent(B2DBodyComponent.class);
        b2dbodyBlock.body = bodyFactory.makeBoxPolyBody(x, -10.25f, 27f, 6.5f, BodyFactory.STONE, BodyType.StaticBody);
        TextureComponent textureBlock = engine.createComponent(TextureComponent.class);
        textureBlock.region = groundTex;
        TypeComponent typeBlock = engine.createComponent(TypeComponent.class);
        typeBlock.type = TypeComponent.GROUND;
        TransformComponent position = engine.createComponent(TransformComponent.class);
        position.position.set(x, -10.25f, -9);

        b2dbodyBlock.body.setUserData(block);
        block.add(position);
        block.add(b2dbodyBlock);
        block.add(textureBlock);
        block.add(typeBlock);

        engine.addEntity(block);

    }

    public void createFloor(float y, boolean isLava, int size) {

        Entity floor = engine.createEntity();
        B2DBodyComponent b2dbodyFloor = engine.createComponent(B2DBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TypeComponent typeFloor = engine.createComponent(TypeComponent.class);
        state.set(StateComponent.STATE_NORMAL);
        state.isLooping = true;

        position.position.set(0, y, -9);

        switch (size) {
            case FLOOR_NORMAL:

                b2dbodyFloor.body = bodyFactory.makeBoxPolyBody(0, y, 27f, 0.75f, BodyFactory.STONE,
                        BodyType.StaticBody);
                if (isLava) {
                    typeFloor.type = TypeComponent.PIT;
                } else {
                    typeFloor.type = TypeComponent.SCENERY;
                }

                break;

            case FLOOR_BIG:

                b2dbodyFloor.body = bodyFactory.makeBoxPolyBody(0, y, 36.5625f, 0.75f, BodyFactory.STONE,
                        BodyType.StaticBody);
                if (isLava) {
                    typeFloor.type = TypeComponent.PIT;
                    animation.animations.put(StateComponent.STATE_NORMAL,
                            new Animation<>(0.08f, atlas.findRegions("giant-pit")));
                    texture.region = animation.animations.get(state.get()).getKeyFrame(0);
                } else {
                    typeFloor.type = TypeComponent.SCENERY;
                    texture.region = lavaFloor;
                }

                
                break;
        }

        b2dbodyFloor.body.setUserData(floor);
        floor.add(b2dbodyFloor);
        floor.add(typeFloor);
        floor.add(position);
        floor.add(animation);
        floor.add(texture);
        floor.add(state);
        engine.addEntity(floor);

    }

    public void createPit(float x, float y, int size) {

        Entity entity = engine.createEntity();
        B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        state.set(StateComponent.STATE_NORMAL);
        state.isLooping = true;
        switch (size) {
            case PIT_SMALL:
                b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 1.5f, .75f, BodyFactory.STONE, BodyType.StaticBody);
                animation.animations.put(StateComponent.STATE_NORMAL,
                        new Animation<>(0.08f, atlas.findRegions("small-pit")));
                texture.region = animation.animations.get(state.get()).getKeyFrame(0);
                break;

            case PIT_NORMAL:
                b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 2, .75f, BodyFactory.STONE, BodyType.StaticBody);
                animation.animations.put(StateComponent.STATE_NORMAL,
                        new Animation<>(0.08f, atlas.findRegions("normal-pit")));
                texture.region = animation.animations.get(state.get()).getKeyFrame(0);
                break;

            case PIT_BIG:
                b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 8f, .75f, BodyFactory.STONE, BodyType.StaticBody);
                animation.animations.put(StateComponent.STATE_NORMAL,
                        new Animation<>(0.08f, atlas.findRegions("big-pit")));
                texture.region = animation.animations.get(state.get()).getKeyFrame(0);
                break;
        }
        TypeComponent type = engine.createComponent(TypeComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        position.position.set(x, y, -9);
        type.type = TypeComponent.PIT;

        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        entity.add(position);
        entity.add(state);
        entity.add(animation);

        engine.addEntity(entity);

    }

}
