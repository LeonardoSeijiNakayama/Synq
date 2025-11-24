package io.Prototipo.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TypeComponent implements Component, Poolable{
    public static final int PLAYER = 0;
    public static final int ENEMY= 1;
    public static final int SCENERY = 2;
    public static final int OTHER = 4;
    public static final int DOOR = 5;
    public static final int PIT = 6;
    public static final int GROUND = 7;

    public int type = OTHER;

    @Override
    public void reset(){
        type =  OTHER;
    }
}
