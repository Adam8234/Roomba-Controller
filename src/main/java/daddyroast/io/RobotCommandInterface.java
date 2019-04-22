package daddyroast.io;

import daddyroast.State;
import daddyroast.gui.GuiHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

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

    @Override
    public void onNext(String s) {
        if (s.equals("D")) {
            state = State.DONE;
        }
        switch (state) {
            case DONE:
                break;
            case MOVING_FORWARD:
                double distance = Double.parseDouble(s);
                guiHandler.moveRobot(distance);
                break;
            case TURNING_RIGHT:
            case TURNING_LEFT:
                double angle = Double.parseDouble(s);
                guiHandler.rotateRobot(angle);
                break;
            case SCANNING:
                try {
                    DetectedObject detectedObject = DetectedObject.fromString(s);
                    guiHandler.addObject(detectedObject);
                } catch (Exception e) {
                    e.printStackTrace();
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
        getRobotConnection().sendString("s;");
        state = State.SCANNING;
    }

    public void moveForward(int distance) {
        getRobotConnection().sendString(String.format("f%d;", distance));
        state = State.MOVING_FORWARD;
    }

   /* public void moveBackward(int distance) {
        getRobotConnection().sendString(String.format("b%d;", distance));
    }*/

    public void rotateLeft(int angle) {
        getRobotConnection().sendString(String.format("l%d;", angle));
        state = State.TURNING_LEFT;
    }

    public void rotateRight(int angle) {
        getRobotConnection().sendString(String.format("r%d;", angle));
        state = State.TURNING_RIGHT;
    }

    public State getState() {
        return state;
    }

    public abstract IRobotConnection getRobotConnection();
}
