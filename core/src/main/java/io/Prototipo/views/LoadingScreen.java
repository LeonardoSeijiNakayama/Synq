package io.Prototipo.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import io.Prototipo.Prototipo;

public class LoadingScreen implements Screen {

    private Prototipo parent;
    private SpriteBatch sb;

    public final int IMAGE = 0;
    public final int FONT = 1;
    public final int PARTY = 2;
    public final int SOUND = 3;
    public final int MUSIC = 4;

    private int currentLoadingStage = 0;
    private BitmapFont loadingFont;
    private final GlyphLayout layout = new GlyphLayout();

    public float countDown = 5f;

    public LoadingScreen(Prototipo Prototipo) {
        parent = Prototipo;
        sb = new SpriteBatch();
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    }

    @Override
    public void show() {

        parent.assMan.queueAddImages();
        System.out.println("Loading images...");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/Commodore Pixeled.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.characters = "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "0123456789" +
                "áàãâäéèêëíìîïóòõôöúùûüç" +
                "ÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÔÖÚÙÛÜÇ" +
                "!?@#$%^&*()-_=+[]{}|;:'\",.<>/\\`~";
        parameter.flip = false;

        loadingFont = generator.generateFont(parameter);
        loadingFont.setColor(Color.WHITE);
        generator.dispose();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // clear the screen
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (parent.assMan.manager.update()) {
            currentLoadingStage += 1;
            switch (currentLoadingStage) {
                case FONT:
                    System.out.println("Loading fonts....");
                    parent.assMan.queueAddFonts();
                    break;
                case PARTY:
                    System.out.println("Loading Particle Effects....");
                    parent.assMan.queueAddParticleEffects();
                    break;
                case SOUND:
                    System.out.println("Loading Sounds....");
                    parent.assMan.queueAddSounds();
                    break;
                case MUSIC:
                    System.out.println("Loading sounds....");
                    parent.assMan.queueAddMusic();
                    break;
                case 5:
                    System.out.println("Finished"); // all done
                    break;
            }
            if (currentLoadingStage > 5) {
                countDown -= delta;
                currentLoadingStage = 5;

                if (countDown < 0) {
                    parent.changeScreen(Prototipo.MENU);
                }
            }
        }
        sb.begin();
        String txt = "Carregando... " + Math.round(parent.assMan.manager.getProgress() * 100f) + "%";
        layout.setText(loadingFont, txt);
        float x = (Gdx.graphics.getWidth() - layout.width) * 0.5f;
        float y = (Gdx.graphics.getHeight() - layout.height) * 0.5f;
        loadingFont.draw(sb, layout, x, y);
        sb.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        sb.dispose();
        if (loadingFont != null)
            loadingFont.dispose();

    }

}
