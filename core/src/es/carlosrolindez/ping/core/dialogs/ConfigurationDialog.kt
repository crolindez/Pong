package es.carlosrolindez.ping.core.dialogs

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.utils.Assets
import es.carlosrolindez.ping.core.utils.DIALOG_SCREEN_WIDTH
import es.carlosrolindez.ping.core.utils.GamePreferences


class ConfigurationDialog(gameScreen: PingScreen) : BaseDialog(gameScreen , scale) {

    companion object {
        val scale = 0.8f
    }

    private var ui : ConfigurationUI


    init {
        ui = ConfigurationUI(Assets.instance.skin)

    }


    override fun prepareUi() { // load Settings

        ui.chkSound.isChecked= GamePreferences.instance.sound
        ui.sldSound.value=GamePreferences.instance.volSound

        ui.chkMusic.isChecked=GamePreferences.instance.music
        ui.sldMusic.value=GamePreferences.instance.volMusic

    }

    override fun closeUi() { // save Setting

        GamePreferences.instance.sound = ui.chkSound.isChecked
        GamePreferences.instance.volSound = ui.sldSound.value

        GamePreferences.instance.music = ui.chkMusic.isChecked
        GamePreferences.instance.volMusic = ui.sldMusic.value

        GamePreferences.instance.player1Name =ui.player1Name.text
        GamePreferences.instance.player2Name =ui.player2Name.text

        GamePreferences.instance.save()
    }

    override fun getUiActor() : Actor {
        return ui.optionsWin
    }

    inner class ConfigurationUI(skin : Skin) {
        internal var optionsWin = Window(Assets.instance.stringBundle.format("configuration"),skin)

        private var optionsWinAudioSettings = Table()

        internal var chkSound: CheckBox
        internal var sldSound: Slider
        internal var chkMusic: CheckBox
        internal var sldMusic: Slider
        private  val btnWinOptClose: TextButton
        internal val player1Name : TextField
        internal val player2Name : TextField


        init {


            optionsWinAudioSettings.add(Label(Assets.instance.stringBundle.format("players"), skin, "font", Color.BLACK)).colspan(4).padBottom(10f)
            optionsWinAudioSettings.row()

            optionsWinAudioSettings.add(Label(Assets.instance.stringBundle.format("playerName", 1), skin, "font", Color.BLACK)).colspan(2).padBottom(10f)
            player1Name = TextField(GamePreferences.instance.player1Name,skin)
            optionsWinAudioSettings.add(player1Name).colspan(2).padBottom(10f)

            optionsWinAudioSettings.row()
            optionsWinAudioSettings.add(Label(Assets.instance.stringBundle.format("playerName", 2), skin, "font", Color.BLACK)).colspan(2).padBottom(   10f)
            player2Name = TextField(GamePreferences.instance.player2Name,skin)
            optionsWinAudioSettings.add(player2Name).colspan(2).padBottom(15f)
            optionsWinAudioSettings.row()



            var lbl = Label("", skin)
            lbl.setColor(0.75f, 0.75f, 0.75f, 1f)
            lbl.style.background = skin.newDrawable("white")

            optionsWinAudioSettings.add(lbl).colspan(4).height(1f).width(DIALOG_SCREEN_WIDTH*scale/2f).pad(0f,0f,0f,1f)
            optionsWinAudioSettings.row()


            lbl = Label("", skin)
            lbl.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl.style.background = skin.newDrawable("white")
            optionsWinAudioSettings.add(lbl).colspan(4).height(1f).width(DIALOG_SCREEN_WIDTH*scale/2f).pad(0f,1f,15f,0f)
            optionsWinAudioSettings.row()

            optionsWinAudioSettings.add(Label(Assets.instance.stringBundle.format("audio"), skin, "font", Color.BLACK)).colspan(4).padBottom(10f)

            optionsWinAudioSettings.row()



            chkSound = CheckBox("", skin)
            chkSound.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    gameScreen.level.setSound((actor as CheckBox).isChecked)
                }
            })
            optionsWinAudioSettings.add(chkSound).padBottom(15f)
            optionsWinAudioSettings.add(Label(Assets.instance.stringBundle.format("sound"), skin, "font", Color.BLACK)).padBottom(15f)

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
            optionsWinAudioSettings.add(Label(Assets.instance.stringBundle.format("music"), skin, "font", Color.BLACK)).padBottom(15f)

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

            optionsWinAudioSettings.add(lbl).colspan(4).height(1f).width(DIALOG_SCREEN_WIDTH*scale/2f).pad(0f,0f,0f,1f)
            optionsWinAudioSettings.row()


            lbl = Label("", skin)
            lbl.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl.style.background = skin.newDrawable("white")
            optionsWinAudioSettings.add(lbl).colspan(4).height(1f).width(DIALOG_SCREEN_WIDTH*scale/2f).pad(0f,1f,15f,0f)
            optionsWinAudioSettings.row()



            btnWinOptClose = TextButton(Assets.instance.stringBundle.format("close"), skin)
            btnWinOptClose.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@ConfigurationDialog.closeDialog()
                }
            })
            optionsWinAudioSettings.add(btnWinOptClose).colspan(4)

            optionsWin.add(optionsWinAudioSettings)
 //           optionsWin.debugAll()
            optionsWin.pack()

        }
    }

}