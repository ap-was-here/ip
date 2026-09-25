# UI Test Plan

## Current package-migration regression suite

This suite invokes `mary.MARY` with Java 25 and compares complete stdout exactly
(normalizing CRLF/LF only). Expected output is defined below before testing.
The historical cases farther down are retained as history: some predate typed
dates, command extraction, and the current farewell and are not current oracles.

Each case starts in a new isolated working directory with no save file, unless
`seed` specifies its contents. Sessions within a case share that directory to
test persistence. The repository's real `mary-data.txt` must never be used or
modified by these tests. Compile to a separate output directory with `javac -d`;
do not overwrite or remove the existing tracked `.class` files.

For this structural refactor, also compile a snapshot of the original sources
and compare its stdout and saved data with the packaged version using identical
fixtures. Stop immediately if either an exact expectation or comparison fails.

The executable specification below records each case's aim, input commands, and
expected output. `welcome` is the exact startup output (including the existing
two initial dividers). For a `startupError`, insert ` Error: MESSAGE` and one
divider after the first startup divider. Each step prints one divider, its
`output` lines, and one divider. Input is supplied through stdin and is not
included in stdout. Every line, including the last, ends with a newline.
An empty `steps` array means EOF immediately after startup.

`saved` is the exact expected final data file content, or null if no file should
exist. Case P1 covers adding all task types, marking/unmarking, deletion, date
search and persistence; P2 covers invalid inputs; P3 covers corrupted data;
P4 covers missing data and EOF. These cover the behaviours described by the
older cases without relying on obsolete date strings such as "Sunday".

<!-- package-suite -->
```json
{
  "separator": "____________________________________________________________",
  "welcome": [
    "____________________________________________________________",
    "____________________________________________________________",
    "███╗   ███╗ █████╗ ██████╗ ██╗   ██╗",
    "████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝",
    "██╔████╔██║███████║██████╔╝ ╚████╔╝",
    "██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝",
    "██║ ╚═╝ ██║██║  ██║██║  ██║   ██║",
    "╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝",
    "Hi! I'm MARY.",
    "What have you got for me today?",
    "____________________________________________________________"
  ],
  "cases": [
    {
      "id": "P1",
      "aim": "Exercise all packaged command types and task subtypes, list renumbering, date filtering, and save/reload across two processes.",
      "sessions": [
        {
          "steps": [
            {
              "input": "list",
              "output": [
                " MARY has no saved tasks yet."
              ]
            },
            {
              "input": "todo read book",
              "output": [
                " Got it. I've added this task:",
                "   [T][ ] read book",
                " Now you have 1 tasks in the list."
              ]
            },
            {
              "input": "deadline return book /by 2/12/2019 1800",
              "output": [
                " Got it. I've added this task:",
                "   [D][ ] return book (by: 2 Dec 2019 18:00)",
                " Now you have 2 tasks in the list."
              ]
            },
            {
              "input": "event project meeting /from 2/12/2019 1400 /to 4/12/2019 1600",
              "output": [
                " Got it. I've added this task:",
                "   [E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)",
                " Now you have 3 tasks in the list."
              ]
            },
            {
              "input": "list",
              "output": [
                " Here are the tasks in your list:",
                " 1.[T][ ] read book",
                " 2.[D][ ] return book (by: 2 Dec 2019 18:00)",
                " 3.[E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)"
              ]
            },
            {
              "input": "mark 2",
              "output": [
                " Nice! I've marked this task as done:",
                "   [D][X] return book (by: 2 Dec 2019 18:00)"
              ]
            },
            {
              "input": "unmark 2",
              "output": [
                " OK, I've marked this task as not done yet:",
                "   [D][ ] return book (by: 2 Dec 2019 18:00)"
              ]
            },
            {
              "input": "mark 1",
              "output": [
                " Nice! I've marked this task as done:",
                "   [T][X] read book"
              ]
            },
            {
              "input": "delete 2",
              "output": [
                " Noted. I've removed this task:",
                "   [D][ ] return book (by: 2 Dec 2019 18:00)",
                " Now you have 2 tasks in the list."
              ]
            },
            {
              "input": "list",
              "output": [
                " Here are the tasks in your list:",
                " 1.[T][X] read book",
                " 2.[E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)"
              ]
            },
            {
              "input": "on 3/12/2019",
              "output": [
                " Tasks occurring on 2019-12-03:",
                " [E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)"
              ]
            },
            {
              "input": "on 1/12/2019",
              "output": [
                " No deadlines or events occur on 2019-12-01."
              ]
            },
            {
              "input": "bye",
              "output": [
                "See you later. Complete your tasks on time!"
              ]
            }
          ]
        },
        {
          "steps": [
            {
              "input": "list",
              "output": [
                " Here are the tasks in your list:",
                " 1.[T][X] read book",
                " 2.[E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)"
              ]
            },
            {
              "input": "on 3/12/2019",
              "output": [
                " Tasks occurring on 2019-12-03:",
                " [E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)"
              ]
            },
            {
              "input": "bye",
              "output": [
                "See you later. Complete your tasks on time!"
              ]
            }
          ]
        }
      ],
      "saved": "T | 1 | read book\nE | 0 | project meeting | 2019-12-02T14:00 | 2019-12-04T16:00\n"
    },
    {
      "id": "P2",
      "aim": "Verify exceptions and invalid command/date messages still cross package boundaries, with no saved data created.",
      "sessions": [
        {
          "steps": [
            {
              "input": "",
              "output": [
                " Error: please enter a command or task."
              ]
            },
            {
              "input": "blah",
              "output": [
                " Error: I don't recognize that command; use todo, deadline, event, on, list, mark, unmark, delete, or bye."
              ]
            },
            {
              "input": "todo",
              "output": [
                " Error: use 'todo description' to add a task without a date."
              ]
            },
            {
              "input": "deadline homework",
              "output": [
                " Error: use 'deadline description /by date or time'."
              ]
            },
            {
              "input": "event meeting /from 2pm",
              "output": [
                " Error: use 'event description /from start /to end'."
              ]
            },
            {
              "input": "mark abc",
              "output": [
                " Error: 'abc' is not a valid task number; use a positive whole number."
              ]
            },
            {
              "input": "delete 0",
              "output": [
                " Error: task 0 does not exist; use 'list' to see valid task numbers."
              ]
            },
            {
              "input": "deadline return book /by tomorrow",
              "output": [
                " Error: use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800."
              ]
            },
            {
              "input": "event meeting /from 2/12/2019 /to 2/12/2019 1600",
              "output": [
                " Error: use event date/time format d/M/yyyy HHmm for both /from and /to."
              ]
            },
            {
              "input": "on tomorrow",
              "output": [
                " Error: use date format d/M/yyyy, for example 2/12/2019."
              ]
            },
            {
              "input": "list",
              "output": [
                " MARY has no saved tasks yet."
              ]
            },
            {
              "input": "bye",
              "output": [
                "See you later. Complete your tasks on time!"
              ]
            }
          ]
        }
      ],
      "saved": null
    },
    {
      "id": "P3",
      "aim": "Load a malformed record in an isolated data file; report the loading error and continue accepting commands without changing that file.",
      "seed": "not a valid task record\n",
      "sessions": [
        {
          "startupError": "the saved task data is corrupted: invalid record on line 1.",
          "steps": [
            {
              "input": "list",
              "output": [
                " MARY has no saved tasks yet."
              ]
            },
            {
              "input": "bye",
              "output": [
                "See you later. Complete your tasks on time!"
              ]
            }
          ]
        }
      ],
      "saved": "not a valid task record\n"
    },
    {
      "id": "P4",
      "aim": "Verify the packaged entry point handles end-of-input and a missing data file.",
      "sessions": [
        {
          "steps": []
        }
      ],
      "saved": null
    }
  ]
}
```
<!-- /package-suite -->

## Package-migration test session

<!-- package-session -->
Date: 2026-09-25. Runtime/compiler: Temurin Java 25.0.4.1.

Result: **PASS** — P1–P4 (five packaged sessions, plus five matching baseline
sessions). All 22 sources compiled into a clean output folder. The complete
stdout, zero exit codes, empty stderr, and final saved-file contents matched the
predeclared expectations and the original default-package implementation.
Only line endings were normalized; spaces and the Unicode banner were preserved.

Each case used an isolated working directory. The project save file was not
used. Below are the actual inputs and complete captured outputs; input is shown
separately because redirected stdin is not echoed by the application.

### P1, session 1: PASS

Console input:

```text
list
todo read book
deadline return book /by 2/12/2019 1800
event project meeting /from 2/12/2019 1400 /to 4/12/2019 1600
list
mark 2
unmark 2
mark 1
delete 2
list
on 3/12/2019
on 1/12/2019
bye
```

Console output (stderr empty; exit code 0):

```text
____________________________________________________________
____________________________________________________________
███╗   ███╗ █████╗ ██████╗ ██╗   ██╗
████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝
██╔████╔██║███████║██████╔╝ ╚████╔╝
██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝
██║ ╚═╝ ██║██║  ██║██║  ██║   ██║
╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
Hi! I'm MARY.
What have you got for me today?
____________________________________________________________
____________________________________________________________
 MARY has no saved tasks yet.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: 2 Dec 2019 18:00)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: 2 Dec 2019 18:00)
 3.[E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: 2 Dec 2019 18:00)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: 2 Dec 2019 18:00)
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: 2 Dec 2019 18:00)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)
____________________________________________________________
____________________________________________________________
 Tasks occurring on 2019-12-03:
 [E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)
____________________________________________________________
____________________________________________________________
 No deadlines or events occur on 2019-12-01.
____________________________________________________________
____________________________________________________________
See you later. Complete your tasks on time!
____________________________________________________________
```

### P1, session 2: PASS

Console input:

```text
list
on 3/12/2019
bye
```

Console output (stderr empty; exit code 0):

```text
____________________________________________________________
____________________________________________________________
███╗   ███╗ █████╗ ██████╗ ██╗   ██╗
████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝
██╔████╔██║███████║██████╔╝ ╚████╔╝
██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝
██║ ╚═╝ ██║██║  ██║██║  ██║   ██║
╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
Hi! I'm MARY.
What have you got for me today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)
____________________________________________________________
____________________________________________________________
 Tasks occurring on 2019-12-03:
 [E][ ] project meeting (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)
____________________________________________________________
____________________________________________________________
See you later. Complete your tasks on time!
____________________________________________________________
```

### P2, session 1: PASS

Console input:

```text

blah
todo
deadline homework
event meeting /from 2pm
mark abc
delete 0
deadline return book /by tomorrow
event meeting /from 2/12/2019 /to 2/12/2019 1600
on tomorrow
list
bye
```

Console output (stderr empty; exit code 0):

```text
____________________________________________________________
____________________________________________________________
███╗   ███╗ █████╗ ██████╗ ██╗   ██╗
████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝
██╔████╔██║███████║██████╔╝ ╚████╔╝
██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝
██║ ╚═╝ ██║██║  ██║██║  ██║   ██║
╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
Hi! I'm MARY.
What have you got for me today?
____________________________________________________________
____________________________________________________________
 Error: please enter a command or task.
____________________________________________________________
____________________________________________________________
 Error: I don't recognize that command; use todo, deadline, event, on, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
 Error: use 'todo description' to add a task without a date.
____________________________________________________________
____________________________________________________________
 Error: use 'deadline description /by date or time'.
____________________________________________________________
____________________________________________________________
 Error: use 'event description /from start /to end'.
____________________________________________________________
____________________________________________________________
 Error: 'abc' is not a valid task number; use a positive whole number.
____________________________________________________________
____________________________________________________________
 Error: task 0 does not exist; use 'list' to see valid task numbers.
____________________________________________________________
____________________________________________________________
 Error: use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800.
____________________________________________________________
____________________________________________________________
 Error: use event date/time format d/M/yyyy HHmm for both /from and /to.
____________________________________________________________
____________________________________________________________
 Error: use date format d/M/yyyy, for example 2/12/2019.
____________________________________________________________
____________________________________________________________
 MARY has no saved tasks yet.
____________________________________________________________
____________________________________________________________
See you later. Complete your tasks on time!
____________________________________________________________
```

### P3, session 1: PASS

Console input:

```text
list
bye
```

Console output (stderr empty; exit code 0):

```text
____________________________________________________________
 Error: the saved task data is corrupted: invalid record on line 1.
____________________________________________________________
____________________________________________________________
███╗   ███╗ █████╗ ██████╗ ██╗   ██╗
████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝
██╔████╔██║███████║██████╔╝ ╚████╔╝
██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝
██║ ╚═╝ ██║██║  ██║██║  ██║   ██║
╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
Hi! I'm MARY.
What have you got for me today?
____________________________________________________________
____________________________________________________________
 MARY has no saved tasks yet.
____________________________________________________________
____________________________________________________________
See you later. Complete your tasks on time!
____________________________________________________________
```

### P4, session 1: PASS

Console input (immediate EOF):

```text
```

Console output (stderr empty; exit code 0):

```text
____________________________________________________________
____________________________________________________________
███╗   ███╗ █████╗ ██████╗ ██╗   ██╗
████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝
██╔████╔██║███████║██████╔╝ ╚████╔╝
██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝
██║ ╚═╝ ██║██║  ██║██║  ██║   ██║
╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
Hi! I'm MARY.
What have you got for me today?
____________________________________________________________
```
<!-- /package-session -->

## Historical test plans and records (superseded)

This file contains command-line UI test cases for MARY. Each test starts a fresh program process, so task data is kept only for the duration of that test. The full startup banner is included in the captured session records.

## Test case 1: Add tasks and list them

Aim: Verify that entered task text is stored and displayed by the `list` command.

Inputs:

```text
todo read book
todo return book
list
bye
```

Expected output: The output includes the MARY greeting, typed task markers, and:

```text
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
[T][ ] return book
Now you have 2 tasks in the list.
Here are the tasks in your list:
1.[T][ ] read book
2.[T][ ] return book
Bye. Hope to see you again soon!
```

## Test case 2: Mark and unmark a task

Aim: Verify that `mark N` changes a task to done and `unmark N` changes it back to not done.

Inputs:

```text
todo read book
mark 1
list
unmark 1
list
bye
```

Expected output: The output includes:

```text
Nice! I've marked this task as done:
[X] read book
1.[T][X] read book
OK, I've marked this task as not done yet:
[ ] read book
1.[T][ ] read book
Bye. Hope to see you again soon!
```

## Test case 3: Add todo, deadline, and event tasks

Aim: Verify that MARY stores todo, deadline, and event objects polymorphically in one task list and preserves their date/time details.

Inputs:

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output:

```text
[T][ ] borrow book
[D][ ] return book (by: Sunday)
[E][ ] project meeting (from: Mon 2pm to: 4pm)
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
Bye. Hope to see you again soon!
```

## Test session record

## Test case 6: Save and reload tasks

Aim: Verify that task changes are saved to the relative current-folder file and loaded by a new chatbot process.

Inputs for the first session:

```text
todo read book
deadline return book /by Sunday
mark 1
bye
```

Expected output: The tasks are added and marked successfully, and a later session started in the same folder displays `[T][X] read book` and `[D][ ] return book (by: Sunday)` after `list`.

Inputs for the second session:

```text
list
bye
```

## Test case 7: Handle corrupted saved data

Aim: Verify that malformed saved records produce a clear startup error and do not crash the chatbot.

Setup: Replace `mary-data.txt` in the current folder with:

```text
not a valid task record
```

Inputs:

```text
list
bye
```

Expected output includes:

```text
Error: the saved task data is corrupted: invalid record on line 1.
MARY has no saved tasks yet.
See you later. Complete your tasks on time!
```

## Test case 8: Parse and search dates and times

Aim: Verify that deadline and event date/time text is parsed into date/time values and that `on d/M/yyyy` finds tasks occurring on that date.

Inputs:

```text
deadline return book /by 2/12/2019 1800
event project meeting /from 2/12/2019 1400 /to 2/12/2019 1600
on 2/12/2019
bye
```

Expected output includes:

```text
[D][ ] return book (by: 2 Dec 2019 18:00)
[E][ ] project meeting (from: 2 Dec 2019 14:00 to: 2 Dec 2019 16:00)
Tasks occurring on 2019-12-02:
[D][ ] return book (by: 2 Dec 2019 18:00)
[E][ ] project meeting (from: 2 Dec 2019 14:00 to: 2 Dec 2019 16:00)
```

## Test case 9: Reject invalid date/time input

Aim: Verify that malformed deadline/event date-time values and malformed date searches receive specific correction messages.

Inputs:

```text
deadline return book /by tomorrow
event meeting /from 2/12/2019 /to 2/12/2019 1600
on tomorrow
bye
```

Expected output includes:

```text
Error: use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800.
Error: use event date/time format d/M/yyyy HHmm for both /from and /to.
Error: use date format d/M/yyyy, for example 2/12/2019.
```

### 2026-09-25 date/time session

Test cases 8 and 9: PASS

The date/time parsing, `on` search, invalid-input messages, and reloading of ISO date-time records matched the expected results.

The test must restore or remove the temporary data file after completion.

### 2026-09-21 persistence session

Test case 6: PASS

The first session saved:

```text
T | 1 | read book
D | 0 | return book | Sunday
```

A second process loaded both records and displayed:

```text
1.[T][X] read book
2.[D][ ] return book (by: Sunday)
```

Test case 7: PASS

Malformed records were reported as a corrupted-data error, and the chatbot continued to accept `list` and `bye`.

### 2026-09-21

Test case 1: PASS

Console input:

```text
todo read book
todo return book
list
bye
```

Console output:

```text
MARY startup banner and greeting displayed.
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
Here are the tasks in your list:
1.[T][ ] read book
2.[T][ ] return book
See you later. Complete your tasks on time!
```

## Test case 5: Delete a task

Aim: Verify that `delete N` removes the selected task, shifts later task numbers, and reports the updated task count.

Inputs:

```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
delete 2
list
bye
```

Expected output:

```text
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
Noted. I've removed this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
1.[T][ ] read book
2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
See you later. Complete your tasks on time!
```

### 2026-09-21 deletion session

Test case 5: PASS

Console input:

```text
todo read book
todo return book
event meeting /from 2pm /to 4pm
list
delete 3
list
bye
```

Console output:

```text
1.[T][ ] read book
2.[T][ ] return book
3.[E][ ] meeting (from: 2pm to: 4pm)
Noted. I've removed this task:
  [E][ ] meeting (from: 2pm to: 4pm)
Now you have 2 tasks in the list.
1.[T][ ] read book
2.[T][ ] return book
See you later. Complete your tasks on time!
```

Test case 2: PASS

Console input:

```text
read book
mark 1
list
unmark 1
list
bye
```

Console output:

```text
Got it. I've added this task:
  [T][ ] read book
Nice! I've marked this task as done:
  [T][X] read book
1.[T][X] read book
OK, I've marked this task as not done yet:
  [T][ ] read book
1.[T][ ] read book
See you later. Complete your tasks on time!
```

Test case 3: PASS

Console input:

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Console output:

```text
Got it. I've added this task:
  [T][ ] borrow book
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
See you later. Complete your tasks on time!
```

Implementation note: The three task types are now represented by `Todo`, `Deadline`, and `Event` subclasses in separate files. They are stored together in `Task[]` and formatted through polymorphic `toString()` methods.

## Test case 4: Explain invalid input

Aim: Verify that invalid commands and malformed task commands produce specific correction guidance without terminating the session.

Inputs:

```text
todo
deadline homework
event meeting /from 2pm
mark abc
blah
bye
```

Expected output:

```text
Error: use 'todo description' to add a task without a date.
Error: use 'deadline description /by date or time'.
Error: use 'event description /from start /to end'.
Error: 'abc' is not a valid task number; use a positive whole number.
Error: I don't recognize that command; use todo, deadline, event, list, mark, unmark, or bye.
See you later. Complete your tasks on time!
```

### 2026-09-21 error-handling session

Test case 4: PASS

Console input:

```text
todo
deadline homework
event meeting /from 2pm
mark abc
blah
bye
```

Console output:

```text
Error: use 'todo description' to add a task without a date.
Error: use 'deadline description /by date or time'.
Error: use 'event description /from start /to end'.
Error: 'abc' is not a valid task number; use a positive whole number.
Error: I don't recognize that command; use todo, deadline, event, list, mark, unmark, or bye.
See you later. Complete your tasks on time!
```
