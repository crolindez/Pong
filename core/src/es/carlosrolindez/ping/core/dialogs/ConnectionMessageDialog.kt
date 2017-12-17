package es.carlosrolindez.ping.core.dialogs

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.utils.Assets


class ConnectionMessageDialog(gameScreen: PingScreen) : BaseDialog(gameScreen , 0.5f) {
    private var ui : AcceptUI
    lateinit private var message : String

    init {
        ui = AcceptUI(Assets.instance.skin)
    }

    internal fun openDialog(message : String) {
        this.message = message
        super.openDialog()
    }

    override fun prepareUi() { // load Settings
        ui.label.setText(Assets.instance.stringBundle.format("tryConnect",message))
    }

    override fun closeUi() { // save Setting

    }

    override fun getUiActor() : Actor {
        return ui.acceptWin
    }


    inner class AcceptUI(skin : Skin) {
        internal var acceptWin = Window(Assets.instance.stringBundle.format("message"),skin)

        private var acceptTable = Table()

        internal val label = Label("", skin, "font", Color.BLACK)

        init {

            acceptTable.add(label).colspan(2).padBottom(10f)
            acceptTable.row()

            acceptWin.add(acceptTable)
 //           optionsWin.debugAll()
            acceptWin.pack()

        }
    }

}