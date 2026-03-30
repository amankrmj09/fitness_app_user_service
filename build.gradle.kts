plugins {
    java
    id("org.springframework.boot") version "3.5.13"
    id("io.spring.dependency-management") version "1.1.7"
}
val springCloudVersion by extra("2025.0.1")

group = "org.wallecodes"
version = "0.0.1-SNAPSHOT"
description = "user_service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // ✅ Spring MVC (servlet-based)
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // ✅ JPA + PostgreSQL
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    runtimeOnly("org.postgresql:postgresql")
    
    // ✅ Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    
    // ✅ Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    
    implementation("com.password4j:password4j:1.8.4")
    
    // ✅ JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    
    // ✅ Utilities
    implementation("org.modelmapper:modelmapper:3.2.6")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    // ✅ Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    
    // ✅ Dev tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    
    // ✅ Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
