package es.carlosrolindez.pong

import com.badlogic.gdx.Game
import es.carlosrolindez.pong.screens.PongScreen

class PongGame : Game() {

    override fun create() {
        setScreen(PongScreen(this))
    }

}
