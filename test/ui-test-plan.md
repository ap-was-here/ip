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
