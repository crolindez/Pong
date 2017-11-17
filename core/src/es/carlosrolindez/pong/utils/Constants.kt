package es.carlosrolindez.pong.utils

import com.badlogic.gdx.graphics.Color


internal val SCREEN_WIDTH = 250f
internal val SCREEN_HEIGHT = 150f
internal val BACKGROUND_COLOR = Color.BLACK // Color.SKY
internal val MARGIN = 10f

internal val FLASH_TIME = 0.2f
internal val INTRO_TIME = 3f
internal val REINTRO_TIME = 1.5f
internal val PADDLE_HIT_FRAME_DURATION = 0.01f

internal val PADDLE_WIDTH = 4f
internal val PADDLE_HEIGHT = 20f
internal val PADDLE_POSITION_LEFT = 30f
internal val PADDLE_POSITION_RIGHT = SCREEN_WIDTH - PADDLE_POSITION_LEFT
internal val PADDLE_INITIAL_POSITION_Y = SCREEN_HEIGHT / 2f
internal val PADDLE_EDGE = 2
internal val PADDLE_VELOCITY = 50

internal val BALL_WIDTH = 4f
internal val BALL_HEIGHT = 4f
internal val BALL_INITIAL_POSITION_X = SCREEN_WIDTH / 2f
internal val BALL_INITIAL_POSITION_Y = SCREEN_HEIGHT / 2f
internal val BALL_EDGE = 2

internal val WALL_WIDTH = 2f

internal val BUTTON_MARGIN_X = 4f
internal val BUTTON_MARGIN_Y = 4f
internal val BUTTON_WIDTH   = 12f
internal val BUTTON_HEIGHT  = 25f

internal val PLAYER_TEXT_OFFSET_X = 60f
internal val PLAYER_TEXT_OFFSET_Y = 1f
internal val SCORE_TEXT_OFFSET = 3f
