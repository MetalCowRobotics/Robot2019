# Robot2019
Code for FRC 2019 - Deep Space Challenge

# **STOP**: Setup Your Worstation Before Proceeding

As of 2019, FRC is no longer supporting Eclipse.
All of the deploy scripts and configuration tools are provided as VSCode.

To setup your worstation, please go to the following link
**SLOWLY FOLLOW _ALL_ THE STEPS**
1. Install VS Code:  https://code.visualstudio.com/
2. Setup: [WPI Lib Plugins for VSCode](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/1027503-installing-c-and-java-development-tools-for-frc)
3. MAKE SURE the WPI folder is in the place they said and labeled named `frc2019` so that VSCode and Gradle can find the Java JDK.
4. Setup the [DriverStation and other NI tools]( https://wpilib.screenstepslive.com/s/currentCS/m/java/l/1027504-installing-the-frc-update-suite-all-languages)
     - NI Code: `B04P64186`
5. Learn about the [VSCode Interface](https://code.visualstudio.com/docs/getstarted/userinterface)
6. Study the [WPI VSCode Commands](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/1027552-wpilib-commands-in-vscode)
7. Creating a new project follow [these steps](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/1027062-creating-a-robot-program) **Tim did this already, see next steps**
     
## DID YOU SETUP YOUR WORSTATION??? ^^^^^

# Importing the 2019 Code

1. Install [Github Dekstop](https://desktop.github.com/)
    - Learn about the [GitHub flow](https://guides.github.com/introduction/flow/)
2. Open Github Desktop.
3. Login as `metalcow-student` via `File>Options` ask for the password on [team Slack](www.metalcowrobotics.comm/slack)
4. Choose the `Repository` dropdown and then `Add>Clone Repository...`
5. Select `2019Robot` from the list and choose 'Clone'.
6. In VSCode choose `File>Open` navigate to the folder `C:\Users\teamm\OneDrive\Documents\GitHub\Robot2019` where you cloned the Robot Code in step 2. Click `OK`
7. Debug a program [tips here](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/242588-debugging-a-robot-program)
8. Deploy the Robot code with [these steps](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/1027063-building-and-deploying-to-a-roborio)

# Read the WPI Java Lib

http://first.wpi.edu/FRC/roborio/release/docs/java/

# MetalCow Robotics 2019 - Code Details

## Stickler is setup on this project
If it stops working...
1. Login to Stickler
2. Give it permissions on all MetalCow Repositories
3. Add a .stickler.yml file to the root of the project
4. Add the google checkstyle to ./resources folder
5. In a pull request or a Push it should auto-run stickler and add comments on the push.

## Talon SRX Motor Controllers
*Note, add LifeBoat and SRX setup stuff