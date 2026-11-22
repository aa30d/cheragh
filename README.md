# Cheragh

A tiny, native Android flashlight with no screens, libraries, ads, analytics, or network permission. Tap the app icon to toggle the LED. Add the **Cheragh** 1×1 home-screen widget to do the same; its icon turns yellow while the torch is on. A foreground notification keeps the torch's process alive; tapping it also switches the light off. It is required by Android and may appear briefly even when switching off.

## Compatibility

The minimum is Android 4.3 (API 18). Android 4.3–5.1 (API 18–22) uses the legacy camera API and needs the camera permission at install; support varies by hardware. Android 6+ uses the torch API and needs no camera permission. A device must have a usable LED flash; the camera being used by another app, battery restrictions, thermal limits, or vendor behavior can make the torch unavailable. Android may kill the service or turn the light off. No app can guarantee operation on every Android device. The app has no screen or controls: a transparent activity immediately starts the torch service and finishes. The required foreground notification is the only app-provided system UI, and may be hidden from the notification drawer by Android 13+ notification settings. If the torch is unavailable, it silently stays off.

While the service is running, the widget follows system torch callbacks on Android 6+. When the service is not running, the widget shows off; an independently activated system torch is detected on the next tap. A launcher may display a widget with padding or occupy more than one cell depending on its grid.

## Build

Install JDK 17 and Android SDK platform 36/build-tools 36.0.0, set `ANDROID_HOME`, then run:

```sh
./gradlew assembleDebug
```

The installable debug APK is `app/build/outputs/apk/debug/app-debug.apk`. `./gradlew assembleRelease` creates a smaller, unsigned APK unless signing variables are set. The Build workflow builds both and uploads them as an artifact; only the debug APK is installable as-is. Real flashlight testing requires physical devices; an emulator generally has no LED.

## Signed GitHub releases

Create a keystore once (for example using `keytool -genkeypair -v -keystore release.jks -alias cheragh -keyalg RSA -keysize 2048 -validity 10000`) and keep a secure backup. **Never commit the key**. Configure these GitHub Actions repository secrets:

* `KEYSTORE_BASE64`: the entire keystore encoded as base64 (e.g. `base64 -w 0 release.jks` on Linux).
* `KEYSTORE_PASSWORD`: keystore password.
* `KEY_ALIAS`: signing key alias.
* `KEY_PASSWORD`: key password.

Push a `v*` tag, e.g. `v1.0`, or run the Release workflow manually with an existing tag. It checks out that tag, builds and verifies a signed APK, and creates or updates the GitHub release with `cheragh.apk`. Keep using the same signing key for every release; Android will reject an update signed with a different key. Increment `versionCode` in `app/build.gradle` for each update. Release signing can also be used locally via `KEYSTORE_PATH` (absolute path), `KEYSTORE_PASSWORD`, `KEY_ALIAS`, and `KEY_PASSWORD` environment variables.

The project is designed to be small, but no claim is made that its APK is literally the world's smallest.
