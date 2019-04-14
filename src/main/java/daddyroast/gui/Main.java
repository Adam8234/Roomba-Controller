package daddyroast.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;
import daddyroast.io.DetectedObject;
import daddyroast.io.FakeRobotCommandInterface;
import daddyroast.io.IRobotCommandInterface;
import daddyroast.io.IRobotConnection;
import daddyroast.io.tcp.TCPRobotCommandInterface;

import java.util.List;


public class Main extends SimpleApplication implements ActionListener {
    float heading = 0;
    int input = 10;
    Node roomba_node;
    IRobotCommandInterface robotCommandInterface;

    public Main() {
        super();
        robotCommandInterface = new FakeRobotCommandInterface();
    }

    public static void main(String[] args) {
        final Main app = new Main();
        app.start();
    }

    Material redMaterial, greenMaterial, blackMaterial, orangeMaterial;

    @Override
    public void simpleInitApp() {
        FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        fpp.addFilter(new CartoonEdgeFilter());
        viewPort.addProcessor(fpp);

        greenMaterial = new Material(assetManager, Materials.UNSHADED);
        greenMaterial.setColor("Color", ColorRGBA.Green);
        redMaterial = new Material(assetManager,  Materials.UNSHADED);
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
        inputManager.addMapping("Scan", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping("INCD", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("DECD", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("MF", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("MB", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("RR", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("RL", new KeyTrigger(KeyInput.KEY_A));

        inputManager.addListener(this, "Scan", "INCD", "DECD", "MF", "MB", "RR", "RL");
        inputManager.addListener(new ZoomActionListener(cam), "ZoomI", "ZoomO");
        cam.getRotation();

        addRobot();
    }

    public void scan() {
        List<DetectedObject> scan = robotCommandInterface.scan();
        for (DetectedObject detectedObject : scan) {
            System.out.println(detectedObject);
            float theta = (toRads((float) (detectedObject.angle)) + (-heading * FastMath.PI / 180.0f));
            Vector3f v = new Vector3f(FastMath.cos(theta), FastMath.sin(theta), 0).normalize().mult((float) detectedObject.distance + (float) detectedObject.width / 2f);
            Vector3f location = roomba_node.getChild("robot_sensors").getWorldTranslation().clone().add(v);
            Cylinder object = new Cylinder(100, 100, (float) (detectedObject.width / 2.0), 20f, true, false);
            Geometry objectG = new Geometry("object", object);
            objectG.setMaterial(redMaterial);
            objectG.move(location);
            rootNode.attachChild(objectG);
        }
    }

    public float toRads(float degree) {
        return (degree * FastMath.PI) / 180.0f;
    }

    public void addRobot() {
        roomba_node = new Node("roomba_node");

        Cylinder mesh = new Cylinder(100, 100, 34.0106f / 2, 10f, true, false);
        Sphere s = new Sphere(100, 100, 2f);
        Geometry sensorG = new Geometry("robot_sensors", s);
        sensorG.move(0f, 14f, 7f);
        Geometry bodyG = new Geometry("robot_body", mesh);

        sensorG.setMaterial(greenMaterial);
        bodyG.setMaterial(blackMaterial);
        roomba_node.attachChild(bodyG);
        roomba_node.attachChild(sensorG);

        rootNode.attachChild(roomba_node);
    }

    public void rotateRobot(float degree) {
        this.heading += degree;
        float theta = -degree * FastMath.PI / 180f;
        rootNode.getChild("roomba_node").rotate(0f, 0f, theta);
        Vector3f v = new Vector3f(FastMath.cos((FastMath.PI / 2) + toRads(-heading)), FastMath.sin((FastMath.PI / 2) + toRads(-heading)), 0);
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

    boolean scanning = false;
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("Scan") && isPressed) {
            scan();
        } else if(name.equals("INCD") && isPressed) {
            input += 5;
            System.out.println(input);
        } else if(name.equals("DECD") && isPressed) {
            input -= 5;
            System.out.println(input);
        } else if(name.equals("MF") && isPressed) {
            float dist = robotCommandInterface.moveForward(input);
            System.out.println(dist);
            moveRobot(dist);
        } else if(name.equals("MB") && isPressed) {
            float dist = robotCommandInterface.moveBackward(input);
            System.out.println(dist);
            moveRobot(dist);
        } else if(name.equals("RR") && isPressed) {
            float angle = robotCommandInterface.rotateRight(input);
            System.out.println(angle);
            rotateRobot(Math.abs(angle));
        } else if(name.equals("RL") && isPressed) {
            float angle = robotCommandInterface.rotateLeft(input);
            System.out.println(angle);
            rotateRobot(-Math.abs(angle));
        }
    }
}
