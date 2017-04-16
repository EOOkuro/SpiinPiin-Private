## Setting Up SpiinPiin on development Machine
###### Introduction
The following steps will guide you through setting up SpiinPiin Mobile application on your  dev computer.
The steps have been tested on Ubuntu 16.04.2 LTS x64
This guide is focused on building for the android platform.

###### 1. Install Java
If you  do not have java installed, open your terminal and run the following commands one at a time:
-sudo apt-get update
-sudo add-apt-repository ppa:webupd8team/java
-sudo apt-get update
-sudo apt-get install oracle-java8-installer
Reference: [How To Install Java with Apt-Get on Ubuntu 16.04](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04)

**Add JAVA_HOME environment variable**
After Java is installed, open up the file `/etc/environment` and add the line:
`JAVA_HOME="/usr/lib/jvm/java-8-oracle"`
Save the file and close. Then check the variable, by running this command:
`echo $JAVA_HOME`

###### 2. Install Android Studio
Actually all you need is the android SDK, however using android studio reduces the effort in managing SDK updates,Signing the APK and creating AVDs. This is why I recommend installing Android Studio.
[Click here to Download Android Studio](https://developer.android.com/studio/index.html) and then [follow this instrrucions to install it](https://developer.android.com/studio/install.html)

After installation, run Android studio and update the SDKs. You can also create an Android Virtual Device. Thereafter, close android studio, we will not need it unless we are doing updates,signing app or creating AVD.
**Adding ANDROID_HOME environment variable**
open up the file `/etc/environment` and add the line:
`ANDROID_HOME="/your/path to/Android/Sdk"` in my case I have `ANDROID_HOME="/home/richie/Android/Sdk"`
Save the file and close. Then check the variable, by running this command:
`echo $ANDROID_HOME`

###### 3. Install Node.js
[Download nodejs from here depending on your OS version](https://nodejs.org/en/download/current/)
Extract the files to a folder, then copy the folder to `/opt`.
In my case i downloaded `node-v7.8.0-linux-x64.tar.xz` then extracted it to `node-v7.8.0-linux-x64` then copied this extracted folder to `/opt/node-v7.8.0-linux-x64` which is now my nodejs installation directory
**Add Nodejs to PATH so that it can be found globally**
Open up the file `/etc/environment` and append the line `:/opt/node-v7.8.0-linux-x64/bin` to the end of the contents in PATH eg
`PATH="....:/opt/node-v7.8.0-linux-x64/bin"`. Save and Close
You can now verify nodejs is installed by running `node -v` and `npm -v`

###### 4. Install Ionic
Run the following command in your teminal
`npm install -g ionic cordova`

###### 5. Install Git
If you have no git installed,Run the following commands in your terminal, one at a time
-sudo apt-get update
-sudo apt-get install git

###### 6. Cloning the SpiinPiin Project/
Clone the Spiinpiin project into your workspace directory by running `git clone https://github.com/SpinPiin/SpiinPiin-Private.git`.
cd into  `SpiinPiin-Private/front-end/mobile/spiinpiin`  and run the following commands one at a time:
- `npm install`
- `cordova platform add android`
- `ionic build android`
- To run app on AVD, ensure the AVD is running then run `ionic emulate android`.
- To run on actual phone, connect phone to computer via USB(ensure USB debugging enabled on the phone) then run `ionic run android`


###### 7. Conclusion.
In case you get stuck during installation, you can create an issue on this github project or send me an email via `richard.omoka@gmail.com`



