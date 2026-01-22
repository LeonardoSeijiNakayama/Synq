package io.Prototipo.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
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

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;


import io.Prototipo.Prototipo;;import java.util.ArrayList;
import java.util.List;

public class WinScreen implements Screen {

    private Prototipo parent;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private int currentFocusIndex = 0;
    private List<Actor> focusableWidgets;

    private Controller controller;
    private ControllerMapping mapping;

    // estados anteriores pra detectar "apertou agora" (edge)
    private boolean prevDpadRight, prevDpadLeft, prevConfirm, prevBack, prevStart;


    public WinScreen(Prototipo Prototipo) {
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

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = new TextureRegionDrawable(atlas.findRegion("button_up"));
        tbs.down = new TextureRegionDrawable(atlas.findRegion("button_pressed"));
        tbs.over = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        tbs.checked = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        tbs.checkedOver = tbs.checked;
        tbs.font = retroFont32;
        skin.add("default", tbs);

        skin.add("default", new Label.LabelStyle(retroFont, com.badlogic.gdx.graphics.Color.WHITE));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton continueButton = new TextButton("Continuar", skin);
        TextButton backButton = new TextButton("Voltar", skin);
        Label congratulations = new Label("Parabéns!", skin);
        Label youPassed = new Label("Você passou!", skin);

        congratulations.setFontScale(1.5f);
        Table buttonsTable = new Table();
        buttonsTable.defaults().size(320, 128).pad(10);

        buttonsTable.add(backButton);
        buttonsTable.add(continueButton);

        table.add(congratulations).center().row();
        table.add(youPassed).center().padBottom(20).row();
        table.add(buttonsTable).center();

        focusableWidgets = new ArrayList<>();
        focusableWidgets.add(backButton);
        focusableWidgets.add(continueButton);

        Gdx.input.setInputProcessor(stage);
        setupInputListeners();
        setFocusToActor(focusableWidgets.get(currentFocusIndex));

        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (parent.currentLevel + 1 <= 13) {
                    parent.nextLevel();
                    parent.changeScreen(parent.currentLevel);
                } else {
                    parent.changeScreen(Prototipo.MENU);
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Prototipo.MENU);
            }
        });

        setupController();

    }


    private void setupInputListeners(){
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if(keycode == Input.Keys.RIGHT|| keycode == Input.Keys.D){
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
        handleControllerUiNavigation();
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

    private void setupController() {
        if (Controllers.getControllers().size > 0) {
            controller = Controllers.getControllers().first();
            mapping = controller.getMapping();
        }
    }

    private void handleControllerUiNavigation() {
        // se conectou depois, tenta pegar
        if (controller == null || mapping == null) {
            if (Controllers.getControllers().size > 0) {
                controller = Controllers.getControllers().first();
                mapping = controller.getMapping();
            } else {
                return;
            }
        }

        boolean dpadRight = (mapping.buttonDpadRight != -1) && controller.getButton(mapping.buttonDpadRight);
        boolean dpadLeft  = (mapping.buttonDpadLeft  != -1) && controller.getButton(mapping.buttonDpadLeft);
        boolean confirm = (mapping.buttonA != -1) && controller.getButton(mapping.buttonA);
        boolean backBtn = (mapping.buttonB != -1) && controller.getButton(mapping.buttonB);
        boolean startBtn = (mapping.buttonStart != -1) && controller.getButton(mapping.buttonStart);

        if (dpadRight && !prevDpadRight) {
            focusNextWidget();
        }
        if (dpadLeft && !prevDpadLeft) {
            focusPreviousWidget();
        }
        if (confirm && !prevConfirm) {
            activateFocusedWidget();
        }
        if (backBtn && !prevBack) {
            // escolha 1: voltar pro menu
            parent.changeScreen(Prototipo.MENU);
            // ou escolha 2: só ativar o botão "Voltar" se estiver focado nele
            // setFocusToActor(focusableWidgets.get(focusableWidgets.size()-1));
            // activateFocusedWidget();
        }
        if (startBtn && !prevStart) {

            parent.changeScreen(Prototipo.MENU);
        }

        prevDpadRight = dpadRight;
        prevDpadLeft  = dpadLeft;
        prevConfirm   = confirm;
        prevBack      = backBtn;
        prevStart     = startBtn;
    }

    private void activateFocusedWidget() {
        Actor focused = stage.getKeyboardFocus();
        if (focused instanceof Button) {
            focused.fire(new ChangeListener.ChangeEvent());
        }
    }

}
