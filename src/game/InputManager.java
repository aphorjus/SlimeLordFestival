package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class InputManager {
    private class KeyState {
        private int key;
        private boolean pressedLastFrame = false;
        private boolean pressed = false;

        public KeyState(int k) {
            key = k;
        }

        public int getKey() { return key; }

        public void update(Input input) {
            pressedLastFrame = pressed;

            pressed = input.isKeyDown(key);
        }

        public boolean justPressed() {
            return (pressedLastFrame && !pressed);
        }

        public boolean isPressed() {
            return pressed;
        }
    }

    GameContainer container;
    KeyState[] keyStates;

    public InputManager(GameContainer gc, int[] keys) {
        container = gc;
        keyStates = new KeyState[keys.length];

        for (int i = 0; i < keys.length; i++) {
            keyStates[i] = new KeyState(keys[i]);
        }
    }

    public boolean justPressed(int key) {
        for (int i = 0; i < keyStates.length; i++) {
            if (keyStates[i].getKey() == key) {
                return keyStates[i].justPressed();
            }
        }

        return false;
    }

    public boolean isPressed(int key) {
        for (int i = 0; i < keyStates.length; i++) {
            if (keyStates[i].getKey() == key) {
                return keyStates[i].isPressed();
            }
        }

        return false;
    }

    public void update() {
        Input input = container.getInput();

        for (int i = 0; i < keyStates.length; i++) {
            keyStates[i].update(input);
        }
    }
}
