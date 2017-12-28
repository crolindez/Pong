package es.carlosrolindez.ping.core.dialogs

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.utils.Assets


class VersionMessageDialog(pingScreen: PingScreen) : BaseDialog(pingScreen , 0.5f, 2) {
    private var ui : AcceptUI

    init {
        ui = AcceptUI(Assets.skin)
    }


    override fun prepareUi() { // load Settings
        ui.label.setText(Assets.stringBundle.format("availableVersion"))
    }

    override fun closeUi() { // save Setting

    }

    override fun getUiActor() : Actor {
        return ui.acceptWin
    }


    inner class AcceptUI(skin : Skin) {
        internal var acceptWin = Window(Assets.stringBundle.format("message"),skin)

        private var acceptTable = Table()

        private  val btnOk: TextButton

        internal val label = Label("", skin, "font", Color.BLACK)

        init {

            acceptTable.add(label).colspan(2).padBottom(30f)
            acceptTable.row()

            btnOk = TextButton("Ok", skin)
            btnOk.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@VersionMessageDialog.closeDialog()
                }
            })
            acceptTable.add(btnOk).colspan(2).padBottom


            acceptWin.add(acceptTable)
 //           optionsWin.debugAll()
            acceptWin.pack()

        }
    }

}