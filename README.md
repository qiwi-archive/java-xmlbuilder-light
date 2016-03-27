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

### Add to your project

**Gradle**   
```
repositories {
    jcenter()
}

dependencies {
    compile 'com.qiwi.utils:java-xmlbuilder-light:1.0'
}
```

### Example of usage
```java
XMLBuilder xml = new XMLBuilder();
xml.tag("request");
xml.tag("request-type", "payment");
xml.tag("extra").attr("name", "password").text("4b3tg42bf23bg832y3rf3!").up();
xml.tag("auth");
xml.tag("payment");
xml.tag("transaction-number", 2343242341256262362L);
xml.tag("from").attr("test", "true");
xml.up();
xml.tag("to");
xml.tag("amount", 100);
xml.tag("account-number", 21312312);
xml.tag("service-id", 121212);
xml.up();
xml.tag("extra").attr("name", "comment").text("abc");
xml.close();
System.out.println(xml.toString());
```
will generate XML like this:
```xml
<?xml version="1.0" encoding="utf-8"?>
<request>
 <request-type>payment</request-type>
 <extra name="password">4b3tg42bf23bg832y3rf3!</extra>
 <auth>
  <payment>
   <transaction-number>2343242341256262362</transaction-number>
   <from test="true"/>
   <to>
    <amount>100</amount>
    <account-number>21312312</account-number>
    <service-id>121212</service-id>
   </to>
   <extra name="comment">abc</extra>
  </payment>
 </auth>
</request>
```
