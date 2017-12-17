package es.carlosrolindez.ping.core.dialogs

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.utils.Assets
import es.carlosrolindez.ping.core.utils.DIALOG_SCREEN_WIDTH
import java.net.InetAddress


class PlayerListDialog(private val gameScreen: PingScreen) : BaseDialog(gameScreen , 0.5f) {
    private var ui : AcceptUI
    private var serverList = mutableListOf<InetAddress>()
    private val players = com.badlogic.gdx.utils.Array<String>()

    init {
        ui = AcceptUI(Assets.instance.skin)
    }

    override fun prepareUi() { // load Settings
  //      gameScreen.netServer.dispose()
        val localServerList  = gameScreen.netClient.getServerList()
        serverList.clear()
        localServerList.let {serverList.addAll(it)}
        players.clear()
        for (serverAddress : InetAddress in serverList) {
            players.add(serverAddress.canonicalHostName)
        }
        ui.playerList.setItems(players)
    }

    override fun closeUi() { // save Setting
 //       gameScreen.netClient.stop()
 //      gameScreen.netServer.start()
    }

    override fun getUiActor() : Actor {
        return ui.playerListWin
    }


    inner class AcceptUI(skin : Skin) {
        internal var playerListWin = Window(Assets.instance.stringBundle.format("network"),skin)

        private var playerListTable = Table()
        internal val playerList = com.badlogic.gdx.scenes.scene2d.ui.List<String>(skin)



        private  val btnNo: TextButton
        private  val btnOk: TextButton

        init {

            playerListTable.add(Label(Assets.instance.stringBundle.format("selectPlayer"), skin, "font", Color.BLACK)).colspan(2).padBottom(20f)
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
                    if (selectedIndex>-1)
                        gameScreen.netClient.connect(serverList[selectedIndex])
                    this@PlayerListDialog.closeDialog()
                    gameScreen.connectionMessageDialog.openDialog(serverList[selectedIndex].canonicalHostName)
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
    }

}