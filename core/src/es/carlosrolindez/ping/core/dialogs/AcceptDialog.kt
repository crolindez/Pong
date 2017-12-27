package es.carlosrolindez.ping.core.dialogs

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.net.Network
import es.carlosrolindez.ping.core.utils.Assets
import es.carlosrolindez.ping.core.utils.GamePreferences


class AcceptDialog(pingScreen: PingScreen) : BaseDialog(pingScreen , 0.5f, 9) {
    private var ui : AcceptUI
    private var messageHeader = ""

    init {
        ui = AcceptUI(Assets.instance.skin)
    }

    override fun prepareUi() { // load Settings
        ui.label.setText(Assets.instance.stringBundle.format("playWith", pingScreen.opponentName))
    }

    override fun closeUi() { // save Setting

    }

    override fun getUiActor() : Actor {
        return ui.acceptWin
    }


    inner class AcceptUI(skin : Skin) {
        internal var acceptWin = Window(Assets.instance.stringBundle.format("message"),skin)

        private var acceptTable = Table()

        private  val btnNo: TextButton
        private  val btnOk: TextButton

        internal val label = Label(messageHeader, skin, "font", Color.BLACK)



        init {

            acceptTable.add(label).colspan(2).padBottom(10f)
            acceptTable.row()

            btnOk = TextButton("Ok", skin)
            btnOk.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    val message = Network.LoginAccepted()
                    message.serverName = GamePreferences.instance.player1Name
                    Network.connection?.sendTCP(message)
                    this@AcceptDialog.closeDialog()
                }
            })
            acceptTable.add(btnOk)

            btnNo = TextButton("No", skin)
            btnNo.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    val message = Network.LoginRejected()
                    Network.connection?.sendTCP(message)
                    Network.connection = null
                    this@AcceptDialog.closeDialog()
                }
            })
            acceptTable.add(btnNo)

            acceptWin.add(acceptTable)
 //           optionsWin.debugAll()
            acceptWin.pack()

        }
    }

}