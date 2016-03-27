java-xml-builder
================

### Description
Small tool to generate XML document with pretty formatting. 

Lightweight analog of https://github.com/jmurty/java-xmlbuilder but without using DOM which makes it pretty fast.

### Build
```
./gradlew clean build
```

### Upload to Bintray
```
./gradlew clean bintrayUpload
```
Don't forget to specify environment variables `BINTRAY_USER` and `BINTRAY_API_KEY` before that.
