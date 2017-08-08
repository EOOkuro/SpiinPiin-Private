# Setup instructions (This is based of the earlier readme provided in the repository)
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
    
* Set up IntelliJ IDEA to build, run or install binaries:

    * Download IntelliJ IDEA community edition or enterpise edition [here] (https://www.jetbrains.com/idea/download) and go through normal windows/mac/linux installation.(Preferably version 2017.2 which has at least Maven 3.3.9 which is needed to properly build and compile the binaries)
    * After install click on the file menu in the top left corner and select "Open".
    * Navigate to the directory generated after cloning the project, select and click on "Open".
    * You should then see the project directory in the left pane under the "Project" tab.
    * Up next we will need to select the java sdk for the project. Click on "File" and then select "Project Structure".
    * Under "Project Settings" select "Project" and then choose the project sdk on the left. If you installed Java 8 properly you should see "1.8" as the selected option for the project sdk.
    * If it's not the default selected option, click on the dropdown and select "1.8" under the options listed.

Next we need to make sure the project has the necessary libraries needed to run the project

    * Click on "File" and then select "Project Structure".
    * Select "Libraries" and then click on the "+" button. Select "From maven".
    * We will need 3 libraries from vertx project namely "vertx-config, vertx-core and vertx-rx-java"
    * These will be added one after the other. In the input bar type or paste "io.vertx:vertx-config:3.4.2"
    * Click "Ok" and this will download the required library from maven.
    * Repeat the last two steps for the remaining libraries:

        "io.vertx:vertx-core:3.4.2"
        "io.vertx:vertx-rx-java:3.4.2"
    
(This basically translates to vertx-config version 3.4.2 from the io.vertx project)

    * Next go back to the default IDE view and select "Maven Projects" tab on the right hand side of the screen.
    * Click on the "+" button and then navigate to "SpiinPiin-Private > MicroServices > user-management"  then select the pom.xml in that directory.

This will populate the necessary maven profiles, plugins and lifecycle commands needed.

    * To successfully build the required jar file run the "clean", "validate", "compile" and "package" commands under the "Lifecycle" folder.
    * This will generate a folder named "target" under "SpiinPiin-Private > MicroServices > user-management > target".
    * In it you will find user-management-1.0.0.jar
    * Make a copy of config.json and place it in the target folder. In it you will find the port and base route for the endpoint.
    * Ensure the path where you want the log files exists. 
    * Create the necessary folder(s) if they dont exist.
    * The default directory is ${user.home}/logs/vertx/
    * If you change from the default directory path, please ensure that you make the necessary changes in **vertx-default-jul-logging.properties**
    * Make any necessary changes to **config.json** 
        * Ensure the port is what you want it to be, as well as the base path **the base path should be in the format '/users/api'**).
    * Start terminal/cmd 
    * Change the path to the location where you have the fat-jar microservice binary i.e. **user-management-1.0.0.jar**
    * Run *$ java -jar user-management-1.0.0.jar -Djava.util.logging.config.file=vertx-default-jul-logging.properties* 