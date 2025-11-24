package io.Prototipo.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.Prototipo.B2dContactListener;
import io.Prototipo.BodyFactory;
import io.Prototipo.LevelFactory;
import io.Prototipo.Prototipo;
import io.Prototipo.components.DoorComponent;
import io.Prototipo.components.PlayerComponent;
import io.Prototipo.controller.KeyboardController;
import io.Prototipo.loader.Assets;
import io.Prototipo.systems.AnimationSystem;
import io.Prototipo.systems.CollisionSystem;
import io.Prototipo.systems.DoorCheckSystem;
import io.Prototipo.systems.EnemyBehaviorSystem;
import io.Prototipo.systems.PhysicsDebugSystem;
import io.Prototipo.systems.PhysicsSystem;
import io.Prototipo.systems.PlayerControlSystem;
import io.Prototipo.systems.RenderingSystem;;

public class MainScreen implements Screen {

    public Prototipo parent;
    private TextureRegion backgroundTex;
    OrthographicCamera cam;
    KeyboardController controller;
    SpriteBatch sb;
    PooledEngine engine;
    World world;
    BodyFactory bodyFactory;
    LevelFactory levelFactory;
    Vector2 screenMeterDimensions;
    RenderingSystem renderingSystem;
    TextureAtlas atlas;
    boolean isPlaying = false;
    public int currentPhase;

    Entity playerOne, playerTwo;

    public MainScreen(Prototipo box2dTutorial, int number) {
        System.out.println("FASE UM CRIADA");
        parent = box2dTutorial;
        controller = new KeyboardController();

        atlas = parent.assMan.manager.get("images/Images.atlas");

        currentPhase = number;
        System.out.println(number);
        applyBackground(number);

        engine = new PooledEngine();

        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(new B2dContactListener(engine, world));
        bodyFactory = BodyFactory.getInstance(world);
        levelFactory = LevelFactory.getInstance(engine, bodyFactory, parent.assMan);

        sb = parent.batch;

        renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();
        screenMeterDimensions = RenderingSystem.getScreenSizeInMeters();
        sb.setProjectionMatrix(cam.combined);

        engine.addSystem(new AnimationSystem());
        engine.addSystem(renderingSystem); 
        engine.addSystem(new PhysicsSystem(world));
        //engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new EnemyBehaviorSystem());
        engine.addSystem(new PlayerControlSystem(controller, parent.assMan.manager, this));
        engine.addSystem(new DoorCheckSystem(controller, parent));

        if(!isPlaying){
            isPlaying = true;
            if(parent.getPreferences().isMusicEnabled()){
                Music music = parent.assMan.manager.get(Assets.PIXEL_SHADOWS);
                music.setLooping(true);
                music.setVolume(parent.getPreferences().getMusicVolume());
                music.play();;
            }
            
        }
        // create some game objects

        switch (number) {
            case Prototipo.PHASE1:

                generateLevelOne();

                break;

            case Prototipo.PHASE2:

                generateLevelTwo();

                break;

            case Prototipo.PHASE3:

                generateLevelThree();

                break;

            case Prototipo.PHASE4: 

                generateLevelFour();

            break;
        }

    }

    @Override
    public void show() {
        controller.reset();
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderingSystem.getViewport().apply();
        cam = renderingSystem.getCamera();
        cam.update();

        // tamanho atual do frustum em unidades de mundo
        float vw = renderingSystem.getViewport().getWorldWidth();
        float vh = renderingSystem.getViewport().getWorldHeight();

        // desenhar centralizado na posição da câmera
        float x = cam.position.x - vw / 2f;
        float y = cam.position.y - vh / 2f;

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(backgroundTex, x, y, vw, vh); 
        sb.end();

        PlayerComponent pco = (playerOne.getComponent(PlayerComponent.class));
        PlayerComponent pct = (playerTwo.getComponent(PlayerComponent.class));
        if (pco.isDead || pct.isDead) {
            controller.reset();
            parent.changeScreen(Prototipo.LOSE);
        }

        engine.update(delta);

    }

    @Override
    public void resize(int width, int height) {
        renderingSystem.getViewport().update(width, height, false);
        renderingSystem.getCamera().position.set(0f, 0f, 0f);
        renderingSystem.getCamera().update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        parent.assMan.manager.get(Assets.PIXEL_SHADOWS).stop();
        isPlaying = false;
        controller.reset();
    }

    @Override
    public void dispose() {
        sb.dispose(); 
    }

    public void resetWorld(int number) {
        engine.removeAllEntities();
        Array<Body> bods = new Array<>();
        world.getBodies(bods);
        for (Body b : bods) {
            world.destroyBody(b);
        }

        currentPhase = number;

        applyBackground(number);

        if(!isPlaying){
            isPlaying = true;
            if(parent.getPreferences().isMusicEnabled()){
                Music music = parent.assMan.manager.get(Assets.PIXEL_SHADOWS);
                music.setLooping(true);
                music.setVolume(parent.getPreferences().getMusicVolume());
                music.play();;
            }
          
        }
        switch (number) {
            case Prototipo.PHASE1:

                generateLevelOne();

                break;

            case Prototipo.PHASE2:

                generateLevelTwo();

                break;

            case Prototipo.PHASE3:

                generateLevelThree();

                break;

            case Prototipo.PHASE4: 

                generateLevelFour();

            break;
        }

    }

    private void applyBackground(int level) {
        String region = (level == 3) ? "background_big" : "background";
        backgroundTex = atlas.findRegion(region);
        if (backgroundTex == null) {
            throw new IllegalStateException("Região não encontrada no atlas: " + region);
        }
    }

    public void generateLevelThree() {

        playerTwo = levelFactory.createPlayer(-6, 1, PlayerComponent.TWO);
        playerOne = levelFactory.createPlayer(6, 1, PlayerComponent.    ONE);

        levelFactory.createGround(0);

        levelFactory.createPlatform(-3, -4f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(3, -4f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(-3, -1f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(3, -1f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(-3, 2f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(3, 2f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(-3, 5f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(3, 5f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(-3, 8f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(3, 8f, LevelFactory.PLATFORM_NORMAL, false);

        levelFactory.createDoor(-3, 9.3f, 1);
        levelFactory.createDoor(3, 9.3f, 2);

        levelFactory.createBox(LevelFactory.BOX_NORMAL);

        renderingSystem.setZoom(false);

    }

    public void generateLevelFour() {

        playerOne = levelFactory.createPlayer(-12.0f, 5f, PlayerComponent.ONE);

        levelFactory.createPlatform(-12.0f, 1.8f, LevelFactory.PLATFORM_NORMAL, false);

        levelFactory.createPlatform(-9.3333f, 2.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(-6.6667f, 4.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(-10.6667f, 6.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(-6.6667f, 8.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(-2.6667f, 10.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(-6.0000f, 14.200f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(1.8333f, 12.2000f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(5.3333f, 9.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(9.3333f, 12.2000f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(5.3333f, 15.2000f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(12.0000f, 14.2000f, LevelFactory.PLATFORM_SMALL, false);

        levelFactory.createPlatform(16.0000f, 9.8667f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createDoor(16.0000f, 11.0667f, DoorComponent.ONE); // +0.6 em Y

        levelFactory.createPlatform(-9.3333f, 11.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPit(-9.3333f, 12.0667f, LevelFactory.PIT_SMALL); // +0.2 em Y

        playerTwo = levelFactory.createPlayer(-13.3f, -4.1f, PlayerComponent.TWO);

        levelFactory.createPlatform(-13.3333f, -4.5333f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(-16.0000f, -8.2000f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createPlatform(-12.0000f, -10.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(-6.6667f, -10.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(-4.0000f, -8.2000f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(0.6667f, -8.2000f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(4.0000f, -4.5333f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(8.0000f, -8.2000f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(10.6667f, -4.5333f, LevelFactory.PLATFORM_SMALL, false);

        levelFactory.createPlatform(14.6667f, -8.5333f, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createDoor(14.6667f, -7.3333f, DoorComponent.TWO); // +0.6 em Y

        levelFactory.createPlatform(-16.0000f, -13.5333f, LevelFactory.PLATFORM_SMALL, false);

        levelFactory.createPlatform(-1.6667f, -4.5333f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPit(-1.6667f, -4.3333f, LevelFactory.PIT_SMALL); // +0.2 em Y

        levelFactory.createPlatform(-9.3333f, -6.8667f, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPit(-9.3333f, -6.6667f, LevelFactory.PIT_SMALL); // +0.2 em Y

        levelFactory.createFloor(.5f, true, LevelFactory.FLOOR_BIG);
        levelFactory.createFloor(-18.05f, true, LevelFactory.FLOOR_BIG);
        levelFactory.createFloor(0, false, LevelFactory.FLOOR_BIG);
        levelFactory.createBox(LevelFactory.BOX_BIG);

        renderingSystem.setZoom(true);

    }

    public void generateLevelTwo() {

        playerOne = levelFactory.createPlayer(-9, -4, 1);
        playerTwo = levelFactory.createPlayer(10.2f, 0, 2);

        levelFactory.createBlock(-8.75f);
        levelFactory.createBlock(8.75f);

        levelFactory.createPlatform(7.8f, -5, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(10.2f, -3, LevelFactory.PLATFORM_SMALL, false);
        levelFactory.createPlatform(11.1f, -.8f, LevelFactory.PLATFORM_BIG, true);

        levelFactory.createPlatform(4, -3, LevelFactory.PLATFORM_BIG, false);
        levelFactory.createPlatform(-1, -1, LevelFactory.PLATFORM_BIG, false);
        levelFactory.createPlatform(-6, 1, LevelFactory.PLATFORM_BIG, false);

        levelFactory.createPlatform(-11.4f, 3, LevelFactory.PLATFORM_BIG, false);
        levelFactory.createDoor(-12.2f, 4.2f, DoorComponent.ONE);

        levelFactory.createPlatform(-11.9f, 6, LevelFactory.PLATFORM_NORMAL, false);
        levelFactory.createDoor(-12.2f, 7.2f, DoorComponent.TWO);

        levelFactory.createPit(0, -13.125f, LevelFactory.PIT_BIG);

        levelFactory.createBox(LevelFactory.BOX_NORMAL);

        renderingSystem.setZoom(false);

    }

    public void generateLevelOne(){
        levelFactory.createBox(LevelFactory.BOX_NORMAL);
        renderingSystem.setZoom(false);

        levelFactory.createGround(0);
        levelFactory.createTower(0, -3.2f);

        levelFactory.createDoor(-8.50f, -6f, DoorComponent.ONE);
        levelFactory.createDoor(8.50f, -6f, DoorComponent.TWO);

        this.playerOne = levelFactory.createPlayer(-1, 11, PlayerComponent.ONE);
        this.playerTwo = levelFactory.createPlayer(1, 11, PlayerComponent.TWO);


    }

}
