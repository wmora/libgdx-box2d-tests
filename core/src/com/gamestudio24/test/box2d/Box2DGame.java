package com.gamestudio24.test.box2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DGame extends ApplicationAdapter {

    World world;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;

    @Override
    public void create() {

        /*
            When setting up Box2D the first thing we need is a world.
            The world object is basically what holds all your physics objects/bodies and simulates
            the reactions between them. It does not however render the objects for you;
            for that you will use libgdx graphics functions.
            The first argument we supply is a 2D vector containing the gravity: 0 to indicate
            no gravity in the horizontal direction, and -10 is a downwards force like in real life
            (assuming your y axis points upwards). These values can be anything you like, but
            remember to stick to a constant scale. In Box2D 1 unit = 1 metre.
         */
        world = new World(new Vector2(0, -10), true);

        /*
            libgdx does come with a Box2D debug renderer which is extremely handy for debugging
            your physics simulations, or even for testing your game-play before writing any rendering code.
         */
        debugRenderer = new Box2DDebugRenderer();

        /*
            Create an orthographic camera
         */
        camera = new OrthographicCamera();
        camera.viewportHeight = 320;
        camera.viewportWidth = 480;
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);
        camera.update();

        /*
            In Box2D our objects are called bodies, and each body is made up of one of more
            fixtures, which have a fixed position and orientation within the body. Our fixtures can
            be any shape you can imagine or you can combine a variety of different shaped fixtures
            to make the shape you want.
         */

        /*
            Dynamic bodies are objects which move around and are affected by forces and other
            dynamic, kinematic and static objects. Dynamic bodies are suitable for any object which
            needs to move and be affected by forces.
         */

        // First we create a body definition
        BodyDef bodyDef = new BodyDef();

        // We set our body to dynamic
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Set our body's starting position in the world
        bodyDef.position.set(100, 300);

        // Create our body in the world using the body definition
        Body body = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(6f);

        /*
            A fixture has a shape, density, friction and restitution attached to it.
            Shape is obvious.
            Density is the mass per square metre: a bowling ball is very dense, yet a balloon isn’t
                very dense at all as it is mainly filled with air.
            Friction is the amount of opposing force when the object rubs/slides along something: a
                block of ice would have a very low friction but a rubber ball would have a high
                friction.
            Restitution is how bouncy something is: a rock would have a very low restitution but
                a basketball would have a fairly high restitution. A body with a restitution of 0
                will come to a halt as soon as it hits the ground, whereas a body with a
                restitution of 1 would bounce to the same height forever.
         */

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

        /*
            Static bodies are objects which do not move and are not affected by forces. Dynamic
            bodies are affected by static bodies. Static bodies are perfect for ground, walls, and
            any object which does not need to move. Static bodies require less computing power.
         */

        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();

        // Set its world position
        groundBodyDef.position.set(new Vector2(0, 10));

        // Create a body from the definition and add it to the world
        Body groundBody = world.createBody(groundBodyDef);

        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();

        // Set the polygon shape as a box which is twice the size of the viewport and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(camera.viewportWidth, 10.0f);

        // Create a fixture from the polygon and add it to the body
        groundBody.createFixture(groundBox, 0.0f);

        // Dispose shape!
        groundBox.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, camera.combined);

        /*
            To update our simulation we need to tell our world to step. Stepping basically updates
            the world objects through time. The best place to call our step function is at the end
            of our render() loop. In a perfect world everyone\'s frame rate is the same.
         */
        world.step(1 / 45f, 6, 2);
    }
}
