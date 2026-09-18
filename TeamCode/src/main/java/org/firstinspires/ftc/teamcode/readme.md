## Team Code

You are in the right place to begin creating code to run on an FTC robot using the REV Control Hub. It is highly recommended that you access this folder using VSCode and GitHub. See the section at the bottom for how to set up VSCode and GitHub.

### Folder Organization
Do not create any Java code right here! Instead, place it it one of the subfolders: opmodes, hardware, and util. 
- **opmodes** Like the name suggests, this is for any code that is selected from the driver station menu to run in a match
- **hardware** This is for creating hardware "objects" that can be brought into opmodes and used to control specific hardware like drive systems, arms, cameras, etc.
- **util** This is for any code that is used as helper code

### Opmodes
There is a ton of help online about how to create opmodes. This section will be expanded later to talk about what we have chosen as best practices for our team

### Hardware
Each class in this folder should define a specific hardware class that can be used to create an object of that class inside an opmode. For example, if ExtendingArm.java is created here, it should have all the code needed to control and monitor an extending arm using public methods. Then, an opmode could include lines like this:
```java
ExtendingArm arm1 = new ExtendingArm(30, 120);
arm1.setExtension(50);
```

### Using VSCOode with GitHub
Setting up VSCode with GitHub to edit:
1. Download the Git installer using [this link](https://github.com/git-for-windows/git/releases/download/v2.55.0.windows.5/Git-2.55.0.5-64-bit.exe) to get the latest version. Run the downloaded .exe file. 
2. Click Next through the setup prompts. You can safely keep the default settings, but keep an eye out for these two helpful adjustments: 
- Change the default editor from Vim to Visual Studio Code.
- Ensure "Git from the command line and also from 3rd-party software" is selected
3. Open VSCode. Open a new terminal by going to Terminal-->New Terminal in the menu
4) Type these two commands at the prompt in the terminal:
    ```
    git config --global user.name "Your Name"
    git config --global user.email "your.email@example.com"
    ```
    except use your actual name and email.
5. Click on the GitHub icon on the left toolbar in VSCode. Select Clone Repository. Enter the URL of this repository: https://github.com/U-High-Robotics-Team/BioBuzz2026. 
6. After making edits, select Commit & Sync from the Github control panel in VSCode.
    