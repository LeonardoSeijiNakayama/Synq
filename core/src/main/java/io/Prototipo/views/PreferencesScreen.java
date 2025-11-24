package io.Prototipo.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.Prototipo.Prototipo;;import java.util.ArrayList;

public class PreferencesScreen implements Screen {

    private Prototipo parent;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private int currentFocusIndex = 0;
    private java.util.List<Actor> focusableWidgets;

    private Label titleLabel, volumeMusicLabel, volumeSoundLabel, musicOnOffLabel, soundOnOffLabel;

    public PreferencesScreen(Prototipo Prototipo) {
        parent = Prototipo;

        stage = new Stage(new ScreenViewport());
        atlas = new TextureAtlas(Gdx.files.internal("images/ui.atlas"));
        skin = new Skin();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("skin/Commodore Pixeled.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 32;
        p.characters = "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "0123456789" +
                "áàãâäéèêëíìîïóòõôöúùûüç" +
                "ÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÔÖÚÙÛÜÇ" +
                "!?@#$%^&*()-_=+[]{}|;:'\",.<>/\\`~";
        BitmapFont retro = gen.generateFont(p);
        p.color = Color.BLACK;
        BitmapFont retroBtn = gen.generateFont(p);
        gen.dispose();

        skin.add("default-font", retro, BitmapFont.class);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(atlas.findRegion("trilha"));
        sliderStyle.knob = new TextureRegionDrawable(atlas.findRegion("knob"));
        skin.add("default-horizontal", sliderStyle);

        CheckBox.CheckBoxStyle cbStyle = new CheckBox.CheckBoxStyle();
        cbStyle.checkboxOff = new TextureRegionDrawable(atlas.findRegion("checkbox_unchecked"));
        cbStyle.checkboxOver = new TextureRegionDrawable(atlas.findRegion("checkbox_unchecked_hover"));
        cbStyle.checkboxOn = new TextureRegionDrawable(atlas.findRegion("checkbox_checked"));
        cbStyle.checkboxOnOver = new TextureRegionDrawable(atlas.findRegion("checkbox_checked_hover"));
        cbStyle.font = retro;
        skin.add("default", cbStyle);

        CheckBox.CheckBoxStyle cbFocus = new CheckBox.CheckBoxStyle();
        cbFocus.checkboxOff    = cbStyle.checkboxOver;     // <- aqui está o “truque”
        cbFocus.checkboxOn     = cbStyle.checkboxOnOver;   // idem, versão marcada
        cbFocus.checkboxOver   = cbStyle.checkboxOver;
        cbFocus.checkboxOnOver = cbStyle.checkboxOnOver;
        cbFocus.font = retro;
        skin.add("cb_focus", cbFocus);

        TextButton.TextButtonStyle btn = new TextButton.TextButtonStyle();
        btn.up = new TextureRegionDrawable(atlas.findRegion("button_up"));
        btn.down = new TextureRegionDrawable(atlas.findRegion("button_pressed"));
        btn.over = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        btn.checked = new TextureRegionDrawable(atlas.findRegion("button_hover"));
        btn.checkedOver = btn.checked;
        btn.font = retroBtn;
        skin.add("default", btn);

        skin.add("default", new Label.LabelStyle(retro, com.badlogic.gdx.graphics.Color.WHITE));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        volumeMusicSlider.setValue(parent.getPreferences().getMusicVolume());
        volumeMusicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                parent.getPreferences().setMusicVolume(volumeMusicSlider.getValue());
                return false;
            }
        });

        final Slider soundMusicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundMusicSlider.setValue(parent.getPreferences().getSoundVolume());
        soundMusicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                parent.getPreferences().setSoundVolume(soundMusicSlider.getValue());
                return false;
            }
        });

        final CheckBox musicCheckBox = new CheckBox(null, skin);
        musicCheckBox.setChecked(parent.getPreferences().isMusicEnabled());
        musicCheckBox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckBox.isChecked();
                parent.getPreferences().setMusicEnabled(enabled);
                return false;
            }
        });

        final CheckBox soundEffectsCheckBox = new CheckBox(null, skin);
        soundEffectsCheckBox.setChecked(parent.getPreferences().isSoundEffectEnabled());
        soundEffectsCheckBox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckBox.isChecked();
                parent.getPreferences().setSoundEffectEnabled(enabled);
                return false;
            }
        });

        final TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Prototipo.MENU);
            }
        });

        titleLabel = new Label("Preferências", skin);
        volumeMusicLabel = new Label("Volume da Música", skin);
        volumeSoundLabel = new Label("Volume do Som", skin);
        musicOnOffLabel = new Label("Música", skin);
        soundOnOffLabel = new Label("Efeitos Sonoros", skin);

        table.add(titleLabel).colspan(2);
        table.row().pad(10, 0, 0, 10);
        table.add(volumeMusicLabel).left();
        table.add(volumeMusicSlider);
        table.row().pad(10, 0, 0, 10);
        table.add(musicOnOffLabel).left();
        table.add(musicCheckBox);
        table.row().pad(10, 0, 0, 10);
        table.add(volumeSoundLabel).left();
        table.add(soundMusicSlider);
        table.row().pad(10, 0, 0, 10);
        table.add(soundOnOffLabel).left();
        table.add(soundEffectsCheckBox);
        table.row().pad(10, 0, 0, 10);
        table.add(backButton).colspan(2);

        focusableWidgets = new ArrayList<>();
        focusableWidgets.add(volumeMusicSlider);
        focusableWidgets.add(musicCheckBox);
        focusableWidgets.add(soundMusicSlider);
        focusableWidgets.add(soundEffectsCheckBox);
        focusableWidgets.add(backButton);

        setupInputListeners();
        setFocusToActor(focusableWidgets.get(currentFocusIndex));

    }

    private void setupInputListeners(){
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
                    focusNextWidget();
                    return true;
                }else if(keycode == Input.Keys.UP|| keycode == Input.Keys.W){
                    focusPreviousWidget();
                    return true;
                }

                Actor focused = stage.getKeyboardFocus();

                if (focused instanceof Slider && ((keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT)||(keycode == Input.Keys.A || keycode == Input.Keys.D))){
                    Slider s = (Slider) focused;
                    float step = s.getStepSize();
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                        step *= 5f;
                    }
                    if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
                        s.setValue(Math.min(s.getMaxValue(), s.getValue() + step));
                    } else {
                        s.setValue(Math.max(s.getMinValue(), s.getValue() - step));
                    }
                    return true;
                }

                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE
                    || keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT){
                    if (focused instanceof CheckBox){
                        CheckBox cb = (CheckBox) focused;
                        cb.setChecked(!cb.isChecked());
                        cb.fire(new ChangeListener.ChangeEvent());
                        return true;
                    } else if (focused instanceof Button){
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

        highlight(old, false);

        stage.setKeyboardFocus(actor);

        highlight(actor, true);

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

    private void highlight(Actor actor, boolean on){
        if (actor == null) return;

        if (actor instanceof TextButton && !(actor instanceof CheckBox)) {
            TextButton b = (TextButton) actor;
            b.setProgrammaticChangeEvents(false);
            b.setChecked(on);
            b.setProgrammaticChangeEvents(true);

        } else if (actor instanceof CheckBox) {
            CheckBox cb = (CheckBox) actor;
            cb.setStyle(skin.get(on ? "cb_focus" : "default", CheckBox.CheckBoxStyle.class));


        } else if (actor instanceof Slider) {
            actor.setColor(on ? Color.YELLOW : Color.WHITE);
            actor.setScale(on ? 1.02f : 1f);

        }
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

    }

}
