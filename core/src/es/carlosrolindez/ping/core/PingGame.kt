package es.carlosrolindez.ping.core

import com.badlogic.gdx.Game

class PingGame : Game() {

    override fun create() {

        setScreen(PingScreen())
    }
}