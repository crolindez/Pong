package es.carlosrolindez.pong

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import es.carlosrolindez.pong.dialogs.AcceptDialog
import es.carlosrolindez.pong.dialogs.ConfigurationDialog
import es.carlosrolindez.pong.dialogs.MessageDialog
import es.carlosrolindez.pong.dialogs.PlayerListDialog
import es.carlosrolindez.pong.net.NetworkClient
import es.carlosrolindez.pong.net.NetworkServer
import es.carlosrolindez.pong.overlays.GUIOverlay
import es.carlosrolindez.pong.utils.Assets
import es.carlosrolindez.pong.utils.BACKGROUND_COLOR
import es.carlosrolindez.pong.utils.GAMEOVER_SCORE
import es.carlosrolindez.pong.utils.GamePreferences


class PongScreen :ScreenAdapter() {

    companion object {
        val TAG: String = PongScreen::class.java.name
    }

    lateinit private var spriteBatch : SpriteBatch

    private var netServer = NetworkServer(this)
    internal var netClient = NetworkClient(this)

    internal var opponentName : String = "Player"

    lateinit internal var level : Level
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
        GamePreferences.instance.load()
        level = Level(this)
        gui = GUIOverlay(this)
        configurationDialog = ConfigurationDialog(this)
        playerListDialog = PlayerListDialog(this)
        acceptDialog = AcceptDialog(this)
        messageDialog = MessageDialog(this)
        Gdx.app.input.inputProcessor = gui
    }

    override fun render(delta: Float) {
        gui.update(delta)
        if (!paused) level.update(delta)
        configurationDialog.update(delta)
        playerListDialog.update(delta)
        acceptDialog.update(delta)
        messageDialog.update(delta)


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

    }


    override fun hide() {
        dispose()
    }


}