package io.Prototipo.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class EnemyComponent implements Component, Poolable{
    public static final int NORMAL = 1;
    public int id;
    public boolean isDead = false;

    @Override
    public void reset(){
        isDead = false;
    }
    
}
