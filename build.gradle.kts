

plugins {
    val kotlinVersion = "1.5.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.8.2"
}

group = "com.julym"
version = "1.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies{
    implementation("com.alibaba:fastjson:1.2.76")
    implementation( "com.microsoft.onnxruntime:onnxruntime:1.9.0")
    //implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}