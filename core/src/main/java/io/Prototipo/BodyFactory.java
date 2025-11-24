package io.Prototipo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BodyFactory {

    private static World world; 

    private static BodyFactory thisInstance;

    public static final int STEEL = 0,  WOOD = 1, RUBBER = 2, STONE =  3;

    private final float DEGTORAD = 0.00174533f;

    private BodyFactory(World world){ 
        BodyFactory.world = world;
    }

    public static BodyFactory getInstance(World world){
        if(thisInstance == null){
            thisInstance = new BodyFactory(world);
        }

        return thisInstance;
    }

    static public FixtureDef makeFixture(int material, Shape shape){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch (material) {
            case STEEL:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.3f;
                fixtureDef.restitution = 0.1f;
                break;

            case WOOD: 
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.7f;
                fixtureDef.restitution = 0.3f;
                break;

            case RUBBER:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 1f;
                break;

            case STONE:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 0.01f;
                break;
        
            default:
                fixtureDef.density = 7f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.3f;
                break;
        }
        return fixtureDef;
    }

    public Body makeCirclePolyBody(float posx, float posy, float diameter, int material, BodyType bodyType, boolean fixedRotation){
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(diameter/2);
        boxBody.createFixture(makeFixture(material, circleShape));
        circleShape.dispose();
        return boxBody;
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType){
        return makeCirclePolyBody(posx, posy, radius, material, bodyType, false);
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, int material){
        return makeCirclePolyBody(posx, posy, radius, material, BodyType.DynamicBody, false);
    }

    public Body makeBoxPolyBody(float posx, float posy, float widht, float height, int material, BodyType bodyType, boolean fixedRotation){
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(widht/2, height/2);
        boxBody.createFixture(makeFixture(material, poly));
        poly.dispose();

        return boxBody;
    }

    public Body makeBoxPolyBody(float posx, float posy, float widht, float height, int material, BodyType bodyType){
        return makeBoxPolyBody(posx, posy, widht, height, material, bodyType, false);
    }

    public Body makeBoxPolyBody(float posx, float posy, float widht, float height, int material){
        return makeBoxPolyBody(posx, posy, widht, height, material, BodyType.DynamicBody, false);
    }

    public Body makePolygonShapeBody(Vector2[] vertices, float posx, float posy, int material, BodyType bodyType){
        BodyDef polygonBodyDef = new BodyDef();
        polygonBodyDef.type = bodyType;
        polygonBodyDef.position.x = posx;
        polygonBodyDef.position.y = posy;
        Body body = world.createBody(polygonBodyDef);

        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices);
        body.createFixture(makeFixture(material, polygon));
        polygon.dispose();

        return body;
    }

    public void makeConeSensor(Body body, float size){
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape polygon = new PolygonShape();

        float radius = size;
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0, 0);
        for(int i = 0; i <6; i++){
            float angle = (float) (i/6.0 * 145 * DEGTORAD);
            vertices[i-1] = new Vector2(radius * ((float)Math.cos(angle)), radius * ((float)Math.sin(angle)));
        }
        polygon.set(vertices);
        fixtureDef.shape = polygon;
        body.createFixture(fixtureDef);
        polygon.dispose();
    }

    public void makeAllFixturesSensor(Body body){
        for(Fixture fix : body.getFixtureList()){
            fix.setSensor(true);
        }
    }
}
