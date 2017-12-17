package es.carlosrolindez.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.async.AsyncExecutor
import com.badlogic.gdx.utils.async.AsyncResult
import com.badlogic.gdx.utils.async.AsyncTask
import es.carlosrolindez.ping.dialogs.AcceptDialog
import es.carlosrolindez.ping.dialogs.ConfigurationDialog
import es.carlosrolindez.ping.dialogs.MessageDialog
import es.carlosrolindez.ping.dialogs.PlayerListDialog
import es.carlosrolindez.ping.net.NetworkClient
import es.carlosrolindez.ping.net.NetworkServer
import es.carlosrolindez.ping.overlays.GUIOverlay
import es.carlosrolindez.ping.utils.Assets
import es.carlosrolindez.ping.utils.BACKGROUND_COLOR
import es.carlosrolindez.ping.utils.GAMEOVER_SCORE
import es.carlosrolindez.ping.utils.GamePreferences
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

    lateinit internal var level : es.carlosrolindez.core.Level
    lateinit internal var gui : GUIOverlay
    lateinit internal var configurationDialog: ConfigurationDialog
    lateinit internal var playerListDialog: PlayerListDialog
    lateinit internal var acceptDialog : AcceptDialog
    lateinit internal var messageDialog : MessageDialog

    internal var scorePlayer1 = 0
    internal var scorePlayer2 = 0
    internal val gameover : Boolean
            get() = (scorePlayer1>= GAMEOVER_SCORE || scorePlayer2>= GAMEOVER_SCORE)
    internal var paused = false


    override fun show() {
        NetworkServer(this)
        netServer.start()
        netClient.start()
        spriteBatch = SpriteBatch()
        Assets.instance.initialize()
        opponentName = Assets.instance.stringBundle.format("player")
        GamePreferences.instance.load()
        level = es.carlosrolindez.core.Level(this)
        gui = GUIOverlay(this)
        configurationDialog = ConfigurationDialog(this)
        playerListDialog = PlayerListDialog(this)
        acceptDialog = AcceptDialog(this)
        messageDialog = MessageDialog(this)
        Gdx.app.input.inputProcessor = gui


        answer = executor.submit(object : AsyncTask<String?> {
            override fun call(): String? {
                var newVersion: String?
                try {
                    newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=es.carlosrolindez.ping")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select("div[itemprop=softwareVersion]")
                            .first()
                            .ownText()
                    return newVersion
                } catch (e: Exception) {
                    return null
                }

            }
        })
    }

    override fun render(delta: Float) {
        gui.update(delta)
        if (!paused) level.update(delta)
        configurationDialog.update(delta)
        playerListDialog.update(delta)
        acceptDialog.update(delta)
        messageDialog.update(delta)

        if (answer?.isDone?:false) {
            Gdx.app.error(TAG,answer?.get())
            answer = null
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
        messageDialog.render(spriteBatch)

    }

    override fun resize(width: Int, height: Int) {
        level.resize(width, height)
        gui.resize(width, height)
        configurationDialog.resize(width, height)
        playerListDialog.resize(width, height)
        acceptDialog.resize(width, height)
        messageDialog.resize(width, height)
    }

    override fun dispose() {
        Assets.instance.dispose()
        spriteBatch.dispose()
        level.dispose()
        gui.dispose()
        configurationDialog.dispose()
        playerListDialog.dispose()
        acceptDialog.dispose()
        messageDialog.dispose()
        netServer.dispose()
        netClient.dispose()

        executor.dispose()
    }


    override fun hide() {
        dispose()
    }


}