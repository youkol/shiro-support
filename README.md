# shiro-support

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.youkol.support.shiro/shiro-support-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.youkol.support.shiro/shiro-support-parent)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.youkol.support.shiro/shiro-support-parent?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/snapshots/com/youkol/support/shiro/shiro-support-parent/)
[![License](https://img.shields.io/badge/license-apache-brightgreen)](http://www.apache.org/licenses/LICENSE-2.0.html)

A simple Shiro extension library.

### 1. Add Supports
* Spring cache-based implementations of Shiro's cache interfaces.
* A simple username/password/captcha authentication token to support the most widely-used authentication mechanism.
* Add JWT Support
* Custom implementation inherited ModularRealmAuthenticator, and 
  store the last AuthenticationException for multi realm.
  Fix when use multi realm, lost the AuthenticationException info. 

### 2. Usage
```xml
<dependency>
    <groupId>com.youkol.support.shiro</groupId>
    <artifactId>shiro-support</artifactId>
    <version>${shiro-support.version}</version>
</dependency>
```
For spring boot autoconfigure
```xml
<dependency>
    <groupId>com.youkol.support.shiro</groupId>
    <artifactId>shiro-support-spring-boot-starter</artifactId>
    <version>${shiro-support.version}</version>
</dependency>
```
