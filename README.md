# Author

**author**: David S Teles

**email**: david.ds.teles@gmail.com

Hello, my name is David Teles. I'm a software engineer. You can find me on [Linkedin](https://www.linkedin.com/in/david-teles/?locale=en_US). 

Feel free to send me a message.

# About

This is a starter point for quarkus reactive projects. It has basic examples and configurations that you maybe want in your projects.

You'll find here:

* basic reactive project structure
* example of layers communication using quarkus dependecy injection.
* example of client consumer of api using quarkus rest client.
* connection with mysql to show how entities and panache works.
* i18n configurations as well as messages bundles to both quarkus and hibernate validation
* prettier

## Prettier

This project has a maven plugin **prettier-maven-plugin** to help keep things neat. 

to run prettier check in this project you can:
```
mvn prettier:check
```

to run prettier write in this project you can:
```
mvn write
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-seed-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.
