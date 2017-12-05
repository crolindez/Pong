package es.carlosrolindez.pong.dialogs

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.pong.screens.PongScreen
import es.carlosrolindez.pong.utils.Assets


class NetworkStage(private val gameScreen: PongScreen) : BaseDialog(gameScreen , 0.5f) {
    private var ui : NetworkUI

    init {
        ui = NetworkUI(Assets.instance.skin)
    }

    override fun prepareUi() { // load Settings
        gameScreen.netServer.stop()
        gameScreen.netClient.start()
    }

    override fun closeUi() { // save Setting
        gameScreen.netClient.stop()
        gameScreen.netServer.start()
    }

    override fun getUiActor() : Actor {
        return ui.networkWin
    }


    inner class NetworkUI(skin : Skin) {
        internal var networkWin = Window("Network",skin)

        private var networkTable = Table()

        private  val btnNo: TextButton
        private  val btnOk: TextButton

        init {

            networkTable.add(Label("Do you accept to play in network?", skin, "font", Color.BLACK)).colspan(2).padBottom(10f)
            networkTable.row()

            btnOk = TextButton("Ok", skin)
            btnOk.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@NetworkStage.closeDialog()
                }
            })
            networkTable.add(btnOk)

            btnNo = TextButton("No", skin)
            btnNo.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@NetworkStage.closeDialog()
                }
            })
            networkTable.add(btnNo)

            networkWin.add(networkTable)
 //           optionsWin.debugAll()
            networkWin.pack()

        }
    }

}