package es.carlosrolindez.pong

import com.badlogic.gdx.Game
import es.carlosrolindez.pong.net.NetworkServer
import es.carlosrolindez.pong.screens.PongScreen

class PongGame : Game() {

    override fun create() {
        NetworkServer()
        setScreen(PongScreen())
    }

}