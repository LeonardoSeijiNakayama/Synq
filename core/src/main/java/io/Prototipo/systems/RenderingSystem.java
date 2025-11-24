package io.Prototipo.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.Prototipo.components.PlayerComponent;
import io.Prototipo.components.TextureComponent;
import io.Prototipo.components.TransformComponent;

public class RenderingSystem extends SortedIteratingSystem {

    private boolean shouldRender = true;

    static final float PPM = 32.0f; // sets the amount of pixels each metre of box2d objects contains

    // this gets the height and width of our camera frustrum based off the width and
    // height of the screen and our pixel per meter ratio
    public static final float FRUSTUM_WIDTH = Gdx.graphics.getWidth() / PPM;
    public static final float FRUSTUM_HEIGHT = Gdx.graphics.getHeight() / PPM;
    public static final float FRUSTUM_HEIGHT_BIG = FRUSTUM_HEIGHT + (FRUSTUM_HEIGHT / 3);
    public static final float FRUSTUM_WIDTH_BIG = FRUSTUM_WIDTH + (FRUSTUM_WIDTH / 3);
    public static final float PIXELS_TO_METRES = 1.0f / PPM; // get the ratio for converting pixels to metres
    public static final float WORLD_WIDTH = 29f;
    public static final float WORLD_HEIGHT = 29f;


    // static method to get screen width in metres
    private static Vector2 meterDimensions = new Vector2();
    private static Vector2 pixelDimensions = new Vector2();

    public static Vector2 getScreenSizeInMeters() {
        meterDimensions.set(Gdx.graphics.getWidth() * PIXELS_TO_METRES,
                Gdx.graphics.getHeight() * PIXELS_TO_METRES);
        return meterDimensions;
    }

    // static method to get screen size in pixels
    public static Vector2 getScreenSizeInPixesl() {
        pixelDimensions.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return pixelDimensions;
    }

    // convenience method to convert pixels to meters
    public static float PixelsToMeters(float pixelValue) {
        return pixelValue * PIXELS_TO_METRES;
    }

    private SpriteBatch batch; 
    private Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of
                                       // each other
    private Comparator<Entity> comparator; // a comparator to sort images based on the z position of the
                                           // transfromComponent
    private OrthographicCamera cam; // a reference to our camera
    private Viewport viewport;
    // component mappers to get components from entities
    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<PlayerComponent> playerM;

    public RenderingSystem(SpriteBatch batch) {
        // gets all entities with a TransofmComponent and TextureComponent
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());

        this.comparator = new ZComparator();
        // creates out componentMappers
        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        playerM = ComponentMapper.getFor(PlayerComponent.class);

        // create the array for sorting entities
        renderQueue = new Array<Entity>();

        this.batch = batch; // set our batch to the one supplied in constructor

        // set up the camera to match our screen size
        cam = new OrthographicCamera();
        cam.position.set(0, 0, 0);

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
        viewport.apply();
        cam.update();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderQueue.sort(comparator);

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();

        if (shouldRender) {
            batch.begin();

            for (Entity entity : renderQueue) {
                TextureComponent tex = textureM.get(entity);
                TransformComponent t = transformM.get(entity);
                PlayerComponent p = playerM.get(entity);

                if (tex.region == null || t.isHidden) {
                    continue;
                }

                float width = tex.region.getRegionWidth();
                float height = tex.region.getRegionHeight();

                float originX = width / 2f;
                float originY = height / 2f;

                float sx = PixelsToMeters(t.scale.x);
                float sy = PixelsToMeters(t.scale.y);



                if (p!=null && !p.left) {
                    sx = -Math.abs(sx);
                } else {
                    sx = Math.abs(sx);
                }

                batch.draw(
                        tex.region,
                        t.position.x - originX + tex.offsetX,
                        t.position.y - originY + tex.offsetY,
                        originX, originY,
                        width, height,
                        sx, sy,
                        t.rotation);
            }
            batch.end();
        }
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    // convenience method to get camera
    public OrthographicCamera getCamera() {
        return cam;
    }

    public Viewport getViewport(){
        return viewport;
    }

    public void setZoom(boolean big) {

        if (big) {

            viewport.setWorldSize(WORLD_WIDTH * 1.331896f, WORLD_HEIGHT * 1.331896f);

        } else {

            viewport.setWorldSize(WORLD_WIDTH, WORLD_HEIGHT);

        }

        viewport.apply();
        cam.position.set(0f, 0f, 0f);
        cam.update();

    }
}
