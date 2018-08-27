
# Pushmanager

![AndroidStudio](https://img.shields.io/badge/Android_Studio-3.1.4-brightgreen.svg)
![minSDK](https://img.shields.io/badge/minSDK-API_16-orange.svg?style=flat)

`Pushmanager` is a small wrapper for [FCM (Firebase Cloud Messageing)](http://https://firebase.google.com/docs/cloud-messaging/ "FCM (Firebase Cloud Messageing)") and your app needs just a few methods to interact with it.

## Installation

Add google-services as dependency to your project `build.gradle`
```
buildscript {
    ...
    dependencies {
        ...
        classpath "com.google.gms:google-services:4.0.2"
        ...
    }
}
```


Add this dependency to your app _build.gradle_ and apply the plugin at the bottom:
```
implementation 'com.github.grumpyshoe:android-module-pushmanager:1.0.0'
```
```
...
apply plugin: 'com.google.gms.google-services'
```

## Usage

- Get instance of PushManager:
```
val pushmanager: PushManager = PushManagerImpl  
```

- Put your `google-services.json` to the app-root folder.


### Register to FCM

- Call the method `register` in your `onCreate` to register to FCM.
```
pushmanager.register(
      context = this,
      onTokenReceived = { token ->
          Log.d("PushManager", "token received: $token")
      },
      onFailure = { exception ->
          Log.d("PushManager", " error during registration: ${exception?.message}")
      },
      handlePayload = { remoteMessageData ->

          Log.d("PushManager", "handlePayload - ${remoteMessageData.title} - ${remoteMessageData.body}" )

          // create notification
          NotificationData(
                  context = this,
                  title = remoteMessageData.title ?: "Default Title",
                  message = remoteMessageData.body ?: "Default Message")
      })
```


### Unregister from FCM
To unregister from FCM you need to register first. By using the token you received, you are able to unregister.
```
pushmanager.unregister(context, token)
```


### Topics


#### Subscribe to Topic

To subscribe to a topic just call  `subscriptToTopic`:

```
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

```
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
| com.google.firebase:firebase-core  | 16.0.1  |
| com.google.firebase:firebase-iid  | 17.0.0  |
| com.google.firebase:firebase-messaging  | 17.3.0  |


## Sample App

To run the sample App, just replace the `application_id` at the project `build.gradle` with someone according to your firebase project and add your `google-services.json` to to app root folder.

## Troubleshooting

If your app doesn't compile after adding this library, please check your dependencies for other play-service packages and update them to the newest version.


## Tests
Tests are not implemented yet but will be added soon.

## Need Help or something missing?

Please [submit an issue](https://github.com/grumpyshoe/android-module-pushmanager/issues) on GitHub.


## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file.


## Other information
#### Build Environment
```
Android Studio 3.1.4
Build #AI-173.4907809, built on July 23, 2018
JRE: 1.8.0_152-release-1024-b01 x86_64
JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
Mac OS X 10.13.4
```
