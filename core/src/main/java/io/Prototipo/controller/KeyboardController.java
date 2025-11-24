package io.Prototipo.controller;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;


public class KeyboardController implements InputProcessor {

    public boolean right, up, left, down, d, w, a, s, esc, shift;
    public boolean isM1Down, isM2Down, isM3down, isDragged;
    public Vector2 mouseLocation = new Vector2();

    public KeyboardController() {

    }

    public void reset(){
        right = up = left = down = d = w = a = s  = esc= isM1Down = isM2Down = isM3down = isDragged = shift = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;

        switch (keycode) {
            case Keys.LEFT:
                left = true;
                keyProcessed = true;
                break;
            case Keys.RIGHT:
                right = true;
                keyProcessed = true;
                break;
            case Keys.UP:
                up = true;
                keyProcessed = true;
                break;
            case Keys.DOWN:
                down = true;
                keyProcessed = true;
                break;
            case Keys.A:
                a = true;
                keyProcessed = true;
                break;
            case Keys.D:
                d = true;
                keyProcessed = true;
                break;
            case Keys.W:
                w = true;
                keyProcessed = true;
                break;
            case Keys.S:
                s = true;
                keyProcessed = true;
                break;
                case Keys.ESCAPE:
                esc = true;
                keyProcessed = true;
                break;
            case Keys.SHIFT_LEFT:
                shift = true;
                break;
            case Keys.SHIFT_RIGHT:
                shift = true;
                break;

        }

        return keyProcessed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyProcessed = false;

        switch (keycode) {
            case Keys.LEFT:
                left = false;
                keyProcessed = true;
                System.out.println("Seta esquerda");
                break;
            case Keys.RIGHT:
                right = false;
                keyProcessed = true;
                System.out.println("Seta direita");
                break;
            case Keys.UP:
                up = false;
                keyProcessed = true;
                System.out.println("Seta cima");
                break;
            case Keys.DOWN:
                down = false;
                keyProcessed = true;
                System.out.println("Seta baixo");

            case Keys.A:
                a = false;
                keyProcessed = true;
                System.out.println("A");
                break;
            case Keys.D:
                d = false;
                keyProcessed = true;
                System.out.println("D");
                break;
            case Keys.W:
                w = false;
                keyProcessed = true;
                System.out.println("W");
                break;
            case Keys.S:
                s = false;
                keyProcessed = true;
                System.out.println("S");
            case Keys.ESCAPE:
                esc = false;
                keyProcessed = true;
                System.out.println("Escape");
                break;
            case Keys.SHIFT_LEFT:
                shift = false;
                break;
            case Keys.SHIFT_RIGHT:
                shift = false;
                break;
        }

        return keyProcessed;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            isM1Down = true;
        } else if (button == 1) {
            isM2Down = true;
        } else if (button == 2) {
            isM3down = true;
        }

        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;

        if (button == 0) {
            isM1Down = false;
        } else if (button == 1) {
            isM2Down = false;
        } else if (button == 2) {
            isM3down = false;
        }

        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
