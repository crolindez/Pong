package es.carlosrolindez.pong

import com.badlogic.gdx.Game
import es.carlosrolindez.pong.screens.PongScreen

class PongGame : Game() {

    override fun create() {
        setScreen(PongScreen(this))
    }

}


// TODO Sound for fireworks
// TODO GAME OVER WITH NAMES
// TODO Save setting
// TODO Name of players
// TODO button to exit from gameOver