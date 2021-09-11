## GPAD

A small app for storing song lyrics with chords.


### Used by
* [react-native](https://reactnative.dev/docs/) 
* cljs 
* [krell](https://github.com/vouch-opensource/krell)

####libs
* [navigation](https://reactnavigation.org/docs/hello-react-navigation/)


### How to start (for dev)


```shell
# run metro
npx react-native start
# run android
npx react-native run-android
# run cljs
clj -M -m krell.main -co build.edn -c -r
```
or from babashka 
```shell
bb dev
```


### Production build
Just add -O advanced to the usual build command:

```shell
# cljs compile
clj -M -m krell.main -v -co build.edn -O advanced -c
# react native compile example
react-native run-android --variant=release
```


And re-run your app. If you see a weird message about npm_deps.js you may need to restart Metro.

