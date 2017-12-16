package es.carlosrolindez.ping.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils



class GamePreferences private constructor() {
    companion object {
        private var TAG = GamePreferences::class.java.name

        private val PREFERENCES = "pong.prefs"
        private val SOUND_KEY = "sound"
        private val MUSIC_KEY = "music"
        private val VOLUME_SOUND_KEY = "volsound"
        private val VOLUME_MUSIC_KEY = "volmusic"
        private val PLAYER1_NAME_KEY = "name1"
        private val PLAYER2_NAME_KEY = "name2"

        private val DEFAULT_PLAYER_NAME = "Player"
        private val DEFAULT_VOL = 0.2f
        private val DEFAULT_MUSIC_STATE = true
        private val DEFAULT_SOUND_STATE = true

        internal val instance = GamePreferences()
    }

    private val preferences = Gdx.app.getPreferences(PREFERENCES)

    internal var sound : Boolean = DEFAULT_SOUND_STATE
    internal var music : Boolean = DEFAULT_MUSIC_STATE
    internal var volSound : Float = DEFAULT_VOL
    internal var volMusic : Float = DEFAULT_VOL
    internal var player1Name : String = DEFAULT_PLAYER_NAME + " 1"
    internal var player2Name : String = DEFAULT_PLAYER_NAME + " 2"

    internal fun load() {
        sound = preferences.getBoolean(SOUND_KEY, DEFAULT_SOUND_STATE)
        music = preferences.getBoolean(MUSIC_KEY, DEFAULT_MUSIC_STATE)
        volSound = MathUtils.clamp(preferences.getFloat(VOLUME_SOUND_KEY, DEFAULT_VOL), 0.0f, 1.0f)
        volMusic = MathUtils.clamp(preferences.getFloat(VOLUME_MUSIC_KEY, DEFAULT_VOL), 0.0f, 1.0f)
        player1Name = preferences.getString(PLAYER1_NAME_KEY,DEFAULT_PLAYER_NAME + " 1")
        player2Name = preferences.getString(PLAYER2_NAME_KEY,DEFAULT_PLAYER_NAME + " 2")
    }

    internal fun save() {

        preferences.putBoolean(SOUND_KEY,sound)
        preferences.putBoolean(MUSIC_KEY,music)
        preferences.putFloat(VOLUME_SOUND_KEY,volSound)
        preferences.putFloat(VOLUME_MUSIC_KEY,volMusic)
        preferences.putString(PLAYER1_NAME_KEY,player1Name)
        preferences.putString(PLAYER2_NAME_KEY,player2Name)

        preferences.flush()
    }

}