package io.Prototipo.views;


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
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.Prototipo.Prototipo;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen implements Screen {

    private Prototipo parent;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private List<Actor> focusableWidgets;
    private int currentFocusIndex = 0;

    private TextureRegionDrawable logoTex;
    Image logo;

    public MenuScreen(Prototipo Prototipo) {
        parent = Prototipo;

        stage = new Stage(new ScreenViewport());
        atlas = new TextureAtlas(Gdx.files.internal("images/ui.atlas"));

        logoTex = new TextureRegionDrawable(atlas.findRegion("logo"));
        logo = new Image(logoTex);
        logo.setScaling(Scaling.fit);
        logo.setSize(logo.getPrefWidth() * .3f, logo.getPrefHeight() * .3f);

        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/Commodore Pixeled.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 32;
        parameter.characters = "abcdefghijklmnopqrstuvwxyz" + // minúsculas
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + // maiúsculas
                "0123456789" + // números
                "áàãâäéèêëíìîïóòõôöúùûüç" + // minúsculas acentuadas
                "ÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÔÖÚÙÛÜÇ" + // maiúsculas acentuadas
                "!?@#$%^&*()-_=+[]{}|;:'\",.<>/\\`~";
        parameter.flip = false;
        parameter.color = Color.BLACK;

        BitmapFont retroFont = generator.generateFont(parameter);
        generator.dispose();

        skin.add("default-font", retroFont, BitmapFont.class);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = new TextureRegionDrawable(atlas.findRegion("button_up"));
        tbs.down = new TextureRegionDrawable(atlas.findRegion("button_pressed"));
        tbs.over = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        tbs.checked      = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        tbs.checkedOver  = tbs.checked;
        tbs.font = retroFont;
        skin.add("default", tbs);

        skin.add("default", new Label.LabelStyle(retroFont, com.badlogic.gdx.graphics.Color.WHITE));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton newGame = new TextButton("Jogar", skin);
        TextButton preferences = new TextButton("Preferências", skin);
        TextButton exit = new TextButton("Sair", skin);

        table.add(logo).center().padBottom(32).row();
        table.add(newGame);
        table.row().pad(10, 0, 10, 0);
        table.add(preferences);
        table.row();
        table.add(exit).padBottom(32);

        focusableWidgets = new ArrayList<>();
        focusableWidgets.add(newGame);
        focusableWidgets.add(preferences);
        focusableWidgets.add(exit);

        Gdx.input.setInputProcessor(stage);

        setupInputListeners();

        setFocusToActor(focusableWidgets.get(currentFocusIndex));

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Prototipo.LEVELS);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Prototipo.PREFERENCES);
            }
        });

    }

    private void setupInputListeners(){
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if(keycode == Input.Keys.DOWN|| keycode == Input.Keys.S){
                    focusNextWidget();
                    return true;
                }else if(keycode == Input.Keys.UP|| keycode == Input.Keys.W){
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

}
