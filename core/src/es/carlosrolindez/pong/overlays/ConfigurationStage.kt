package es.carlosrolindez.pong.overlays

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.pong.screens.PongScreen
import es.carlosrolindez.pong.utils.*


class ConfigurationStage(private val gameScreen: PongScreen) : InputAdapter() {
    private val viewport: ExtendViewport

    private var activated : Boolean
    private var stage : Stage

    private var ui : PongUI


    init {
        viewport = ExtendViewport(OPTION_SCREEN_WIDTH, OPTION_SCREEN_HEIGHT)
        viewport.camera.position.set(OPTION_SCREEN_WIDTH /2, OPTION_SCREEN_HEIGHT /2,0f)
        activated = false
        stage = Stage(viewport)
        ui = PongUI(Assets.instance.skin)
    }

    fun render(batch: SpriteBatch) {
        if (activated) {
            viewport.apply()

            batch.projectionMatrix = viewport.camera.combined
            batch.begin()

            stage.draw()
            batch.end()
        }
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(OPTION_SCREEN_WIDTH/2, OPTION_SCREEN_HEIGHT/2,0f)
    }

    fun dispose() {
        stage.dispose()
    }

    fun update(delta: Float) {
        if (activated)
            stage.act(delta)
    }

    internal fun openConfigurationWindow() {
        activated = true
        val stack = Stack()
        stage.addActor(stack)
        stack.setSize(OPTION_SCREEN_WIDTH/2, OPTION_SCREEN_HEIGHT/2)

        loadSettings()
        stack.add(ui.optionsWin)
        stack.setPosition(OPTION_SCREEN_WIDTH/4, OPTION_SCREEN_HEIGHT/4)
        Gdx.app.input.inputProcessor = stage

    }

    internal fun closeConfigurationWindow() {
        saveSettings()
        activated = false
        stage.clear()
        Gdx.app.input.inputProcessor = gameScreen.gui
    }

    private fun loadSettings() {

        ui.chkSound.isChecked=GamePreferences.instance.sound
        ui.sldSound.value=GamePreferences.instance.volSound

        ui.chkMusic.isChecked=GamePreferences.instance.music
        ui.sldMusic.value=GamePreferences.instance.volMusic

    }

    private fun saveSettings() {

        GamePreferences.instance.sound = ui.chkSound.isChecked
        GamePreferences.instance.volSound = ui.sldSound.value

        GamePreferences.instance.music = ui.chkMusic.isChecked
        GamePreferences.instance.volMusic = ui.sldMusic.value

        GamePreferences.instance.save()
    }


    inner class PongUI(skin : Skin) {
        internal var optionsWin = Window("Configuration",skin)
        internal var optionsWinAudioSettings: Table
        internal var chkSound: CheckBox
        internal var sldSound: Slider
        internal var chkMusic: CheckBox
        internal var sldMusic: Slider
        internal val btnWinOptClose: TextButton



        init {

            optionsWinAudioSettings = Table()
            optionsWinAudioSettings.pad(10f, 10f, 0f, 10f)
            optionsWinAudioSettings.add(Label("Audio", skin, "font", Color.BLACK)).colspan(3).padBottom(25f)

            optionsWinAudioSettings.row()


            optionsWinAudioSettings.columnDefaults(0).padRight(10f).padBottom(15f)
            optionsWinAudioSettings.columnDefaults(1).padRight(10f).padBottom(15f)
            optionsWinAudioSettings.columnDefaults(2).padRight(10f).padBottom(15f)

            chkSound = CheckBox("", skin)
            chkSound.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setSound((actor as CheckBox).isChecked)
                }
            })
            optionsWinAudioSettings.add(chkSound)
            optionsWinAudioSettings.add<Label>(Label("Sound", skin))

            sldSound = Slider(0.0f, 1.0f, 0.1f, false, skin)
            sldSound.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setVolumeSound((actor as Slider).value)
                }
            })
            optionsWinAudioSettings.add(sldSound)
            optionsWinAudioSettings.row()

            chkMusic = CheckBox("", skin)
            chkMusic.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setMusic((actor as CheckBox).isChecked)
                }
            })
            optionsWinAudioSettings.add(chkMusic)
            optionsWinAudioSettings.add<Label>(Label("Music", skin))

            sldMusic = Slider(0.0f, 1.0f, 0.1f, false, skin)
            sldMusic.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setVolumeMusic((actor as Slider).value)
                }
            })
            optionsWinAudioSettings.add(sldMusic)
            optionsWinAudioSettings.row()



            var lbl = Label("", skin)
            lbl.setColor(0.75f, 0.75f, 0.75f, 1f)
            //lbl.setStyle(Label.LabelStyle(lbl.getStyle()))
            lbl.getStyle().background = skin.newDrawable("white")

            optionsWinAudioSettings.add(lbl).colspan(3).height(1f).width(OPTION_SCREEN_WIDTH*0.3f).pad(10f,0f,0f,1f)
            optionsWinAudioSettings.row()


            lbl = Label("", skin)
            lbl.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl.getStyle().background = skin.newDrawable("white")
            optionsWinAudioSettings.add(lbl).colspan(3).height(1f).width(OPTION_SCREEN_WIDTH*0.3f).pad(0f,1f,30f,0f)
            optionsWinAudioSettings.row()



            btnWinOptClose = TextButton("Close", skin)
            btnWinOptClose.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    closeConfigurationWindow()
                }
            })
            optionsWinAudioSettings.add(btnWinOptClose).colspan(3)//.padRight(30f)



            optionsWin.add(optionsWinAudioSettings)//.row()
 //           optionsWin.debugAll()
            optionsWin.pack()

        }
    }

}