package es.carlosrolindez.pong.dialogs

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.pong.PongScreen
import es.carlosrolindez.pong.utils.Assets


class AcceptDialog(private val gameScreen: PongScreen) : BaseDialog(gameScreen , 0.5f) {
    private var ui : AcceptUI

    init {
        ui = AcceptUI(Assets.instance.skin)
    }

    override fun prepareUi() { // load Settings

    }

    override fun closeUi() { // save Setting

    }

    override fun getUiActor() : Actor {
        return ui.acceptWin
    }


    inner class AcceptUI(skin : Skin) {
        internal var acceptWin = Window("Network",skin)

        private var acceptTable = Table()

        private  val btnNo: TextButton
        private  val btnOk: TextButton

        init {

            acceptTable.add(Label("Do you accept to play in network?", skin, "font", Color.BLACK)).colspan(2).padBottom(10f)
            acceptTable.row()

            btnOk = TextButton("Ok", skin)
            btnOk.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@AcceptDialog.closeDialog()
                }
            })
            acceptTable.add(btnOk)

            btnNo = TextButton("No", skin)
            btnNo.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
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