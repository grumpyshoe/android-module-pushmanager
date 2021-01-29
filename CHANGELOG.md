
# Changelog

## 1.3.0

* Bump dependencies to latest version.

### BREAKING CHANGES

* The functions `register` and `unregister` are now suspend functions so you need to use a coroutine context to run this. Example at the sample app.


## 1.2.2

* Bump dependencies to latest version.


## 1.2.1

* Make return value for `handleNotificationPayload` nullable. If this method returns _null_ no notification will be generated.


## 1.2.0

* Change structure of how to implement payload handling


## 1.1.0

* Change PendingIntent handling and move it's logic to `NotificationData`.