package daddyroast.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
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
import daddyroast.State;
import daddyroast.DetectedObject;
import daddyroast.io.RobotCommandInterface;
import daddyroast.io.tcp.TCPRobotCommandInterface;

import java.util.LinkedList;
import java.util.List;

/**
 * Graphics
 * @author adamcorp
 */
public class UIApplication extends SimpleApplication implements ActionListener, GuiHandler {
    //Asynchronous Queues to make the main thread happy
    private LinkedList<Double> moveQueue = new LinkedList<>();
    private LinkedList<Double> turnQueue = new LinkedList<>();
    private LinkedList<DetectedObject> detectedObjectQueue = new LinkedList<>();

    private Node roomba_node;
    private float heading = 0;
    private int input = 10;
    private boolean lock;
    private RobotCommandInterface robotCommandInterface;


    private BitmapText statusText;
    private BitmapText inputText;

    public UIApplication() {
        super();
        robotCommandInterface = new TCPRobotCommandInterface(this);
    }

    private void clearScanArea() {
        List<Spatial> children = rootNode.getChildren();
        for (Spatial child : children) {
            if (child != null && "object".equals(child.getName())) {
                Vector3f post = child.getWorldTranslation().clone();
                double distance = post.distance(rootNode.getChild("robot_sensors").getWorldTranslation());
                Vector3f relative = post.subtract(rootNode.getChild("robot_sensors").getWorldTranslation()).normalize();
                Vector3f rotateRelative = new Vector3f();
                //rotation matrix because this doesn't have it..
                rotateRelative.setX(relative.x * FastMath.cos(toRads(heading)) - relative.y * FastMath.sin(toRads(heading)));
                rotateRelative.setY(relative.x * FastMath.sin(toRads(heading)) + relative.y * FastMath.cos(toRads(heading)));
                //If relative y position is less than 0 we are on angles 180 to 360 which is BAD, we don't want remove objects behind us
                if (rotateRelative.y < 0) {
                    continue;
                }
                double between = Math.toDegrees(Vector3f.UNIT_X.clone().normalize().angleBetween(rotateRelative));
                if (distance <= 60 && between >= 10 && between <= 170) {
                    child.removeFromParent();
                }
            }
        }
    }

    Material redMaterial, greenMaterial, blackMaterial, orangeMaterial, brownMaterial;

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(new CartoonEdgeFilter());
        viewPort.addProcessor(fpp);

        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        statusText = new BitmapText(font, false);
        inputText = new BitmapText(font, false);
        statusText.setSize(guiFont.getCharSet().getRenderedSize());
        inputText.setSize(guiFont.getCharSet().getRenderedSize());
        guiNode.attachChild(statusText);
        guiNode.attachChild(inputText);

        BitmapText start = new BitmapText(font, false);
        rootNode.attachChild(start);
        start.setText("START");
        start.setSize(5);
        start.setLocalTranslation(0f - start.getLineWidth() * 0.5f, 0f, 20f);

        greenMaterial = new Material(assetManager, Materials.UNSHADED);
        greenMaterial.setColor("Color", ColorRGBA.Green);
        redMaterial = new Material(assetManager, Materials.UNSHADED);
        redMaterial.setColor("Color", ColorRGBA.Red);
        blackMaterial = new Material(assetManager, Materials.UNSHADED);
        blackMaterial.setColor("Color", ColorRGBA.White);
        orangeMaterial = new Material(assetManager, Materials.UNSHADED);
        orangeMaterial.setColor("Color", ColorRGBA.Orange);
        brownMaterial = new Material(assetManager, Materials.UNSHADED);
        brownMaterial.setColor("Color", ColorRGBA.Brown);

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
        inputManager.addMapping("RESET_STATE", new KeyTrigger(KeyInput.KEY_Y));
        //inputManager.addMapping("KILL ROBOT", new KeyTrigger(KeyInput.KEY_T));

        inputManager.addListener(this, "Scan", "INCD", "DECD", "MF", "MB", "RR", "RL", "RESET_STATE", "KILL_ROBOT");
        inputManager.addListener(new ZoomActionListener(cam), "ZoomI", "ZoomO");
        cam.getRotation();

        addRobot();
    }

    public float toRads(float degree) {
        return (degree * FastMath.PI) / 180.0f;
    }

    public void addRobot() {
        roomba_node = new Node("roomba_node");

        Cylinder mesh = new Cylinder(100, 100, 34.0106f / 2, 10f, true, false);
        Sphere s = new Sphere(100, 100, 2f);
        Geometry sensorG = new Geometry("robot_sensors", s);
        sensorG.move(0f, 17f, 7f);
        Geometry bodyG = new Geometry("robot_body", mesh);

        sensorG.setMaterial(greenMaterial);
        bodyG.setMaterial(blackMaterial);
        roomba_node.attachChild(bodyG);
        roomba_node.attachChild(sensorG);

        Line line = new Line(new Vector3f(0f, 17f, 0f), new Vector3f(0f, 17f + input, 0f));
        line.setLineWidth(5);
        Geometry lineG = new Geometry("moveLine", line);
        Material lineM = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        lineM.setColor("Color", ColorRGBA.Yellow);
        lineG.setMaterial(lineM);
        roomba_node.attachChild(lineG);

        rootNode.attachChild(roomba_node);
    }

    public Spatial getRoombaNode() {
        return roomba_node;
    }

    @Override
    public void simpleUpdate(float tpf) {
        lock = robotCommandInterface.getState() != State.DONE;
        moveRobotUI();
        rotateRobotUI();
        addObjectsUI();

        statusText.setText(robotCommandInterface.getState().toString());
        statusText.setLocalTranslation(cam.getWidth() * 0.5f - statusText.getLineWidth() * 0.5f, statusText.getLineHeight() + 5f, 0f);
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    boolean scanning = false;

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("RESET_STATE")) {
            robotCommandInterface.resetState();
        } else if(name.equals("KILL_ROBOT")){
            robotCommandInterface.killRobot();
        } else if (name.equals("INCD") && isPressed) {
            input += 5;
        } else if (name.equals("DECD") && isPressed) {
            input -= 5;
        }
        inputText.setText("Input: " + input);
        inputText.setLocalTranslation((float) (cam.getWidth() * 0.95 - inputText.getLineWidth() * .5), inputText.getLineHeight() + 5, 0);

        roomba_node.getChild("moveLine").removeFromParent();
        Line line = new Line(new Vector3f(0f, 17f, 0f), new Vector3f(0f, 17f + input, 0f));
        line.setLineWidth(5);
        Geometry lineG = new Geometry("moveLine", line);
        Material lineM = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        lineM.setColor("Color", ColorRGBA.Yellow);
        lineG.setMaterial(lineM);
        roomba_node.attachChild(lineG);

        if (lock) {
            return;
        }
        if (name.equals("Scan") && isPressed) {
            clearScanArea();
            robotCommandInterface.scan();
        } else if (name.equals("MF") && isPressed) {
            robotCommandInterface.moveForward(input);
        } else if (name.equals("MB") && isPressed) {
            //robotCommandInterface.moveBackward(input);
        } else if (name.equals("RR") && isPressed) {
            robotCommandInterface.rotateRight(input);
        } else if (name.equals("RL") && isPressed) {
            robotCommandInterface.rotateLeft(input);
        }

    }

    @Override
    public void addObject(DetectedObject detectedObject) {
        detectedObjectQueue.add(detectedObject);

    }

    @Override
    public void moveRobot(double distance) {
        moveQueue.add(distance);
    }


    private void addObjectsUI() {
        while (!detectedObjectQueue.isEmpty()) {
            DetectedObject detectedObject = detectedObjectQueue.remove();
            Node objectNode = new Node(detectedObject.name);

            float theta = (toRads((float) (detectedObject.angle)) + (-heading * FastMath.PI / 180.0f));
            Vector3f v = new Vector3f(FastMath.cos(theta), FastMath.sin(theta), 0).normalize().mult((float) detectedObject.distance + (float) detectedObject.width / 2f);
            Vector3f location = roomba_node.getChild("robot_sensors").getWorldTranslation().clone().add(v);
            Cylinder object = new Cylinder(100, 100, (float) (detectedObject.width / 2.0), 20f, true, false);
            Geometry objectG = new Geometry("object_object", object);

            Material objectMaterial = new Material(assetManager, Materials.UNSHADED);
            if (detectedObject.getName().equals("cliff")) {
                objectMaterial.setColor("Color", ColorRGBA.Black);
            } else if (detectedObject.getName().equals("boulder")) {
                objectMaterial.setColor("Color", ColorRGBA.DarkGray);
            } else if (detectedObject.getName().equals("barrier")) {
                objectMaterial.setColor("Color", ColorRGBA.White);
            } else {
                if (detectedObject.width < 8) {
                    objectMaterial.setColor("Color", ColorRGBA.Green);
                } else {
                    objectMaterial.setColor("Color", ColorRGBA.Red);
                }
            }
            BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
            BitmapText objectText = new BitmapText(font, false);
            rootNode.attachChild(objectText);
            objectText.setText(Double.toString(detectedObject.width));
            objectText.setSize(5);
            objectText.setLocalTranslation(-objectText.getLineWidth() * 0.5f, (float) detectedObject.width * .25f + objectText.getLineHeight() * 0.25f, 20f);
            objectG.setMaterial(objectMaterial);

            objectNode.move(location);

            objectNode.attachChild(objectG);
            objectNode.attachChild(objectText);

            rootNode.attachChild(objectNode);
        }
    }

    private void moveRobotUI() {
        while (!moveQueue.isEmpty()) {
            double distance = moveQueue.remove();
            float theta = (FastMath.PI / 2) + -this.heading * FastMath.PI / 180f;
            Vector3f previous = roomba_node.getWorldTranslation().clone().setZ(0.5f);
            Vector3f v = new Vector3f(FastMath.cos(theta), FastMath.sin(theta), 0).normalize().mult((float) distance);
            cam.setLocation(cam.getLocation().add(v));
            Line line = new Line(previous, previous.add(v));
            line.setLineWidth(5);
            Geometry lineG = new Geometry("trail", line);
            lineG.move(0, 0, 20f);
            if (distance < 0) {
                lineG.setMaterial(brownMaterial);
            } else {
                lineG.setMaterial(orangeMaterial);
            }
            roomba_node.move(v);
            rootNode.attachChild(lineG);
        }
    }

    private void rotateRobotUI() {
        while (!turnQueue.isEmpty()) {
            double degrees = turnQueue.remove();
            this.heading += degrees;
            float theta = -(float) degrees * FastMath.PI / 180f;
            rootNode.getChild("roomba_node").rotate(0f, 0f, theta);
            Vector3f v = new Vector3f(FastMath.cos((FastMath.PI / 2) + toRads(-heading)), FastMath.sin((FastMath.PI / 2) + toRads(-heading)), 0);
            cam.lookAtDirection(new Vector3f(0, 0, -1), v);
        }
    }

    @Override
    public void rotateRobot(double degrees) {
        turnQueue.add(degrees);
    }
}
