package io.Prototipo.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Assets {
    public static final AssetDescriptor<Sound> SFX_JUMP = 
        new AssetDescriptor<>("sounds/jump.ogg", Sound.class);

        public static final AssetDescriptor<Music> PIXEL_SHADOWS = 
        new AssetDescriptor<>("sounds/Pixel Shadows.ogg", Music.class);

    
}
