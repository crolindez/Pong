package es.carlosrolindez.pong

import com.badlogic.gdx.Game

class PongGame : Game() {

    override fun create() {
        setScreen(PongScreen())
    }
    // TODO avoid copy of players on Player list (after rejecting a connection)
    // TODO fix issue with client disconnection when closing game
}