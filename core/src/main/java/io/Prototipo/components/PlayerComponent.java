package io.Prototipo.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerComponent implements Component, Poolable{
    public static final int ONE = 1, TWO = 2;

    public boolean isAtDoor = false;
    public boolean isDead = false;
    public boolean pass = false;
    public boolean left = true;
    public int size = 0;
    public int id;

    @Override
    public void reset(){
        isAtDoor = false;
        isDead = false;
        pass = false;
        left = true;
        size = 0;
    }
}
