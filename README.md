# SAJI Text Motion Studio

A native Android text-motion template studio built with Kotlin and Jetpack Compose.

## Template gallery

The app includes 36 built-in animated text presets across five groups:

- Kinetic
- 3D
- Captions
- Cinematic
- Social

## Included now

- Premium dark template gallery.
- Live animated preview on every template card.
- Search by template name or description.
- Category filters.
- Full-screen preview for each template.
- Editable custom text that updates the preview live.
- "Use this template" action that selects the chosen preset.
- All animations are native Compose animations and work without downloading template packs.

## Build

Open the project in Android Studio with JDK 17 and Android SDK 35.

The GitHub Actions workflow builds a debug APK using Gradle 8.9:

```bash
gradle assembleDebug
```

## Next stages

The current implementation is the template gallery and preview foundation. Integration with a full timeline, saved presets, keyframes, audio beat sync, and video export will be added in later stages.


## TXTFX-inspired Android presets

The app includes 22 additional Android-native motion presets inspired by the supplied TXTFX preview set. The original After Effects CEP extension and AEP project are not executed on Android; these presets are recreated with Jetpack Compose animations. The original package identifies source compositions as TXT 1 through TXT 22, so the app uses descriptive names for the recreated styles rather than claiming an exact one-to-one conversion.
