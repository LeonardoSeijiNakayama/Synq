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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.Prototipo.Prototipo;

import java.util.ArrayList;
import java.util.List;

public class LevelsScreen implements Screen {

    private Prototipo parent;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private List<Actor> focusableWidgets;
    private int currentFocusIndex = 0;

    public LevelsScreen(Prototipo Prototipo) {
        parent = Prototipo;

        stage = new Stage(new ScreenViewport());
        atlas = new TextureAtlas(Gdx.files.internal("images/ui.atlas"));
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

        parameter.size = 32;
        parameter.color = Color.WHITE;
        BitmapFont retroFontBig = generator.generateFont(parameter);
        generator.dispose();

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = new TextureRegionDrawable(atlas.findRegion("small_button_up"));
        tbs.down = new TextureRegionDrawable(atlas.findRegion("small_button_pressed"));
        tbs.over = new TextureRegionDrawable(atlas.findRegion("small_button_hover"));
        tbs.checked = new TextureRegionDrawable(atlas.findRegion("small_button_hover"));
        tbs.checkedOver = tbs.checked;
        tbs.font = retroFont;

        TextButton.TextButtonStyle tbsb = new TextButton.TextButtonStyle();
        tbsb.up = new TextureRegionDrawable(atlas.findRegion("button_up"));
        tbsb.down = new TextureRegionDrawable(atlas.findRegion("button_pressed"));
        tbsb.over = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        tbsb.checked = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        tbsb.checkedOver = tbs.checked;
        tbsb.font = retroFont;

        Label.LabelStyle lbs = new Label.LabelStyle();
        lbs.font = retroFontBig;

        skin.add("default-textbutton", tbs);
        skin.add("big-textbutton", tbsb);
        skin.add("default-label", lbs);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton levelOne = new TextButton("1", skin, "default-textbutton");
        TextButton levelTwo = new TextButton("2", skin, "default-textbutton");
        TextButton levelThree = new TextButton("3", skin, "default-textbutton");
        TextButton levelFour = new TextButton("4", skin, "default-textbutton");
        TextButton back = new TextButton("Voltar", skin, "big-textbutton");
        Label selectALevel = new Label("Selecione um level", skin, "default-label");

        selectALevel.setFontScale(1.5f);

        Table buttonsTable = new Table();
        buttonsTable.defaults().size(144, 128).pad(10);

        buttonsTable.add(levelOne);
        buttonsTable.add(levelTwo).row();
        buttonsTable.add(levelThree);
        buttonsTable.add(levelFour).row();

        table.add(selectALevel).center().padBottom(20).row();
        table.add(buttonsTable).center().row();
        table.add(back);

        focusableWidgets = new ArrayList<>();
        focusableWidgets.add(levelOne);
        focusableWidgets.add(levelTwo);
        focusableWidgets.add(levelThree);
        focusableWidgets.add(levelFour);
        focusableWidgets.add(back);

        Gdx.input.setInputProcessor(stage);
        setupInputListeners();
        setFocusToActor(focusableWidgets.get(currentFocusIndex));

        levelOne.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Prototipo.PHASE1);
            }
        });

        levelTwo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Prototipo.PHASE2);
            }
        });

        levelThree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Prototipo.PHASE3);
            }
        });

        levelFour.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.changeScreen(Prototipo.PHASE4);
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.changeScreen(Prototipo.MENU);
            }
        });

    }

    private void setupInputListeners(){
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
                    focusNextWidget();
                    return true;
                }else if(keycode == Input.Keys.LEFT|| keycode == Input.Keys.A){
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
