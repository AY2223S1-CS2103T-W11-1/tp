---
layout: page
title: Developer Guide
---

#Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

### **Acknowledgements**

This application is based on addressbook-level3 by se-edu.

--------------------------------------------------------------------------------------------------------------------

### **Setting up, getting started**

Refer to the guide 
[_Setting up and getting started_](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/docs/SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Introduction**

NUSEatWhere is a Command Line (CLI) application which helps you search for the available food options in NUS and 
make an informed decision on where to eat. It does this via a preloaded set of data paired with commands that helps the
user navigate the list of eateries in the National University of Singapore (NUS).

This Developer Guide aims to outline how the NUSEatWhere application was implemented to ensure
ease of modifications for any future changes/developments you might have in mind for our application.

_For details on commands or how a user would use the application, 
you can refer to our [User Guide](https://ay2223s1-cs2103t-w11-1.github.io/tp/UserGuide.html)._

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the 
[diagrams](https://github.com/AY2223S1-CS2103T-W11-1/tp/tree/master/docs/diagrams/) folder. Refer to the 
[_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create 
and edit diagrams.
</div>

### Architecture

<p align="center">
<img src="images/ArchitectureDiagram.png" width="280" /> <br>
*The* ***Architecture Diagram*** *explains the high-level design of the App.*
</p>

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called 
[`Main`](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/src/main/java/eatwhere/foodguide/Main.java) and 
[`MainApp`](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/src/main/java/eatwhere/foodguide/MainApp.java). 
It is responsible for,

* **At app launch**: Initializes the components in the correct sequence, and connects them up with each other.

* **At shut down**: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

<p align="center">
<img src="images/ArchitectureSequenceDiagram.png" width="574" /> <br>
*The* ***Sequence Diagram*** *shows how the components interact with each other for the 
scenario where the user issues the command `delete 1`.*
</p>

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

<br>

<p align="center">
<img src="images/ComponentManagers.png" width="300" /> <br>
*The (partial)* ***class diagram*** *shows how the Logic class interacts with the Model and Storage classes.*
</p>

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality 
using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given 
component through its interface rather than the concrete class (reason: to prevent outside component's being 
coupled to the implementation of a component), as illustrated in the (partial) class diagram above.

The sections below give more details of each component.

### UI component

The **API** of this component is specified in 
[`Ui.java`](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/src/main/java/eatwhere/foodguide/ui/Ui.java)

<p align="center">
<img src="images/UiClassDiagram.png"  /> <br>
*The (partial)* ***class diagram*** *shows how the Logic class interacts with the Model and Storage classes.*
</p>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `EateryListPanel`, 
`StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures 
the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files 
that are in the `src/main/resources/view` folder. For example, the layout of the 
[`MainWindow`](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/src/main/java/eatwhere/foodguide/ui/MainWindow.java) 
is specified in [`MainWindow.fxml`](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* __executes user commands__ using the `Logic` component.
* __listens for changes to `Model` data__ so that the UI can be updated with the modified data.
* __keeps a reference to the `Logic` component__, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it __displays the `Eatery` object__ residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/src/main/java/eatwhere/foodguide/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it uses the `FoodGuideParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add an eatery).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

<p align="center">
<img src="images/DeleteSequenceDiagram.png" /> <br>
*The* ***Sequence Diagram*** *illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.*
</p>

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

<p align="center">
<img src="images/ParserClasses.png" width="600"/> <br>
*The diagram shows the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command.*
</p>

How the parsing works:
* When called upon to parse a user command, the `FoodGuideParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `FoodGuideParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/src/main/java/eatwhere/foodguide/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />

The `Model` component,

* __Stores the address book data__ i.e., all `Eatery` objects (which are contained in a `UniqueEateryList` object).
* __Stores the currently 'selected' `Eatery` objects__ (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Eatery>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* __Stores a `UserPref` object__ that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* Does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `FoodGuide`, which `Eatery` references. This allows `FoodGuide` to only require one `Tag` object per unique tag, instead of each `Eatery` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>

### Storage component

**API** : [`Storage.java`](https://github.com/AY2223S1-CS2103T-W11-1/tp/blob/master/src/main/java/eatwhere/foodguide/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,

* Can __save both food guide data and user preference data__ in json format, and read them back into corresponding objects.
* Inherits from both `FoodGuideStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* Depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `eatwhere.foodguide.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Displaying Command Help

Users are able to view a help or usage message for individual commands by including a special flag in their input.
This mimics the behaviour found in other CLI applications and shells, where it is facilitated by
the addition of arguments such as `-h`, `-help`, or `--help`.

#### Implementation

The FoodGuide's current `PREFIX_HELP`, representing the help flag, is `-h`,
which aligns with the other prefixes in the command syntax.
In the FoodGuide, since all parsing beyond identifying the command word is handled by individual command parsers,
searching for this flag has to be done in these parsers.

Individual `CommandParser`s return instances of their `Command`.
Because different commands have varying number of arguments, modifying `CommandResult` to support the
displaying of a help message would require additional constructors for all supported commands,
which is tedious and makes future commands harder to implement.

Therefore, the option to display help instead makes use of a special exception `DisplayCommandHelpException`,
which is thrown when `PREFIX_HELP` is detected. This then bypasses usual command evaluation and
returns the usage message of the command.

The detection of `PREFIX_HELP` is as follows:
* The prefix must not be accompanied by a value. `-hello` and `-h ello` will not invoke the exception.
* Presence of the prefix overrides other arguments.

The detection rules limit false-positives from stray `-h`s in names of eateries, locations, etc.

For supported commands, the following activity diagram summarizes what happens when a user executes a command:

<img src="images/CommandDisplayHelpActivityDiagram.png" />

### Favourite/Unfavourite feature
The favourite/unfavourite commands were introduced as a way to standardize how "favourite eateries" are tagged.
In a way, `fav`/`unfav` is a shortcut for `tag`/`untag`. The "`<3`" favourite tag is implemented in such a way in the interest of time, and the fact that
it can be searched up with other tags via `findTag`, hence proving to be more useful at the project's current iteration.

#### Implementation
Currently, `FavouriteCommand`/`UnfavouriteCommand` is similar to the other commands available for use, with the exception that it 
_extends_ `TagCommand`/`UntagCommand` and not `Command`. Since `FavouriteCommand` and `UnfavouriteCommand` are both implemented
in a similar way, this section will be focusing mainly on `FavouriteCommand` for ease of explanation.

<p align="center">
<img src="images/FavouriteSequenceDiagram.png" /> <br>
*The* ***Sequence Diagram*** *shows the interactions within the Logic component when `fav` is called.*
</p>

As can be seen by the above diagram, most function calls to `FavouriteCommand` is directed to TagCommand, though there are some points to note:
* The fixed "`<3`" tag should be made into a tag in the `FavouriteCommand` class rather than in the `TagCommand` class. This is because `FavouriteCommand` is the subclass, hence `TagCommand` should have no dependencies on `FavouriteCommand`.
* Using the above-mentioned `-h` help command should show a custom message for `FavouriteCommand`, since it's command `fav` is different from `tag`.
* Upon successfully favouriting an eatery, the user should receive a message indicating the "`<3`" is a tag, and can be searched up and used as a criteria for the `-r` randomizer feature via the `findTag` command.

Through a comparison with the sequence diagram found in the [logic component section](#logic-component) that bears the shape of most of the default commands, it can be seen in exactly which function calls `FavouriteCommand` differs.
<br>

When `FavouriteCommand` is first initialized, it initializes `TagCommand` via a `super()` method call. Hence, the process of turning "`<3`" into a tag acceptable by `TagCommand` has to be streamlined, such that prefixes in `FavouriteCommand` are used.
Additionally, the `execute()` call to `FavouriteCommand` is passed onto `TagCommand` whereby `TagCommand` then normally interacts with the `Model` class as other commands do.

This unique implementation of `FavouriteCommand` and `UnfavouriteCommand` is especially important to keep in mind if future modifications (e.g. keeping favourited eateries at the top of the list) are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedFoodGuide`. It extends `FoodGuide` with an undo/redo history, stored internally as an `addressBookfoodGuideStateList` and `currentStatePointer`. Additionally, it implements the following operations:


* `VersionedFoodGuide#commit()` — Saves the current food guide state in its history.
* `VersionedFoodGuide#undo()` — Restores the previous food guide state from its history.
* `VersionedFoodGuide#redo()` — Restores a previously undone food guide state from its history.

These operations are exposed in the `Model` interface as `Model#commitFoodGuide()`, `Model#undoFoodGuide()` and `Model#redoFoodGuide()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedFoodGuide` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th eatery in the address book. The `delete` command calls `Model#commitFoodGuide()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `foodGuideStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new eatery. The `add` command also calls `Model#commitFoodGuide()`, causing another modified address book state to be saved into the `foodGuideStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitFoodGuide()`, so the address book state will not be saved into the `foodGuideStateList`.

</div>

Step 4. The user now decides that adding the eatery was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoFoodGuide()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial FoodGuide state, then there are no previous FoodGuide states to restore. The `undo` command uses `Model#canUndoFoodGuide()` to check if this is the case. If so, it will return an error to the user rather

than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoFoodGuide()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `foodGuideStateList.size() - 1`, pointing to the latest address book state, then there are no undone FoodGuide states to restore. The `redo` command uses `Model#canRedoFoodGuide()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitFoodGuide()`, `Model#undoFoodGuide()` or `Model#redoFoodGuide()`. Thus, the `foodGuideStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitFoodGuide()`. Since the `currentStatePointer` is not pointing at the end of the `foodGuideStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.

* Pros: Easy to implement.
* Cons: May have performance issues in terms of memory usage.





* **Alternative 2:** Individual command knows how to undo/redo by itself.

* Pros: Will use less memory (e.g. for `delete`, just save the eatery being deleted).
* Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: This product is for ...
* NUS students/staff who prefer CLI over GUI
* want to keep track of the various food options in NUS.

**Value proposition**: This application summarizes the various food options available in NUS, and allows users to make an informed choice as to what to eat.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a … | I want to …                      | So that I can…                                    |
|----------|--------|----------------------------------|---------------------------------------------------|
| `* * *`  | user   | see usage instructions           | easily figure out and remember how to use the app |
| `* * *`  | user   | see what stalls there are in NUS | know what food is available                       |
| `* * *`  | user   | search for stalls by name        |                                                   |
| `* * *`  | user   | search for stalls by tags        |                                                   |
| `* * *`  | user   | search for stalls by location    |                                                   |
| `* * *`  | user   | search for stalls by cuisine     |                                                   |
| `* * *`  | user   | tag stalls                       | organize stalls by tags                           |
| `* * *`  | user   | untag a stall                    | remove a stall from a tag group                   |
| `* *`    | user   | add new food stalls              | customize the list                                |
| `* *`    | user   | remove food stalls               | customize the list                                |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `FoodGuide` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Search stall by Cuisine**

**MSS**


1. User requests to search for stall by cuisine
2. NUSEatWhere returns a list of all the food stalls of the given cuisine

**Use case: Search stall by Name**

**MSS**

1. User requests to search for stall by name
2. NUSEatWhere returns a list of all the food stalls with a matching name

**Use case: Search stall by Tag**

**MSS**

1. User requests to search for stall by tag
2. NUSEatWhere returns a list of all the food stalls with that tag

**Use case: Search stall by Location**

**MSS**

1. User requests to search for stall by location
2. NUSEatWhere returns a list of all the food stalls with that location

**Use case: Help Command**

**MSS**

1. User enters the help command
2. NUSEatWhere returns a list of all commands and their functionality explained

**Use case: List Command**

**MSS**

1. User enters the list command
2. NUSEatWhere returns a list of all food stalls

**Use case: Tag Command**

**MSS**

1. User requests to tag a food stall
2. NUSEatWhere adds a tag to the food stall and shows the updated food stall.

**Use case: Untag Command**

**MSS**

1. User requests to untag a food stall
2. NUSEatWhere untags the food stall and shows the updated food stall

**Use case: Add Command**

**MSS**

1. User requests to add a food stall
2. NUSEatWhere adds the food stall to the list.

**Use case: Delete Command**

**MSS**

1. User requests to delete a food stall
2. NUSEatWhere deletes the food stall from the list.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.

2. Should be able to hold up to 100 tagged food stalls without a noticeable sluggishness in performance for typical usage.

3. Should be able to store and load data for at least tagged 100 food stalls

4. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

5. Should be usable for users who have never been to NUS

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;

testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

1. Download the jar file and copy into an empty folder

1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

1. Resize the window to an optimum size. Move the window to a different location. Close the window.

1. Re-launch the app by double-clicking the jar file.<br>

Expected: The most recent window size and location is retained.

1. _{ more test cases …}_

### Deleting a Food Stall

1. Deleting a food stall while all food stall are being shown

1. Prerequisites: List all food stalls using the `list` command. Multiple eateries in the list.

1. Test case: `delete 1`<br>

Expected: First food stall is deleted from the list. Details of the deleted food stall shown in the status message. Timestamp in the status bar is updated.

1. Test case: `delete 0`<br>

Expected: No food stall is deleted. Error details shown in the status message. Status bar remains the same.

1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>

Expected: Similar to previous.

1. _{ more test cases …}_

### Saving data

1. Dealing with missing/corrupted data files

1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …}_
