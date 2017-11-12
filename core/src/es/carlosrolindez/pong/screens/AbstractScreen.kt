package es.carlosrolindez.pong.screens

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.ScreenAdapter


abstract class AbstractScreen : ScreenAdapter() {
    abstract var inputProcessor: InputProcessor?
}
