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
import es.carlosrolindez.pong.utils.Assets
import es.carlosrolindez.pong.utils.GamePreferences
import es.carlosrolindez.pong.utils.OPTION_SCREEN_HEIGHT
import es.carlosrolindez.pong.utils.OPTION_SCREEN_WIDTH


class ConfigurationStage(private val gameScreen: PongScreen) : InputAdapter() {
    private val viewport = ExtendViewport(OPTION_SCREEN_WIDTH, OPTION_SCREEN_HEIGHT)

    private var activated : Boolean
    private var stage : Stage

    private var ui : PongUI


    init {

        viewport.camera.position.set(0f, 0f,0f)
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
        viewport.camera.position.set(0f, 0f,0f)
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
        stack.setSize(OPTION_SCREEN_WIDTH*0.8f, OPTION_SCREEN_HEIGHT*0.8f)

        loadSettings()

        stack.add(ui.optionsWin)
        stack.setPosition(-OPTION_SCREEN_WIDTH*0.4f, -OPTION_SCREEN_HEIGHT*0.4f)
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

        GamePreferences.instance.player1Name =ui.player1Name.text
        GamePreferences.instance.player2Name =ui.player2Name.text

        GamePreferences.instance.save()
    }


    inner class PongUI(skin : Skin) {
        internal var optionsWin = Window("Configuration",skin)

        private var optionsWinAudioSettings = Table()

        internal var chkSound: CheckBox
        internal var sldSound: Slider
        internal var chkMusic: CheckBox
        internal var sldMusic: Slider
        private  val btnWinOptClose: TextButton
        internal val player1Name : TextField
        internal val player2Name : TextField





        init {


  //          optionsWinAudioSettings.pad(10f, 10f, 0f, 10f)

            optionsWinAudioSettings.add(Label("Players", skin, "font", Color.BLACK)).colspan(4).padBottom(10f)
            optionsWinAudioSettings.row()

            optionsWinAudioSettings.add(Label("Player 1's name:", skin, "font", Color.BLACK)).colspan(2).padBottom(10f)
            player1Name = TextField(GamePreferences.instance.player1Name,skin)
            optionsWinAudioSettings.add(player1Name).colspan(2).padBottom(10f)

            optionsWinAudioSettings.row()
            optionsWinAudioSettings.add(Label("Player 2's name:", skin, "font", Color.BLACK)).colspan(2).padBottom(   10f)
            player2Name = TextField(GamePreferences.instance.player2Name,skin)
            optionsWinAudioSettings.add(player2Name).colspan(2).padBottom(15f)
            optionsWinAudioSettings.row()



            var lbl = Label("", skin)
            lbl.setColor(0.75f, 0.75f, 0.75f, 1f)
            lbl.style.background = skin.newDrawable("white")

            optionsWinAudioSettings.add(lbl).colspan(4).height(1f).width(OPTION_SCREEN_WIDTH*0.3f).pad(0f,0f,0f,1f)
            optionsWinAudioSettings.row()


            lbl = Label("", skin)
            lbl.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl.style.background = skin.newDrawable("white")
            optionsWinAudioSettings.add(lbl).colspan(4).height(1f).width(OPTION_SCREEN_WIDTH*0.3f).pad(0f,1f,15f,0f)
            optionsWinAudioSettings.row()

            optionsWinAudioSettings.add(Label("Audio", skin, "font", Color.BLACK)).colspan(4).padBottom(10f)

            optionsWinAudioSettings.row()


  /*          optionsWinAudioSettings.columnDefaults(0).padRight(10f).padBottom(15f)
            optionsWinAudioSettings.columnDefaults(1).padRight(10f).padBottom(15f)
            optionsWinAudioSettings.columnDefaults(2).padRight(10f).padBottom(15f)
            optionsWinAudioSettings.columnDefaults(3).padRight(10f).padBottom(15f)*/

            chkSound = CheckBox("", skin)
            chkSound.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setSound((actor as CheckBox).isChecked)
                }
            })
            optionsWinAudioSettings.add(chkSound).padBottom(15f)
            optionsWinAudioSettings.add(Label("Sound", skin, "font", Color.BLACK)).padBottom(15f)

            sldSound = Slider(0.0f, 1.0f, 0.1f, false, skin)
            sldSound.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setVolumeSound((actor as Slider).value)
                }
            })
            optionsWinAudioSettings.add(sldSound).colspan(2).padBottom(15f)
            optionsWinAudioSettings.row()

            chkMusic = CheckBox("", skin)
            chkMusic.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setMusic((actor as CheckBox).isChecked)
                }
            })
            optionsWinAudioSettings.add(chkMusic).padBottom(15f)
            optionsWinAudioSettings.add(Label("Music", skin, "font", Color.BLACK)).padBottom(15f)

            sldMusic = Slider(0.0f, 1.0f, 0.1f, false, skin)
            sldMusic.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setVolumeMusic((actor as Slider).value)
                }
            })
            optionsWinAudioSettings.add(sldMusic).colspan(2).padBottom(15f)
            optionsWinAudioSettings.row()



            lbl = Label("", skin)
            lbl.setColor(0.75f, 0.75f, 0.75f, 1f)
            lbl.style.background = skin.newDrawable("white")

            optionsWinAudioSettings.add(lbl).colspan(4).height(1f).width(OPTION_SCREEN_WIDTH*0.3f).pad(0f,0f,0f,1f)
            optionsWinAudioSettings.row()


            lbl = Label("", skin)
            lbl.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl.style.background = skin.newDrawable("white")
            optionsWinAudioSettings.add(lbl).colspan(4).height(1f).width(OPTION_SCREEN_WIDTH*0.3f).pad(0f,1f,15f,0f)
            optionsWinAudioSettings.row()



            btnWinOptClose = TextButton("Close", skin)
            btnWinOptClose.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    closeConfigurationWindow()
                }
            })
            optionsWinAudioSettings.add(btnWinOptClose).colspan(4)

            optionsWin.add(optionsWinAudioSettings)
 //           optionsWin.debugAll()
            optionsWin.pack()

        }
    }

}