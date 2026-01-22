package io.Prototipo.controller;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;

public class KeyboardController implements InputProcessor, ControllerListener {

    public boolean right, up, left, down, d, w, a, s, esc, shift, r;
    public boolean isM1Down, isM2Down, isM3down, isDragged;
    public Vector2 mouseLocation = new Vector2();

    private Controller controller;
    private ControllerMapping mapping;

    public KeyboardController() {
        // pega o primeiro controle (se existir)
        if (Controllers.getControllers().size > 0) {
            controller = Controllers.getControllers().first();
            mapping = controller.getMapping();
        }
        Controllers.addListener(this);
    }

    public void reset() {
        right = up = left = down = d = w = a = s = esc = r =
            isM1Down = isM2Down = isM3down = isDragged = shift = false;
    }

    // ---------------- TECLADO ----------------

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;

        switch (keycode) {
            case Keys.LEFT:  left = true;  keyProcessed = true; break;
            case Keys.RIGHT: right = true; keyProcessed = true; break;
            case Keys.UP:    up = true;    keyProcessed = true; break;
            case Keys.DOWN:  down = true;  keyProcessed = true; break;

            case Keys.A: a = true; keyProcessed = true; break;
            case Keys.D: d = true; keyProcessed = true; break;
            case Keys.W: w = true; keyProcessed = true; break;
            case Keys.S: s = true; keyProcessed = true; break;
            case Keys.R: r = true; keyProcessed = true; break;

            case Keys.ESCAPE: esc = true; keyProcessed = true; break;

            case Keys.SHIFT_LEFT:
            case Keys.SHIFT_RIGHT:
                shift = true;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyProcessed = false;

        switch (keycode) {
            case Keys.LEFT:  left = false;  keyProcessed = true; break;
            case Keys.RIGHT: right = false; keyProcessed = true; break;
            case Keys.UP:    up = false;    keyProcessed = true; break;
            case Keys.DOWN:  down = false;  keyProcessed = true; break;

            case Keys.A: a = false; keyProcessed = true; break;
            case Keys.D: d = false; keyProcessed = true; break;
            case Keys.W: w = false; keyProcessed = true; break;
            case Keys.S: s = false; keyProcessed = true; break;
            case Keys.R: r = false; keyProcessed = true; break;

            case Keys.ESCAPE: esc = false; keyProcessed = true; break;

            case Keys.SHIFT_LEFT:
            case Keys.SHIFT_RIGHT:
                shift = false;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }

    @Override public boolean keyTyped(char character) { return false; }

    // ---------------- MOUSE ----------------

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) isM1Down = true;
        else if (button == 1) isM2Down = true;
        else if (button == 2) isM3down = true;

        mouseLocation.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;

        if (button == 0) isM1Down = false;
        else if (button == 1) isM2Down = false;
        else if (button == 2) isM3down = false;

        mouseLocation.set(screenX, screenY);
        return false;
    }

    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        mouseLocation.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseLocation.set(screenX, screenY);
        return false;
    }

    @Override public boolean scrolled(float amountX, float amountY) { return false; }

    // ---------------- CONTROLE (APENAS BOTÕES) ----------------

    @Override
    public void connected(Controller controller) {
        // se ainda não tinha controle, usa esse
        if (this.controller == null) {
            this.controller = controller;
            this.mapping = controller.getMapping();
        }
    }

    @Override
    public void disconnected(Controller controller) {
        if (this.controller == controller) {
            this.controller = null;
            this.mapping = null;
            reset();
        }
    }

    @Override
    public boolean buttonDown(Controller c, int buttonCode) {
        if (c != this.controller || mapping == null) return false;

        // A/B/X -> "mouse buttons" (você pode remapear como quiser)
        if (buttonCode == mapping.buttonA) isM1Down = true;
        if (buttonCode == mapping.buttonB) isM2Down = true;
        if (buttonCode == mapping.buttonX) isM3down = true;

        // Start -> esc
        if (buttonCode == mapping.buttonStart) esc = true;

        // LB/RB (ou L1/R1) -> shift (correr)
        if (buttonCode == mapping.buttonL1 || buttonCode == mapping.buttonR1) shift = true;

        // D-Pad como botões (se o seu controle expõe assim)
        if (buttonCode == mapping.buttonDpadUp)    up = true;
        if (buttonCode == mapping.buttonA) up = true;
        if (buttonCode == mapping.buttonDpadDown)  down = true;
        if (buttonCode == mapping.buttonDpadLeft)  left = true;
        if (buttonCode == mapping.buttonDpadRight) right = true;
        if (buttonCode == mapping.buttonL1) r = true;

        return false;
    }

    @Override
    public boolean buttonUp(Controller c, int buttonCode) {
        if (c != this.controller || mapping == null) return false;

        if (buttonCode == mapping.buttonA) isM1Down = false;
        if (buttonCode == mapping.buttonB) isM2Down = false;
        if (buttonCode == mapping.buttonX) isM3down = false;

        if (buttonCode == mapping.buttonStart) esc = false;

        if (buttonCode == mapping.buttonL1 || buttonCode == mapping.buttonR1) shift = false;

        if (buttonCode == mapping.buttonDpadUp)    up = false;
        if (buttonCode == mapping.buttonA) up = false;
        if (buttonCode == mapping.buttonDpadDown)  down = false;
        if (buttonCode == mapping.buttonDpadLeft)  left = false;
        if (buttonCode == mapping.buttonDpadRight) right = false;
        if (buttonCode == mapping.buttonL1) r = false;

        return false;
    }

    // Os outros callbacks não são necessários pra botões
    @Override public boolean axisMoved(Controller controller, int axisCode, float value) { return false; }

    public void dispose() {
        Controllers.removeListener(this);
    }
}
