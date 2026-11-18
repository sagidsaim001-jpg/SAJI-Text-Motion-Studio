package com.saji.textmotionstudio.data

enum class MotionStyle {
    POP, RISE, FLIP, TILT, TRACKING, TYPEWRITER, GLITCH, WAVE,
    ROLL, PULSE, BOUNCE, FADE, DROP, FLOAT, STRETCH, ECHO,
    SPIN, SNAP, STAGGER, SPLIT, KARAOKE, ZOOM
}

data class TextTemplate(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val motion: MotionStyle,
    val accent: Long,
    val durationMs: Int = 1400
)
