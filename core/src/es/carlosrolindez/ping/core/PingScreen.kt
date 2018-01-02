package es.carlosrolindez.ping.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.async.AsyncExecutor
import com.badlogic.gdx.utils.async.AsyncResult
import com.badlogic.gdx.utils.async.AsyncTask
import es.carlosrolindez.ping.core.dialogs.*
import es.carlosrolindez.ping.core.net.Network
import es.carlosrolindez.ping.core.net.NetworkClient
import es.carlosrolindez.ping.core.net.NetworkServer
import es.carlosrolindez.ping.core.overlays.GUIOverlay
import es.carlosrolindez.ping.core.utils.*
import org.jsoup.Jsoup


class PingScreen :ScreenAdapter() {

    companion object {
        val TAG: String = PingScreen::class.java.name
    }

    lateinit private var spriteBatch : SpriteBatch

    private var netServer = NetworkServer(this)
    internal var netClient = NetworkClient(this)

    private val executor = AsyncExecutor(5)
    private var answer : AsyncResult<String?>? = null

    lateinit internal var opponentName : String

    lateinit internal var level : Level
    lateinit internal var gui : GUIOverlay

    lateinit internal var configurationDialog: ConfigurationDialog
    lateinit internal var playerListDialog: PlayerListDialog
    lateinit internal var acceptDialog : AcceptDialog
    lateinit internal var connectionMessageDialog: ConnectionMessageDialog
    lateinit internal var versionMessageDialog: VersionMessageDialog
    lateinit internal var helpDialog : HelpDialog

    internal var versionAvailable = false

    internal var scorePlayer1 = 0
    internal var scorePlayer2 = 0
    internal val gameover : Boolean
            get() = (scorePlayer1>= GAMEOVER_SCORE || scorePlayer2>= GAMEOVER_SCORE)
    internal var paused = false

    lateinit private var listOfDialogs : List<BaseDialog>

    internal fun hasPriority(dialog : BaseDialog): Boolean {
        return listOfDialogs.none{ it.activated && dialog.priority<it.priority }
    }

    internal fun closeLessPriorityDialogs(dialog : BaseDialog) {
        listOfDialogs.filter { it.activated && dialog.priority>it.priority && it!=dialog}.forEach {
            it.closeDialog() }
    }

    override fun show() {
        NetworkServer(this)
        netServer.start()
        netClient.start()
        spriteBatch = SpriteBatch()
        Assets.initialize()
        opponentName = Assets.stringBundle.format("player")
        GamePreferences.load()
        level = Level(this)
        gui = GUIOverlay(this)
        configurationDialog = ConfigurationDialog(this)
        playerListDialog = PlayerListDialog(this)
        acceptDialog = AcceptDialog(this)
        connectionMessageDialog = ConnectionMessageDialog(this)
        versionMessageDialog = VersionMessageDialog(this)
        helpDialog = HelpDialog(this)
        listOfDialogs = listOf(configurationDialog,
                playerListDialog,
                acceptDialog,
                connectionMessageDialog,
                versionMessageDialog,
                helpDialog)

        Gdx.app.input.inputProcessor = gui


        answer = executor.submit {
            try {
                Jsoup.connect("https://play.google.com/store/apps/details?id=es.carlosrolindez.ping")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText()
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun render(delta: Float) {
        gui.update(delta)
        if (!paused) level.update(delta)
        configurationDialog.update(delta)
        playerListDialog.update(delta)
        acceptDialog.update(delta)
        connectionMessageDialog.update(delta)
        versionMessageDialog.update(delta)
        helpDialog.update(delta)

        if (answer?.isDone == true) {
            if (answer?.get()?.equals(VERSION) == false) {
                versionAvailable = true
                if (hasPriority(versionMessageDialog)) {
                    closeLessPriorityDialogs(versionMessageDialog)
                    if (!paused) {
                        if (Network.connection==null) {
                            paused = true
                        } else {
                            Network.pause()
                        }
                    }
                    versionMessageDialog.openDialog()
                    answer = null
                }

            } else answer = null
        }


        Gdx.gl.glClearColor(BACKGROUND_COLOR.r,
                BACKGROUND_COLOR.g,
                BACKGROUND_COLOR.b,
                BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        gui.render(spriteBatch)
        level.render(spriteBatch)
        configurationDialog.render(spriteBatch)
        playerListDialog.render(spriteBatch)
        acceptDialog.render(spriteBatch)
        connectionMessageDialog.render(spriteBatch)
        versionMessageDialog.render(spriteBatch)
        helpDialog.render(spriteBatch)

    }

    override fun resize(width: Int, height: Int) {
        level.resize(width, height)
        gui.resize(width, height)
        configurationDialog.resize(width, height)
        playerListDialog.resize(width, height)
        acceptDialog.resize(width, height)
        connectionMessageDialog.resize(width, height)
        versionMessageDialog.resize(width, height)
        helpDialog.resize(width, height)
    }

    override fun dispose() {
        Assets.dispose()
        spriteBatch.dispose()
        level.dispose()
        gui.dispose()
        configurationDialog.dispose()
        playerListDialog.dispose()
        acceptDialog.dispose()
        connectionMessageDialog.dispose()
        versionMessageDialog.dispose()
        helpDialog.dispose()

        netServer.dispose()
        netClient.dispose()

        executor.dispose()
    }


    override fun hide() {
        dispose()
    }


}