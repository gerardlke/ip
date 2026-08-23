# Finn UI test plan

Run this plan with `powershell -NoProfile -ExecutionPolicy Bypass -File .codex/skills/test-ui/scripts/run-ui-tests.ps1`. Expected-output blocks contain the complete program output; the prompt and divider appear on the same line because the application uses `print` for the prompt. A `␠` represents one required trailing space.

## Test: Todo lifecycle

Aim: Verify that a todo can be added, listed, marked, unmarked, and displayed with its completion status.

Inputs:
```text
todo borrow book
list
mark 1
list
unmark 1
list
bye

```

Expected output:

```text
 ____ ___ _   _ _   _␠
|  __|_ _| \ | | \ | |
| |_  | ||  \| |  \| |
|  _| | || |\  | |\  |
|_|  |___|_| \_|_| \_|
____________________________________________________________

Hello! I'm Finn.
Your personal AI assistant!
____________________________________________________________

What can I do for you? ____________________________________________________________

Got it. I've added this task:
   [T][ ] borrow book
Now you have 1 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Here are the tasks in your list:
1.[T][ ] borrow book
____________________________________________________________

What can I do for you? ____________________________________________________________

Nice! I've marked this task as done:
[T][X] borrow book
____________________________________________________________

What can I do for you? ____________________________________________________________

Here are the tasks in your list:
1.[T][X] borrow book
____________________________________________________________

What can I do for you? ____________________________________________________________

OK, I've marked this task as not done yet:
[T][ ] borrow book
____________________________________________________________

What can I do for you? ____________________________________________________________

Here are the tasks in your list:
1.[T][ ] borrow book
____________________________________________________________

What can I do for you? ____________________________________________________________

Bye. Hope to see you again soon!

```

## Test: Deadline and event details

Aim: Verify that deadline and event date/time text is preserved exactly and each task displays its type-specific details.

Inputs:

```text
deadline do homework /by 2019-10-15
event project meeting /from 2019-10-16 /to 2019-10-17
list
bye

```

Expected output:

```text
 ____ ___ _   _ _   _␠
|  __|_ _| \ | | \ | |
| |_  | ||  \| |  \| |
|  _| | || |\  | |\  |
|_|  |___|_| \_|_| \_|
____________________________________________________________

Hello! I'm Finn.
Your personal AI assistant!
____________________________________________________________

What can I do for you? ____________________________________________________________

Got it. I've added this task:
   [D][ ] do homework (by: Oct 15 2019)
Now you have 1 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Got it. I've added this task:
   [E][ ] project meeting (from: Oct 16 2019, to: Oct 17 2019)
Now you have 2 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Here are the tasks in your list:
1.[D][ ] do homework (by: Oct 15 2019)
2.[E][ ] project meeting (from: Oct 16 2019, to: Oct 17 2019)
____________________________________________________________

What can I do for you? ____________________________________________________________

Bye. Hope to see you again soon!

```

## Test: Invalid command feedback

Aim: Verify that malformed task commands, an invalid task index, and an unknown command show their current guidance without adding tasks.

Inputs:

```text
todo
deadline return book
event project meeting /from Mon 2pm
mark 1
remind me
bye

```

Expected output:

```text
 ____ ___ _   _ _   _␠
|  __|_ _| \ | | \ | |
| |_  | ||  \| |  \| |
|  _| | || |\  | |\  |
|_|  |___|_| \_|_| \_|
____________________________________________________________

Hello! I'm Finn.
Your personal AI assistant!
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please follow the format: todo DESCRIPTION
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please follow the format: deadline DESCRIPTION /by DATE
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please follow the format: event DESCRIPTION /from START /to END
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Invalid task index!
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Unknown task type: remind
____________________________________________________________

What can I do for you? ____________________________________________________________

Bye. Hope to see you again soon!

```

## Test: Delete task lifecycle

Aim: Verify that deleting a task removes it, updates the task count and numbering, and rejects an index beyond the list.

Inputs:

```text
todo first task
todo second task
delete 1
list
delete 5
bye

```

Expected output:

```text
 ____ ___ _   _ _   _␠
|  __|_ _| \ | | \ | |
| |_  | ||  \| |  \| |
|  _| | || |\  | |\  |
|_|  |___|_| \_|_| \_|
____________________________________________________________

Hello! I'm Finn.
Your personal AI assistant!
____________________________________________________________

What can I do for you? ____________________________________________________________

Got it. I've added this task:
   [T][ ] first task
Now you have 1 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Got it. I've added this task:
   [T][ ] second task
Now you have 2 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Oops! I've removed this task:
   [T][ ] first task
Now you have 1 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Here are the tasks in your list:
1.[T][ ] second task
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Invalid task index!
____________________________________________________________

What can I do for you? ____________________________________________________________

Bye. Hope to see you again soon!

```

## Test: Load saved tasks

Aim: Verify that Finn loads valid todo, deadline, and event records while safely ignoring malformed saved records.

Saved tasks:

```text
T | 2 | cmVhZCBib29r
X | 0 | cmVhZCBib29r
T | 0 | not-valid-base64!
T | 0 | cmVhZCBib29r
D | 1 | cmV0dXJuIGJvb2s= | MjAxOS0wNi0wNg==
E | 0 | cHJvamVjdCBtZWV0aW5n | MjAxOS0wOC0wNg== | MjAxOS0wOC0wNw==
E | 0 | b3V0IG9mIG9yZGVy | MjAxOS0wOC0wNw== | MjAxOS0wOC0wNg==

```

Inputs:

```text
list
bye

```

Expected output:

```text
 ____ ___ _   _ _   _␠
|  __|_ _| \ | | \ | |
| |_  | ||  \| |  \| |
|  _| | || |\  | |\  |
|_|  |___|_| \_|_| \_|
____________________________________________________________

Hello! I'm Finn.
Your personal AI assistant!
____________________________________________________________

What can I do for you? ____________________________________________________________

Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] return book (by: Jun 06 2019)
3.[E][ ] project meeting (from: Aug 06 2019, to: Aug 07 2019)
____________________________________________________________

What can I do for you? ____________________________________________________________

Bye. Hope to see you again soon!

```

## Test: Invalid input edge cases

Aim: Verify that blank input, invalid task indexes, incomplete task details, and commands with unexpected arguments show guidance without terminating Finn.

Inputs:

```text

todo one task
mark zero
mark 0
delete -1
deadline /by Friday
deadline study /by 2019-02-29
event meeting /from /to 4pm
event meeting /from 2019-10-16 /to 2019-02-29
event meeting /from 2019-10-17 /to 2019-10-16
list extra
bye later
bye

```

Expected output:

```text
 ____ ___ _   _ _   _␠
|  __|_ _| \ | | \ | |
| |_  | ||  \| |  \| |
|  _| | || |\  | |\  |
|_|  |___|_| \_|_| \_|
____________________________________________________________

Hello! I'm Finn.
Your personal AI assistant!
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please enter a command.
____________________________________________________________

What can I do for you? ____________________________________________________________

Got it. I've added this task:
   [T][ ] one task
Now you have 1 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Invalid task index!
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Invalid task index!
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Invalid task index!
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please follow the format: deadline DESCRIPTION /by DATE
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please use a valid date in the format yyyy-MM-dd.
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please follow the format: event DESCRIPTION /from START /to END
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please use valid dates in the format yyyy-MM-dd.
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! The event end date must not be before its start date.
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please follow the format: list
____________________________________________________________

What can I do for you? ____________________________________________________________

Sorry! Please follow the format: bye
____________________________________________________________

What can I do for you? ____________________________________________________________

Bye. Hope to see you again soon!

```

## Test: Save changed task list

Aim: Verify that task-list changes can be completed without changing the existing console feedback.

Inputs:

```text
todo save this task
mark 1
delete 1
bye

```

Expected output:

```text
 ____ ___ _   _ _   _␠
|  __|_ _| \ | | \ | |
| |_  | ||  \| |  \| |
|  _| | || |\  | |\  |
|_|  |___|_| \_|_| \_|
____________________________________________________________

Hello! I'm Finn.
Your personal AI assistant!
____________________________________________________________

What can I do for you? ____________________________________________________________

Got it. I've added this task:
   [T][ ] save this task
Now you have 1 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Nice! I've marked this task as done:
[T][X] save this task
____________________________________________________________

What can I do for you? ____________________________________________________________

Oops! I've removed this task:
   [T][X] save this task
Now you have 0 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Bye. Hope to see you again soon!

```
