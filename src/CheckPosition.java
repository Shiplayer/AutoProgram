import java.awt.*;

public class CheckPosition {
    private static Robot robot;
    private static PointerInfo mouseInfo;
    public static void main(String[] args) throws AWTException, InterruptedException {
        new CheckPosition().run();
    }

    private void run() throws AWTException, InterruptedException {
        robot = new Robot();
        new Thread(() -> {
            while(true) {
                mouseInfo = MouseInfo.getPointerInfo();
                System.out.println(mouseInfo.getLocation().toString() + " " + robot.getPixelColor(mouseInfo.getLocation().x, mouseInfo.getLocation().y));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
