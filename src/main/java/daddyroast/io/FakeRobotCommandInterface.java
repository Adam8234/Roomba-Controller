package daddyroast.io;

import daddyroast.gui.GuiHandler;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class FakeRobotCommandInterface extends RobotCommandInterface {
    private FakeRobotConnection fakeRobotCommandInterface = new FakeRobotConnection();

    public FakeRobotCommandInterface(GuiHandler guiHandler) {
        super(guiHandler);
    }

    @Override
    public void scan() {
        super.scan();
        Observable.fromCallable(() ->
        {
            responseStream.onNext("90.0,10,10");
            Thread.sleep(1000);
            responseStream.onNext("D");
            return "";
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

    @Override
    public void moveForward(int distance) {
        super.moveForward(distance);
        Observable.fromCallable(() ->
        {
            for (int i = 0; i < distance; i++) {
                responseStream.onNext("1.0");
                Thread.sleep(100);
            }
            responseStream.onNext("D");
            return "";
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

    @Override
    public void rotateLeft(int angle) {
        super.rotateLeft(angle);
        Observable.fromCallable(() ->
        {
            for (int i = 0; i < angle; i++) {
                responseStream.onNext("1.0");
                Thread.sleep(50);
            }
            responseStream.onNext("D");
            return "";
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

    @Override
    public void rotateRight(int angle) {
        super.rotateRight(angle);
        Observable.fromCallable(() ->
        {
            for (int i = 0; i < angle; i++) {
                responseStream.onNext("-1.0");
                Thread.sleep(50);
            }
            responseStream.onNext("D");
            return "";
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

    @Override
    public IRobotConnection getRobotConnection() {
        return fakeRobotCommandInterface;
    }
}
