package es.carlosrolindez.pong

import com.badlogic.gdx.Game
import es.carlosrolindez.pong.screens.PongScreen

class PongGame : Game() {

    override fun create() {
        setScreen(PongScreen(this))
    }

}



// TODO button to exit from gameOver
// TODO button to pause (and pause when enter in configuration
// TODO improve up/down buttons