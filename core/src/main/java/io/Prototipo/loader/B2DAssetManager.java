package io.Prototipo.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;

public class B2DAssetManager {

    public final AssetManager manager = new AssetManager();

    public final String skin = "skin/glassy-ui.json";

    public final String gameImages = "images/Images.atlas";
    public final String loadingImages = "images/loading.atlas"; 
    public final String loadUI = "images/ui.atlas";

    public void queueAddImages() {
        manager.load(gameImages, TextureAtlas.class);
        manager.load(loadUI, TextureAtlas.class);
        
    }

    public void queueAddLoadingImages() {
        manager.load(loadingImages, TextureAtlas.class);
        
    }

    public void queueAddSounds() {
        manager.load(Assets.SFX_JUMP);
    }

    public void queueAddMusic() {
        manager.load(Assets.PIXEL_SHADOWS);
    }

    public void queueAddSkin() {
        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
        manager.load(skin, Skin.class, params);
    }

    public void queueAddFonts() {
    }

    public void queueAddParticleEffects() {
    }

}
