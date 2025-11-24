package io.Prototipo.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DoorComponent implements Component, Poolable{
    public static int ONE = 1, TWO = 2;

    public int id;

    @Override
    public void reset(){
        id = 0;
    }
}
