package daddyroast.io;

import daddyroast.*;
import daddyroast.gui.GuiHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * RobotCommandInterface takes responses from the asynchronous stream from the PublishSubject
 * and communicates with the UI with the GUI Handler
 *
 * @author adamcorp
 */
public abstract class RobotCommandInterface implements Observer<String> {
    protected PublishSubject<String> responseStream = PublishSubject.create();
    private GuiHandler guiHandler;
    private State state = State.DONE;

    public RobotCommandInterface(GuiHandler guiHandler) {
        this.guiHandler = guiHandler;
        responseStream.subscribe(this);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    /**
     * onNext is called when we receive events from the robot.
     * BE CAREFUL YOU ARE NOT GUARANTEED TO BE ON THE UI THREAD
     * You will need to implement your own way of communicating with the UI Thread
     */
    @Override
    public void onNext(String s) {
        if (s.charAt(0) == 'D') {
            state = State.DONE;
        }

        switch (state) {
            case DONE:
                break;
            case MOVING_FORWARD:
                try {
                    double distance = Double.parseDouble(s);
                    guiHandler.moveRobot(distance);
                } catch (Exception e) {
                    String location = s.substring(1);
                    switch (s.charAt(0)) {
                        case 'C':
                            switch (location) {
                                case "L":
                                    guiHandler.addObject(CliffType.LEFT.getObject());
                                    break;
                                case "FL":
                                    guiHandler.addObject(CliffType.FRONT_LEFT.getObject());
                                    break;
                                case "F":
                                    guiHandler.addObject(CliffType.FRONT.getObject());
                                    break;
                                case "FR":
                                    guiHandler.addObject(CliffType.FRONT_RIGHT.getObject());
                                    break;
                                case "R":
                                    guiHandler.addObject(CliffType.RIGHT.getObject());
                                    break;
                            }
                            break;
                        case 'O':
                            switch (location) {
                                case "L":
                                    guiHandler.addObject(BarrierType.LEFT.getObject());
                                    break;
                                case "FL":
                                    guiHandler.addObject(BarrierType.FRONT_LEFT.getObject());
                                    break;
                                case "FR":
                                    guiHandler.addObject(BarrierType.FRONT_RIGHT.getObject());
                                    break;
                                case "R":
                                    guiHandler.addObject(BarrierType.RIGHT.getObject());
                                    break;
                            }
                            break;
                        case 'B':
                            switch (location) {
                                case "L":
                                    guiHandler.addObject(BoulderType.LEFT.getObject());
                                    break;
                                case "F":
                                    guiHandler.addObject(BoulderType.FRONT.getObject());
                                    break;
                                case "R":
                                    guiHandler.addObject(BoulderType.RIGHT.getObject());
                                    break;
                            }
                            break;
                    }
                }
                break;
            case TURNING_RIGHT:
            case TURNING_LEFT:
                try {
                    double angle = -Double.parseDouble(s);
                    guiHandler.rotateRobot(angle);
                } catch (Exception e) {
                }
                break;
            case SCANNING:
                try {
                    DetectedObject detectedObject = DetectedObject.fromString(s);
                    guiHandler.addObject(detectedObject);
                } catch (Exception e) {
                }
                break;
        }
    }


    @Override
    public void onError(Throwable e) {
    }


    @Override
    public void onComplete() {

    }

    public void scan() {
        state = State.SCANNING;
        getRobotConnection().sendString("s;");
    }

    public void killRobot() {
        getRobotConnection().sendString("x;");
    }

    public void moveForward(int distance) {
        state = State.MOVING_FORWARD;
        getRobotConnection().sendString(String.format("f%d;", distance));
    }

   /* public void moveBackward(int distance) {
        getRobotConnection().sendString(String.format("b%d;", distance));
    }*/

    public void rotateLeft(int angle) {
        state = State.TURNING_LEFT;
        getRobotConnection().sendString(String.format("l%d;", angle));
    }

    public void rotateRight(int angle) {
        state = State.TURNING_RIGHT;
        getRobotConnection().sendString(String.format("r%d;", angle));
    }

    public State getState() {
        return state;
    }

    public void resetState() {
        state = State.DONE;
    }

    public abstract IRobotConnection getRobotConnection();
}
