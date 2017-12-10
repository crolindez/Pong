package es.carlosrolindez.pong.dialogs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.pong.PongScreen
import es.carlosrolindez.pong.utils.Assets
import es.carlosrolindez.pong.utils.DIALOG_SCREEN_WIDTH
import java.net.InetAddress


class PlayerListDialog(private val gameScreen: PongScreen) : BaseDialog(gameScreen , 0.5f) {
    private var ui : AcceptUI
    private var serverList = mutableListOf<InetAddress>()

    init {
        ui = AcceptUI(Assets.instance.skin)
    }

    override fun prepareUi() { // load Settings
  //      gameScreen.netServer.dispose()
        val localServerList  = gameScreen.netClient.getServerList()
        localServerList.let {serverList.addAll(it)}
        ui.resetList()
        for (serverAddress : InetAddress in serverList)
            ui.updateList(serverAddress.canonicalHostName)
    }

    override fun closeUi() { // save Setting
 //       gameScreen.netClient.stop()
 //      gameScreen.netServer.start()
    }

    override fun getUiActor() : Actor {
        return ui.playerListWin
    }


    inner class AcceptUI(skin : Skin) {
        internal var playerListWin = Window("Network",skin)

        private var playerListTable = Table()
        private val playerList = com.badlogic.gdx.scenes.scene2d.ui.List<String>(skin)

        private val players = com.badlogic.gdx.utils.Array<String>()

        private  val btnNo: TextButton
        private  val btnOk: TextButton

        init {

            playerListTable.add(Label("Select player:", skin, "font", Color.BLACK)).colspan(2).padBottom(20f)
            playerListTable.row()

            playerList.setItems(players)

            val scrollPane = ScrollPane(playerList,skin)
            scrollPane.setFadeScrollBars(false)
            scrollPane.setScrollingDisabled(true, false)

            playerListTable.add(scrollPane).colspan(2).width(DIALOG_SCREEN_WIDTH * ConfigurationDialog.scale /2f).padBottom(20f)
            playerListTable.row()

            btnOk = TextButton("Ok", skin)
            btnOk.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    val selectedIndex = playerList.selectedIndex
                    Gdx.app.error("Selected: ","" + selectedIndex)
                    if (selectedIndex>-1)
                        gameScreen.netClient.connect(serverList[selectedIndex])
                    this@PlayerListDialog.closeDialog()
                }
            })
            playerListTable.add(btnOk).padBottom(20f)

            btnNo = TextButton("No", skin)
            btnNo.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@PlayerListDialog.closeDialog()
                }
            })
            playerListTable.add(btnNo).padBottom(20f)

            playerListWin.add(playerListTable)
            //           optionsWin.debugAll()
            playerListWin.pack()


        }

        fun updateList(name : String) {
            players.add(name)
            playerList.setItems(players)
        }

        fun resetList() {
            players.clear()
            playerList.setItems(players)
        }

    }

}