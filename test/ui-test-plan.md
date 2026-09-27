# UI Test Plan

## Bug-fix UI regression run (2026-09-27)

Run R1–R2 and P1–P4 from the shared JSON specification against the newly built
`mary.jar`, with Java 25. R1 adds exact expectations for missing mark/unmark
arguments and impossible dates; R2 adds a malformed todo record with an extra
field. Use isolated directories. Compare exact output and saved data, stopping
at the first mismatch. Do not compare these corrected cases to the old buggy
baseline program. All prior console records below are historical.

<!-- bugfix-ui-session -->
Result: **PASS** — six cases (R1, R2, P1–P4), seven JAR sessions, Java 25.0.4.1.
Exact stdout, empty stderr, zero exit codes and saved-file contents all matched
the specification. Gradle `test shadowJar` succeeded and all 52 JUnit tests passed.
The tests used temporary data directories; the project save file was not used.

Complete console records follow, with redirected stdin shown separately.

### Bug-fix UI R1, session 1: PASS

Input (JSON strings preserve the trailing spaces in two commands):

```json
[
  "todo read",
  "mark",
  "unmark",
  "mark ",
  "unmark ",
  "on 29/2/2023",
  "deadline invalid /by 31/4/2024 1800",
  "event invalid /from 29/2/2023 1400 /to 1/3/2023 1600",
  "list",
  "bye"
]
```

Output:

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
 Got it. I've added this task:
   [T][ ] read
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use date format d/M/yyyy, for example 2/12/2019.
____________________________________________________________
____________________________________________________________
 Error: use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800.
____________________________________________________________
____________________________________________________________
 Error: use event date/time format d/M/yyyy HHmm for both /from and /to.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read
____________________________________________________________
____________________________________________________________
See you later. Complete your tasks on time!
____________________________________________________________
```

### Bug-fix UI R2, session 1: PASS

Input:

```text
list
bye
```

Output:

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

### Bug-fix UI P1, session 1: PASS

Input:

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

Output:

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

### Bug-fix UI P1, session 2: PASS

Input:

```text
list
on 3/12/2019
bye
```

Output:

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

### Bug-fix UI P2, session 1: PASS

Input:

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

Output:

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

### Bug-fix UI P3, session 1: PASS

Input:

```text
list
bye
```

Output:

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

### Bug-fix UI P4, session 1: PASS

Input (EOF):

```text
```

Output:

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
<!-- /bugfix-ui-session -->

## JUnit-addition UI regression run (2026-09-27)

The JUnit addition does not change production behavior. Run the existing P1–P4
suite and exact expectations below against `mary.jar` with Java 25. This UI run
is separate from the new boundary-focused JUnit tests, whose defects are recorded
in `junit-test-plan.md`. Stop this UI run at its first mismatch.

<!-- junit-ui-session -->
Result: **PASS** — P1–P4, five JAR sessions using Java 25. Every stdout
comparison and saved-file check matched the recorded expectations. Each process
exited with 0 and empty stderr. These results do not supersede the four failing
JUnit boundary tests described in `junit-test-plan.md`.

Complete console records follow (stdin is shown separately from stdout).

### JUnit-addition UI P1, session 1: PASS

Input:

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

Output:

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

### JUnit-addition UI P1, session 2: PASS

Input:

```text
list
on 3/12/2019
bye
```

Output:

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

### JUnit-addition UI P2, session 1: PASS

Input:

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

Output:

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

### JUnit-addition UI P3, session 1: PASS

Input:

```text
list
bye
```

Output:

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

### JUnit-addition UI P4, session 1: PASS

Input (EOF):

```text
```

Output:

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
<!-- /junit-ui-session -->

## Gradle JAR verification (2026-09-27)

Run the P1–P4 suite below against the Gradle-built `build/libs/mary.jar` using
Java 25 and `java -jar`, so the tests also verify the manifest's main class.
The aims, commands, exact output expectations and save-file expectations are
unchanged. Use isolated working directories and stop at the first mismatch.

<!-- gradle-session -->
Result: **PASS**. Gradle 9.6.1 `build` succeeded using Temurin Java 25.0.4.1.
The generated `mary.jar` launched successfully via `java -jar`. All five JAR
sessions (P1–P4) matched the exact expected console output and saved data;
stderr was empty and each process exited with code 0. Gradle's `test` task
reported NO-SOURCE; the UI suite below was run separately.

Actual console input and complete output follow.

### Gradle P1, session 1: PASS

Input:

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

Output:

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

### Gradle P1, session 2: PASS

Input:

```text
list
on 3/12/2019
bye
```

Output:

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

### Gradle P2, session 1: PASS

Input:

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

Output:

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

### Gradle P3, session 1: PASS

Input:

```text
list
bye
```

Output:

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

### Gradle P4, session 1: PASS

Input (EOF):

```text
```

Output:

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
<!-- /gradle-session -->

## Current package-migration regression suite

This suite invokes `mary.Mary` with Java 25 and compares complete stdout exactly
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
      "id": "R1",
      "aim": "Verify missing mark/unmark arguments and impossible dates give errors without crashing or adding invalid tasks.",
      "sessions": [
        {
          "steps": [
            {
              "input": "todo read",
              "output": [
                " Got it. I've added this task:",
                "   [T][ ] read",
                " Now you have 1 tasks in the list."
              ]
            },
            {
              "input": "mark",
              "output": [
                " Error: use 'mark N' or 'unmark N', where N is a task number."
              ]
            },
            {
              "input": "unmark",
              "output": [
                " Error: use 'mark N' or 'unmark N', where N is a task number."
              ]
            },
            {
              "input": "mark ",
              "output": [
                " Error: use 'mark N' or 'unmark N', where N is a task number."
              ]
            },
            {
              "input": "unmark ",
              "output": [
                " Error: use 'mark N' or 'unmark N', where N is a task number."
              ]
            },
            {
              "input": "on 29/2/2023",
              "output": [
                " Error: use date format d/M/yyyy, for example 2/12/2019."
              ]
            },
            {
              "input": "deadline invalid /by 31/4/2024 1800",
              "output": [
                " Error: use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800."
              ]
            },
            {
              "input": "event invalid /from 29/2/2023 1400 /to 1/3/2023 1600",
              "output": [
                " Error: use event date/time format d/M/yyyy HHmm for both /from and /to."
              ]
            },
            {
              "input": "list",
              "output": [
                " Here are the tasks in your list:",
                " 1.[T][ ] read"
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
      "saved": "T | 0 | read\n"
    },
    {
      "id": "R2",
      "aim": "Reject a saved todo containing an extra field, report its line, and preserve the corrupted file.",
      "seed": "T | 0 | read | unexpected\n",
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
      "saved": "T | 0 | read | unexpected\n"
    },
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
## Fat JAR verification — 2026-09-27

Built with Java 25 using Gradle `test shadowJar --rerun-tasks`: 52 JUnit tests passed.
Verified `build/libs/mary.jar` has `Main-Class: mary.MARY`, includes the entry-point class,
and excludes JUnit. Existing R1, R2, P1–P4 aims, inputs and expected outputs remain unchanged.
All six UI cases (seven fresh-process sessions) passed using `java -jar` in isolated
working directories; saved data also matched. No project task data was changed.
Inputs below are JSON strings to make trailing spaces and empty input explicit.
Actual output matched the recorded expected output exactly, ignoring only line endings.

### R1, session 1: PASS

Console input (one array element per line; an empty array means EOF):

```json
[
  "todo read",
  "mark",
  "unmark",
  "mark ",
  "unmark ",
  "on 29/2/2023",
  "deadline invalid /by 31/4/2024 1800",
  "event invalid /from 29/2/2023 1400 /to 1/3/2023 1600",
  "list",
  "bye"
]
```

Complete console output:

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
 Got it. I've added this task:
   [T][ ] read
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use date format d/M/yyyy, for example 2/12/2019.
____________________________________________________________
____________________________________________________________
 Error: use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800.
____________________________________________________________
____________________________________________________________
 Error: use event date/time format d/M/yyyy HHmm for both /from and /to.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read
____________________________________________________________
____________________________________________________________
See you later. Complete your tasks on time!
____________________________________________________________
```

### R2, session 1: PASS

Console input (one array element per line; an empty array means EOF):

```json
[
  "list",
  "bye"
]
```

Complete console output:

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

### P1, session 1: PASS

Console input (one array element per line; an empty array means EOF):

```json
[
  "list",
  "todo read book",
  "deadline return book /by 2/12/2019 1800",
  "event project meeting /from 2/12/2019 1400 /to 4/12/2019 1600",
  "list",
  "mark 2",
  "unmark 2",
  "mark 1",
  "delete 2",
  "list",
  "on 3/12/2019",
  "on 1/12/2019",
  "bye"
]
```

Complete console output:

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

Console input (one array element per line; an empty array means EOF):

```json
[
  "list",
  "on 3/12/2019",
  "bye"
]
```

Complete console output:

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

Console input (one array element per line; an empty array means EOF):

```json
[
  "",
  "blah",
  "todo",
  "deadline homework",
  "event meeting /from 2pm",
  "mark abc",
  "delete 0",
  "deadline return book /by tomorrow",
  "event meeting /from 2/12/2019 /to 2/12/2019 1600",
  "on tomorrow",
  "list",
  "bye"
]
```

Complete console output:

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

Console input (one array element per line; an empty array means EOF):

```json
[
  "list",
  "bye"
]
```

Complete console output:

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

Console input (one array element per line; an empty array means EOF):

```json
[]
```

Complete console output:

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
## Javadoc-only verification — 2026-09-27

Reviewed the existing UI cases and JUnit selection: no expected behavior or assertions
needed changing. Java sources differ only in comments. Java 25 Gradle
`test javadoc shadowJar --rerun-tasks` succeeded; all 52 JUnit tests passed.
Javadoc generation reported 32 warnings for undocumented fields, enum constants,
and implicit constructors, not the explicitly declared methods covered by this update.
The test-ui skill ran R1, R2, P1–P4 using the rebuilt JAR in isolated directories.
All six cases/seven sessions passed exact output, exit-status, and saved-data checks.
Inputs are JSON arrays (one element per input line; an empty array means EOF).
All actual output below matched the existing expected output, ignoring line endings only.

### R1, session 1: PASS

Console input:

```json
[
  "todo read",
  "mark",
  "unmark",
  "mark ",
  "unmark ",
  "on 29/2/2023",
  "deadline invalid /by 31/4/2024 1800",
  "event invalid /from 29/2/2023 1400 /to 1/3/2023 1600",
  "list",
  "bye"
]
```

Complete console output:

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
 Got it. I've added this task:
   [T][ ] read
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use 'mark N' or 'unmark N', where N is a task number.
____________________________________________________________
____________________________________________________________
 Error: use date format d/M/yyyy, for example 2/12/2019.
____________________________________________________________
____________________________________________________________
 Error: use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800.
____________________________________________________________
____________________________________________________________
 Error: use event date/time format d/M/yyyy HHmm for both /from and /to.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read
____________________________________________________________
____________________________________________________________
See you later. Complete your tasks on time!
____________________________________________________________
```

### R2, session 1: PASS

Console input:

```json
[
  "list",
  "bye"
]
```

Complete console output:

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

### P1, session 1: PASS

Console input:

```json
[
  "list",
  "todo read book",
  "deadline return book /by 2/12/2019 1800",
  "event project meeting /from 2/12/2019 1400 /to 4/12/2019 1600",
  "list",
  "mark 2",
  "unmark 2",
  "mark 1",
  "delete 2",
  "list",
  "on 3/12/2019",
  "on 1/12/2019",
  "bye"
]
```

Complete console output:

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

```json
[
  "list",
  "on 3/12/2019",
  "bye"
]
```

Complete console output:

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

```json
[
  "",
  "blah",
  "todo",
  "deadline homework",
  "event meeting /from 2pm",
  "mark abc",
  "delete 0",
  "deadline return book /by tomorrow",
  "event meeting /from 2/12/2019 /to 2/12/2019 1600",
  "on tomorrow",
  "list",
  "bye"
]
```

Complete console output:

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

```json
[
  "list",
  "bye"
]
```

Complete console output:

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

Console input:

```json
[]
```

Complete console output:

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
## SE-EDU style verification — 2026-09-27 (pending)

Invoked the test-ui workflow and reviewed R1, R2, P1–P4. Their aims, command inputs,
expected console output, and saved data remain unchanged. The entry point is now
`mary.Mary`; the chatbot still displays MARY. Current run instructions and Gradle
configuration use the new Java name; historical transcripts retain their original names.

Compilation and rebuilding the UI-test JAR were not run: permission for the Java 25
Gradle `test javadoc shadowJar --rerun-tasks` command was declined. Consequently,
no UI processes were started and there is no new console session to record.
Earlier PASS records below do not verify this change. Rebuild the JAR before
running these six cases/seven sessions; do not test a stale artifact.

Static review covered 34 Java files: no wildcard imports, over-120-column lines,
tabs, trailing whitespace, detected unbraced bodies, import-order mismatches, or
missing declared-method Javadoc headers were found. These checks do not replace
compilation, JUnit, or the exact-output UI comparisons.
