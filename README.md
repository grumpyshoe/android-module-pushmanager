
# Pushmanager

![minSDK 16](https://img.shields.io/badge/minSDK-API_16-green.svg?style=flat)
  ![targetSDK 28](https://img.shields.io/badge/targetSDK-API_28-blue.svg)

`Pushmanager` is a small wrapper for [FCM (Firebase Cloud Messageing)](http://https://firebase.google.com/docs/cloud-messaging/ "FCM (Firebase Cloud Messageing)") and your app needs just a few methods to interact with it.

## Installation

Add google-services as dependency to your project `build.gradle`
```gradle
buildscript {
    ...
    dependencies {
        ...
        classpath "com.google.gms:google-services:4.3.5"
        ...
    }
}
```

and add `jitpack`to your repositories
```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
    ...
}
```

Add this dependency to your app _build.gradle_ and apply the plugin at the bottom:
```gradle
implementation 'com.github.grumpyshoe:android-module-pushmanager:1.3.1'
```
```gradle
...
apply plugin: 'com.google.gms.google-services'
```

## Usage

Get instance of PushManager:
```kotlin
val pushmanager: PushManager = PushManagerImpl  
```

Put your `google-services.json` to the app-root folder.


Create a class extending `PushmanagerMessagingService` and implement `handleNotificationPayload`. Here you can decide if a notification should be silent handled or shown to the user according to it's reason.
```kotlin
class MyService : PushmanagerMessagingService() {

    override fun handleNotificationPayload(context:Context, remoteMessageData: RemoteMessageData): NotificationData? {

      Log.d("PushManager", "handlePayload - ${remoteMessageData.title} - ${remoteMessageData.body}" )

      // create pending intent (example)
      val notificationIntent = Intent(context, SomeActivity::class.java)
      notificationIntent.putExtra("info", "Some information for pending intent")
      notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
      val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

      // create notification (example)
      return NotificationData(
            context = context,
            title = remoteMessageData.title ?: "Default Title",
            message = remoteMessageData.body ?: "Default Message",
            channelId = "channel_global_notifications",             // needed SDK >= Android O
            autoCancel = true,
            pendingIntent = contentIntent)
    }
}
```
Add your implementation to you `Manifest.xml`
```
<application ... >

       <service
           android:name=".MyService">
           <intent-filter>
               <action android:name="com.google.firebase.MESSAGING_EVENT"/>
           </intent-filter>
       </service>
       ...

</application>
```

### Register to FCM

Call the method `register` in your `onCreate` to register to FCM. Notice that `register` is a coroutine so you have to use something like `GlobalScope.launch` to run this function.
```kotlin
pushmanager.register(
      context = this,
      onTokenReceived = { token ->
          Log.d("PushManager", "token received: $token")
      },
      onFailure = { exception ->
          Log.d("PushManager", " error during registration: ${exception?.message}")
      })
```


### Unregister from FCM
To unregister from FCM you need to register first. Notice that `unregister` is a coroutine so you have to use something like `GlobalScope.launch` to run this function.
```kotlin
pushmanager.unregister(context)
```


### Topics


#### Subscribe to Topic

To subscribe to a topic just call  `subscriptToTopic`:

```kotlin
pushmanager.subscriptToTopic(
      topic = "wurst",
      onSuccess = {
          Log.d("PushManager", "successfully subscribed")
      },
      onFailure = { exception ->
          Log.d("PushManager", " error while subscribing: ${exception?.message}")
      })

```


#### Unsubscribe from Topic

To unsubscribe from a topic call  `unsubscriptFromTopic`:

```kotlin
pushmanager.unsubscriptFromTopic(
      topic = "wurst",
      onSuccess = {
          Log.d("PushManager", "successfully unsubscribed")
      },
      onFailure = { exception ->
          Log.d("PushManager", "error while unsubscribing: ${exception?.message}")
      })

```


### Dependencies
| Package  | Version  |
| ------------ | ------------ |
| com.google.firebase:firebase-core | 17.2.0  |
| com.google.firebase:firebase-iid  | 20.0.0  |
| com.google.firebase:firebase-messaging  | 20.0.0  |


## Sample App

To run the sample App, just replace the `application_id` at the project `build.gradle` with someone according to your firebase project and add your `google-services.json` to to app root folder.

## Troubleshooting
See [Troubleshooting](https://github.com/grumpyshoe/android-module-pushmanager/wiki) at github wiki.


## Need Help or something missing?

Please [submit an issue](https://github.com/grumpyshoe/android-module-pushmanager/issues) on GitHub.


## Changelog

See [CHANGELOG](CHANGELOG.md) for morre information.


## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file.


## Other information


#### Build Environment
```
Android Studio 4.1.2
Build #AI-201.8743.12.41.7042882, built on December 20, 2020
Runtime version: 1.8.0_242-release-1644-b3-6915495 x86_64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
macOS 10.15.5
```
