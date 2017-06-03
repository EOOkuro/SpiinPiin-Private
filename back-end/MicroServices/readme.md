# Setup instructions
The instructions are envoronment agnostic to the greatest extent posible. 
* Install Java version 8 or higher (before installing, check the version of java that you have installed i.e. run javac -version command)
    * Ubuntu
        1. **Add the PPA** *$ sudo add-apt-repository ppa:webupd8team/java* 
        2. **Update and install the installer script** *$ sudo apt update; sudo apt install oracle-java8-installer*
        3. **Check the Java version** *$ javac -version*
        4. **Set Java environment variables** *$ sudo apt install oracle-java8-set-default*
    * Windows
        1. Download the jre/jdk installable from Oracle website and go through mormal windows installation.

* Install mondodb
    * Follow instructions [here](https://docs.mongodb.com/manual/installation/) based on your prefered environment.

#Building bunaries
* Ensure that you have [maven >= v3](https://maven.apache.org/) installed
* Change path to where the sources are, up until you are at the root where pom.xml file is located.
* Run *mvn package*, to package and generate the fat-jar.
    * You can run *mvn clean test*, if you want to run the test cases
    * Run *mvn compile vertx:run* to kickoff the microservice using maven. 

# Run the [microservices](http://vertx.io/) as a standalone
* Ensure the path where you want the log files exists. 
    * Create the necessary folder(s) if they dont exist.
    * The default directory is ${user.home}/logs/vertx/
    * If you change from the default directory path, please ensure that you make the necessary changes in **vertx-default-jul-logging.properties**
* Make any necessary changes to **config.json** 
    * Ensure the port is what you want it to be, as well as the base path **the base path should be in the format '/users/api'**).
* Start terminal/cmd 
* Change the path to the location where you have the fat-jar microservice binary i.e. **user-management-1.0.0.jar**
* Run *$ java -jar user-management-1.0.0.jar -Djava.util.logging.config.file=vertx-default-jul-logging.properties* 