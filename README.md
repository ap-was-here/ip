# MARY project template

This is a project template for a greenfield Java project. It's named after the Java mascot _Duke_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/mary/MARY.java` file, right-click it, and choose `Run MARY.main()` (if the code editor is showing compile errors, try restarting the IDE). If you have an existing run configuration, change its main class to `mary.MARY`. If the setup is correct, you should see something like the below as the output:
   ```
   ███╗   ███╗ █████╗ ██████╗ ██╗   ██╗
   ████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝
   ██╔████╔██║███████║██████╔╝ ╚████╔╝
   ██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝
   ██║ ╚═╝ ██║██║  ██║██║  ██║   ██║
   ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Package structure

Packages group classes by responsibility. `src/main/java` remains the source root;
package directories sit underneath it.

```text
src/main/java/mary/
├── MARY.java
├── task/       Task, Todo, Deadline, Event, TaskList, TaskType, TaskStatus
├── command/    Command, AddCommand, DeleteCommand, ExitCommand, ListCommand,
│               MarkCommand, OnCommand, UnknownCommand, CommandType
├── parser/     Parser
├── storage/    Storage
├── ui/         Ui
└── exception/  MaryException, ErrorType
```

For example, `mary.task.Deadline` extends `mary.task.Task`. Classes in other
packages use explicit imports to refer to these types. Enums live beside the
classes they describe.

## Compile and run from PowerShell

From the project root, with JDK 25 on `PATH`:

```powershell
$sources = Get-ChildItem src/main/java -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d out $sources
if ($LASTEXITCODE -eq 0) { java -cp out mary.MARY }
```

The recursive source search includes every package. `-d out` keeps compiled
classes in the ignored output folder, and `mary.MARY` is the fully qualified
entry point. In IntelliJ, keep `src/main/java` marked as Sources Root and use
the project root as the working directory so `mary-data.txt` stays in place.
