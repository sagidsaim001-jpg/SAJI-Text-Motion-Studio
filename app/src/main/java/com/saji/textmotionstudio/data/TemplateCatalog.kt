package com.saji.textmotionstudio.data

object TemplateCatalog {
    val templates = listOf(
        TextTemplate("kinetic-punch", "Kinetic Punch", "Kinetic", "A bold punch-in with a crisp landing.", MotionStyle.POP, 0xFFB69CFF, 1050),
        TextTemplate("word-cascade", "Word Cascade", "Captions", "Words enter in a quick cascading rhythm.", MotionStyle.STAGGER, 0xFF67D7F0, 1500),
        TextTemplate("split-impact", "Split Impact", "Kinetic", "A sharp split-style reveal from the center.", MotionStyle.SPLIT, 0xFFFF9C72, 1250),
        TextTemplate("soft-rise", "Soft Rise", "Cinematic", "Smooth upward movement with a gentle fade.", MotionStyle.RISE, 0xFFB8A6FF, 1700),
        TextTemplate("axis-flip", "Axis Flip", "3D", "Text flips forward into a clean position.", MotionStyle.FLIP, 0xFF8FE0D0, 1550),
        TextTemplate("perspective-tilt", "Perspective Tilt", "3D", "A tilted 3D entrance with subtle depth.", MotionStyle.TILT, 0xFFFFA8CB, 1600),
        TextTemplate("tracking-expand", "Tracking Expand", "Kinetic", "Letter spacing opens as the title settles.", MotionStyle.TRACKING, 0xFFFFD27D, 1800),
        TextTemplate("typewriter-pro", "Typewriter Pro", "Captions", "A clean character-by-character reveal.", MotionStyle.TYPEWRITER, 0xFF9DE3FF, 1900),
        TextTemplate("signal-glitch", "Signal Glitch", "Social", "A short digital jitter for punchy edits.", MotionStyle.GLITCH, 0xFF89F0E0, 1150),
        TextTemplate("wave-flow", "Wave Flow", "Kinetic", "Text floats through a smooth wave motion.", MotionStyle.WAVE, 0xFFB8A1FF, 1700),
        TextTemplate("vertical-roll", "Vertical Roll", "3D", "A rolling entrance with a cinematic finish.", MotionStyle.ROLL, 0xFFFFB28D, 1500),
        TextTemplate("pulse-beat", "Pulse Beat", "Social", "A soft repeating pulse for beat-led edits.", MotionStyle.PULSE, 0xFFFF8FC2, 900),
        TextTemplate("elastic-bounce", "Elastic Bounce", "Kinetic", "A springy bounce with a soft landing.", MotionStyle.BOUNCE, 0xFF9FE6C3, 1250),
        TextTemplate("cinema-fade", "Cinema Fade", "Cinematic", "A slow, polished fade into focus.", MotionStyle.FADE, 0xFFC2C6FF, 1900),
        TextTemplate("gravity-drop", "Gravity Drop", "Kinetic", "Drops into frame with controlled momentum.", MotionStyle.DROP, 0xFFFFC17A, 1300),
        TextTemplate("dream-float", "Dream Float", "Cinematic", "A light floating motion for calm visuals.", MotionStyle.FLOAT, 0xFF9EDCFF, 2000),
        TextTemplate("wide-stretch", "Wide Stretch", "Kinetic", "Expands horizontally into a bold title.", MotionStyle.STRETCH, 0xFFE5A7FF, 1350),
        TextTemplate("echo-trails", "Echo Trails", "Social", "Layered echoes create a sense of speed.", MotionStyle.ECHO, 0xFFFFA0B7, 1450),
        TextTemplate("orbit-spin", "Orbit Spin", "3D", "A compact spin that lands front and center.", MotionStyle.SPIN, 0xFF9CDBD3, 1650),
        TextTemplate("snap-focus", "Snap Focus", "Cinematic", "A quick snap into a steady title.", MotionStyle.SNAP, 0xFFFFD18E, 1000),
        TextTemplate("caption-karaoke", "Karaoke Highlight", "Captions", "A moving highlight for lyric-style captions.", MotionStyle.KARAOKE, 0xFF9CE7B0, 1600),
        TextTemplate("micro-zoom", "Micro Zoom", "Cinematic", "A subtle camera-like zoom for clean edits.", MotionStyle.ZOOM, 0xFFAFC5FF, 1800),
        TextTemplate("neon-drift", "Neon Drift", "Social", "A smooth drift with a bright accent.", MotionStyle.FLOAT, 0xFF7CE9FF, 1750),
        TextTemplate("3d-word-rise", "3D Word Rise", "3D", "A dimensional rise with a tilted landing.", MotionStyle.FLIP, 0xFFFFA7D4, 1450),
        TextTemplate("rhythm-stagger", "Rhythm Stagger", "Captions", "A rhythmic stagger for short-form captions.", MotionStyle.STAGGER, 0xFFB9F08C, 1150),
        TextTemplate("prism-split", "Prism Split", "3D", "A wide reveal with a bright color accent.", MotionStyle.SPLIT, 0xFF9CB8FF, 1500),
        TextTemplate("soft-pop", "Soft Pop", "Social", "A minimal pop that stays easy to read.", MotionStyle.POP, 0xFFFFB6A0, 1400),
        TextTemplate("slow-motion", "Slow Motion", "Cinematic", "A slow rise designed for dramatic pauses.", MotionStyle.RISE, 0xFFB7C4FF, 2200),
        TextTemplate("letter-wave", "Letter Wave", "Kinetic", "A smooth wave-like text movement.", MotionStyle.WAVE, 0xFF8BE3D5, 1350),
        TextTemplate("flash-type", "Flash Type", "Captions", "Fast typing for energetic subtitle moments.", MotionStyle.TYPEWRITER, 0xFFFFC27D, 1050),
        TextTemplate("tilt-swipe", "Tilt Swipe", "3D", "A tilted entrance that feels like a camera move.", MotionStyle.TILT, 0xFFB4A3FF, 1250),
        TextTemplate("elastic-title", "Elastic Title", "Kinetic", "A playful stretch and settle.", MotionStyle.STRETCH, 0xFFFFA8BD, 1600),
        TextTemplate("glitch-cut", "Glitch Cut", "Social", "A punchy digital cut with slight jitter.", MotionStyle.GLITCH, 0xFF8FE9FF, 950),
        TextTemplate("word-focus", "Word Focus", "Captions", "A focused zoom for one key phrase.", MotionStyle.ZOOM, 0xFFFFD88F, 1500),
        TextTemplate("halo-pulse", "Halo Pulse", "Cinematic", "A restrained pulse for a polished finish.", MotionStyle.PULSE, 0xFFB7A4FF, 1750),
        TextTemplate("spin-caption", "Spin Caption", "Social", "A quick rotation for energetic titles.", MotionStyle.ROLL, 0xFF9AE3CE, 1200)
    )

    val categories = listOf("All", "Kinetic", "3D", "Captions", "Cinematic", "Social")
}
