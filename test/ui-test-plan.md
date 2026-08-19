# Finn UI test plan

Run this plan with `powershell -NoProfile -ExecutionPolicy Bypass -File .codex/skills/test-ui/scripts/run-ui-tests.ps1`. Expected-output blocks contain the complete program output; the prompt and divider appear on the same line because the application uses `print` for the prompt.

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
 ____ ___ _   _ _   _ 
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
deadline do homework /by no idea :-p
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output:
```text
 ____ ___ _   _ _   _ 
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
   [D][ ] do homework (by: no idea :-p)
Now you have 1 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm, to: 4pm)
Now you have 2 task(s) in the list.
____________________________________________________________

What can I do for you? ____________________________________________________________

Here are the tasks in your list:
1.[D][ ] do homework (by: no idea :-p)
2.[E][ ] project meeting (from: Mon 2pm, to: 4pm)
____________________________________________________________

What can I do for you? ____________________________________________________________

Bye. Hope to see you again soon!
```
