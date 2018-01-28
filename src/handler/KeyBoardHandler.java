package handler;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyBoardHandler implements NativeKeyListener {
    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        //System.out.println("Typed " + nativeKeyEvent.getKeyChar() + " " + nativeKeyEvent.getKeyCode());
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        //System.out.println("Pressed " + nativeKeyEvent.getKeyChar() + " " + nativeKeyEvent.getKeyCode());
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        
    }

    public static void main(String[] args) throws NativeHookException {
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new KeyBoardHandler());
    }
}
