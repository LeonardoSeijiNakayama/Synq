package io.Prototipo.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.Prototipo.Prototipo;;

public class LoseScreen implements Screen {

    private Prototipo parent;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private List<Actor> focusableWidgets;
    private int currentFocusIndex = 0;

    private String message;

    public LoseScreen(Prototipo Prototipo) {
        parent = Prototipo;

        stage = new Stage(new ScreenViewport());
        atlas = new TextureAtlas(Gdx.files.internal("images/ui.atlas"));
        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/Commodore Pixeled.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 24;
        parameter.characters = "abcdefghijklmnopqrstuvwxyz" + // minúsculas
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + // maiúsculas
                "0123456789" + // números
                "áàãâäéèêëíìîïóòõôöúùûüç" + // minúsculas acentuadas
                "ÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÔÖÚÙÛÜÇ" + // maiúsculas acentuadas
                "!?@#$%^&*()-_=+[]{}|;:'\",.<>/\\`~";
        parameter.flip = false;

        BitmapFont retroFont = generator.generateFont(parameter);
        parameter.size = 32;
        parameter.color = Color.BLACK;
        BitmapFont retroFont32 = generator.generateFont(parameter);
        generator.dispose();

        skin.add("default-font", retroFont, BitmapFont.class);
        skin.add("big", retroFont32, BitmapFont.class);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = new TextureRegionDrawable(atlas.findRegion("button_up"));
        tbs.down = new TextureRegionDrawable(atlas.findRegion("button_pressed"));
        tbs.over = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        tbs.checked = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        tbs.checkedOver = tbs.checked;
        tbs.font = retroFont32;
        skin.add("default", tbs);

        skin.add("default", new Label.LabelStyle(retroFont, com.badlogic.gdx.graphics.Color.WHITE));

        Random rand = new Random();

        int num = rand.nextInt(5);

        switch (num) {
            case 0:
                message = "E não sobrou nada...";
                break;

            case 1:
                message = "Não desista ainda!";
                break;
            case 2:
                message = "Até uma criança de 5 anos passaria...";
                break;
            case 3:
                message = "Tô começando a perder as esperanças.";
                break;
            case 4:
                message = "Já pode desistir agora!";
                break;
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton tryAgainButton = new TextButton("Recomeçar", skin);
        TextButton backButton = new TextButton("Voltar", skin);
        Label youFailed = new Label(message, skin);

        youFailed.setFontScale(1.5f);
        youFailed.setAlignment(Align.center);
        youFailed.setWrap(true);

        Table buttonsTable = new Table();
        buttonsTable.defaults().size(320, 128).pad(10);

        buttonsTable.add(backButton);
        buttonsTable.add(tryAgainButton);

        focusableWidgets = new ArrayList<>();
        focusableWidgets.add(backButton);
        focusableWidgets.add(tryAgainButton);

        Gdx.input.setInputProcessor(stage);

        setupInputListeners();

        setFocusToActor(focusableWidgets.get(currentFocusIndex));

        float textWidth = stage.getWidth() * 0.8f;

        table.add(youFailed)
                .width(textWidth)
                .center()
                .padBottom(20)
                .row();

        table.add(buttonsTable).center();

        tryAgainButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(parent.currentLevel);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Prototipo.MENU);
            }
        });

    }

    private void setupInputListeners(){
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if(keycode == Input.Keys.LEFT|| keycode == Input.Keys.A){
                    focusNextWidget();
                    return true;
                }else if(keycode == Input.Keys.RIGHT|| keycode == Input.Keys.D){
                    focusPreviousWidget();
                    return true;
                }else if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE
                    || keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT){
                    Actor focused = stage.getKeyboardFocus();
                    if (focused instanceof Button){
                        focused.fire(new ChangeListener.ChangeEvent());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void focusNextWidget() {
        currentFocusIndex = (currentFocusIndex + 1) % focusableWidgets.size();
        setFocusToActor(focusableWidgets.get(currentFocusIndex));
    }

    private void focusPreviousWidget() {
        currentFocusIndex = (currentFocusIndex - 1 + focusableWidgets.size()) % focusableWidgets.size();
        setFocusToActor(focusableWidgets.get(currentFocusIndex));
    }

    private void setFocusToActor(Actor actor){
        Actor old = stage.getKeyboardFocus();

        if (old instanceof TextButton) {
            TextButton tb = (TextButton) old;
            boolean prev = tb.isChecked();
            tb.setProgrammaticChangeEvents(false);
            tb.setChecked(false);
            tb.setProgrammaticChangeEvents(true);
        }

        stage.setKeyboardFocus(actor);

        if (actor instanceof TextButton) {
            TextButton tb = (TextButton) actor;
            tb.setProgrammaticChangeEvents(false);
            tb.setChecked(true);
            tb.setProgrammaticChangeEvents(true);
        }

        if (old != null){
            FocusListener.FocusEvent unfocus = new FocusListener.FocusEvent();
            unfocus.setType(FocusListener.FocusEvent.Type.keyboard);
            unfocus.setFocused(false);
            unfocus.setRelatedActor(actor);
            old.fire(unfocus);
        }

        FocusListener.FocusEvent focus = new FocusListener.FocusEvent();
        focus.setType(FocusListener.FocusEvent.Type.keyboard);
        focus.setFocused(true);
        focus.setRelatedActor(old);
        actor.fire(focus);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
    }

    public void reset() {
        Random rand = new Random();

        int num = rand.nextInt(5);

        switch (num) {
            case 0:
                message = "E não sobrou nada...";
                break;

            case 1:
                message = "Não desista ainda!";
                break;
            case 2:
                message = "Até uma criança de 5 anos passaria...";
                break;
            case 3:
                message = "Tô começando a perder as esperanças.";
                break;
            case 4:
                message = "Já pode desistir agora!";
                break;
        }
    }

}
