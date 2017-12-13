package es.carlosrolindez.pong.dialogs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.pong.PongScreen
import es.carlosrolindez.pong.net.Network
import es.carlosrolindez.pong.net.NetworkServer
import es.carlosrolindez.pong.utils.Assets
import es.carlosrolindez.pong.utils.DIALOG_SCREEN_HEIGHT
import es.carlosrolindez.pong.utils.DIALOG_SCREEN_WIDTH
import es.carlosrolindez.pong.utils.GamePreferences


class MessageDialog(private val gameScreen: PongScreen) : BaseDialog(gameScreen , 0.5f) {
    private var ui : AcceptUI
    private var messageHeader = "Trying to connect with "
    lateinit private var message : String

    init {
        ui = AcceptUI(Assets.instance.skin)
    }

    internal fun openDialog(message : String) {
        this.message = message
        super.openDialog()
    }

    override fun prepareUi() { // load Settings
        ui.label.setText(messageHeader + message)
    }

    override fun closeUi() { // save Setting

    }

    override fun getUiActor() : Actor {
        return ui.acceptWin
    }


    inner class AcceptUI(skin : Skin) {
        internal var acceptWin = Window("Message",skin)

        private var acceptTable = Table()

        internal val label = Label(messageHeader, skin, "font", Color.BLACK)

        init {

            acceptTable.add(label).colspan(2).padBottom(10f)
            acceptTable.row()

            acceptWin.add(acceptTable)
 //           optionsWin.debugAll()
            acceptWin.pack()

        }
    }

}