package es.carlosrolindez.pong.utils

import com.badlogic.gdx.graphics.Color


internal val SCREEN_WIDTH = 250f
internal val SCREEN_HEIGHT = 150f
internal val OPTION_SCREEN_WIDTH = SCREEN_WIDTH * 5f
internal val OPTION_SCREEN_HEIGHT = SCREEN_HEIGHT * 6f

internal val BACKGROUND_COLOR = Color.BLACK // Color.SKY
internal val MARGIN = 20f

internal val GAMEOVER_SCORE = 2

internal val LOADING_TIME = 1f
internal val INTRO_TIME = 4f
internal val REINTRO_TIME = 1.5f
internal val ANIMATION_FRAME_DURATION = 0.01f

internal val PADDLE_WIDTH = 4f
internal val PADDLE_HEIGHT = 20f
internal val PADDLE_POSITION_LEFT = 40f
internal val PADDLE_POSITION_RIGHT = SCREEN_WIDTH - PADDLE_POSITION_LEFT
internal val PADDLE_INITIAL_POSITION_Y = SCREEN_HEIGHT / 2f
internal val PADDLE_VELOCITY = 50

internal val BALL_WIDTH = 4f
internal val BALL_HEIGHT = 4f
internal val BALL_INITIAL_POSITION_X = SCREEN_WIDTH / 2f
internal val BALL_INITIAL_POSITION_Y = SCREEN_HEIGHT / 2f

internal val WALL_WIDTH = 2f

internal val BUTTON_MARGIN_X = 6f
internal val BUTTON_MARGIN_Y = 6f
internal val BUTTON_WIDTH   = 15f
internal val BUTTON_HEIGHT  = 30f
internal val BUTTON_AUTO_MARGIN_X = 3f
internal val BUTTON_AUTO_MARGIN_Y = 45f
internal val BUTTON_AUTO_WIDTH = 21f
internal val BUTTON_AUTO_HEIGHT = 8f

internal val PLAYER_TEXT_OFFSET_X = 60f
internal val PLAYER_TEXT_OFFSET_Y = 6f
internal val SCORE_TEXT_OFFSET = 3f

internal val BOTTOM_BUTTONS_OFFSET_Y = 2f
internal val SETTINGS_OFFSET_X = -75f
internal val MUSIC_OFFSET_X = 0f
internal val SOUND_OFFSET_X = 75f
internal val SETTING_BUTTON_WIDTH = 10f
internal val SETTING_BUTTON_HEIGHT = 11f

internal val WINNER_MESSAGE = " wins"

internal val MUSIC_VOLUME = 0.2f
internal val SOUND_VOLUME = 0.5f