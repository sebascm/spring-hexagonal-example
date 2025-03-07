# Hexagonal (Port and Adapter) Example #

A spring-boot based example of hexagonal design (also known as the ports and adapters design).

Through the use of ports, contracts between the various modules can be set up, allowing for the modules to be easily replaced with other implementations. The only condition; that the module conforms to the contract specified.

Thus by having a hexagonal design, the current database adapter module; which is a simple in-memory implementation can be swapped out for a JPA repository or a flat file or something else and as long as it conforms to the contract (aka port) no other module (especially the domain module) needs to know or care.

Similarly, the name verifier adapter can be swapped out for a real implementation that would communicate with a third party application without the rest of our application ever knowing. Again as long as the current implementation conforms to the contract (in this case the NameVerifierService) no one will ever know the difference.

Also, the rest api adapter (which as the name implies uses HTTP REST) could be replaced by a SOAP based api and again the domain (and other modules) would not know nor need to care as long as the new api passed along the expected objects as specified in the ports (AddUserService and GetUserService).

## Getting Started ##

```
    git clone https://github.com/gshaw-pivotal/spring-hexagonal-example.git
```

## Resources on Hexagonal / Ports and Adapters ##

The following are some resources that explain the hexagonal design / pattern

- [Hexagonal Architecture](http://alistair.cockburn.us/Hexagonal+architecture)
- [Ports-And-Adapters / Hexagonal Architecture](http://www.dossier-andreas.net/software_architecture/ports_and_adapters.html)

## Plugins
### Checkstyle
This plugin is executed during the validate phase. To test it use:
 ```
     $ mvn validate
 ```
Or:
```
    $ mvn checkstyle:check
```
### Spotbugs
This plugin is executed during the verify phase. To test it use:
```
    $ mvn verify
```
Or, you can use:
```
    $ mvn exec:exec -Pspotbugs -N
```
## How to run the application
Firstly, you have to install the project:
```
    $ mvn install
```
Move to application folder:
```
    $ cd application
```
Then, you run it via spring-boot:
```
    $ mvn spring-boot:run
```

## Docker configuration
The docker image is in [DockerHub](https://cloud.docker.com/u/sebascm/repository/docker/sebascm/mvnproject).

Pull the image 
```
    $ sudo docker pull sebascm/mvnproject:firstversion
```
And then run it
```
    $ sudo docker run -p 8080:8080 -t mvnproject:firstversion
```

# CI/CD Documentation

All CI/CD documentation of this project can be found [here](https://github.com/TorusNewies/documentationMavenCICD) 