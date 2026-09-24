# UI Test Plan

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
