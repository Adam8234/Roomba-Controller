package daddyroast.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;
import daddyroast.io.IRobotConnection;
import daddyroast.io.fake.FakeRobotConnection;


public class Main extends SimpleApplication {
    IRobotConnection robotConnection;
    Geometry robotMesh;
    Quaternion robotDirection = new Quaternion();
    float heading = 0;

    Node roomba_node;

    public Main() {
        super();
        robotConnection = new FakeRobotConnection();
        robotConnection.init();
    }

    public static void main(String[] args) {
        final Main app = new Main();
        app.start();
    }

    Material redMaterial, greenMaterial, blackMaterial, orangeMaterial;

    @Override
    public void simpleInitApp() {
        greenMaterial = new Material(assetManager, Materials.UNSHADED);
        greenMaterial.setColor("Color", ColorRGBA.Green);
        redMaterial = new Material(assetManager, Materials.UNSHADED);
        redMaterial.setColor("Color", ColorRGBA.Red);
        blackMaterial = new Material(assetManager, Materials.UNSHADED);
        blackMaterial.setColor("Color", ColorRGBA.Black);
        orangeMaterial = new Material(assetManager, Materials.UNSHADED);
        orangeMaterial.setColor("Color", ColorRGBA.Orange);

        flyCam.setEnabled(false);
        viewPort.setBackgroundColor(ColorRGBA.Gray);

        cam.setLocation(cam.getLocation().add(new Vector3f(0, 0, 200)));

        inputManager.addMapping("ZoomI", new KeyTrigger(KeyInput.KEY_EQUALS));
        inputManager.addMapping("ZoomO", new KeyTrigger(KeyInput.KEY_MINUS));
        inputManager.addListener(new ZoomActionListener(cam), "ZoomI", "ZoomO");
        cam.getRotation();

        addRobot();
    }

    public void addRobot() {
        roomba_node = new Node("roomba_node");

        Cylinder mesh = new Cylinder(100, 100, 34.0106f / 2, 10f, true, false);
        Sphere s = new Sphere(100, 100, 2f);
        Geometry sensorG = new Geometry("robot_sensors", s);
        sensorG.move(0f, 10f, 7f);
        Geometry bodyG = new Geometry("robot_body", mesh);

        sensorG.setMaterial(greenMaterial);
        bodyG.setMaterial(blackMaterial);
        roomba_node.attachChild(bodyG);
        roomba_node.attachChild(sensorG);

        rootNode.attachChild(roomba_node);
        moveRobot(30);
        rotateRobot(45);
        moveRobot(20);
    }

    public void rotateRobot(float degree) {
        this.heading += degree;
        float theta = -degree * FastMath.PI / 180f;
        rootNode.getChild("roomba_node").rotate(0f, 0f, theta);
        Vector3f v = new Vector3f(FastMath.cos((FastMath.PI / 2) + theta), FastMath.sin((FastMath.PI / 2) + theta), 0);
        cam.lookAtDirection(new Vector3f(0, 0, -1), v);
    }

    public void moveRobot(float distance) {
        float theta = (FastMath.PI / 2) + -this.heading * FastMath.PI / 180f;
        Vector3f previous = roomba_node.getWorldTranslation().clone().setZ(0.5f);
        Vector3f v = new Vector3f(FastMath.cos(theta), FastMath.sin(theta), 0).normalize().mult(distance);
        cam.setLocation(cam.getLocation().add(v));
        Line line = new Line(previous, previous.add(v));
        line.setLineWidth(5);
        Geometry lineG = new Geometry("trail", line);
        lineG.setMaterial(orangeMaterial);

        roomba_node.move(v);
        rootNode.attachChild(lineG);
    }

    public Spatial getRoombaNode() {
        return roomba_node;
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
