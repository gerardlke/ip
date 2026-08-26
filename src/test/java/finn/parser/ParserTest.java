package finn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import finn.command.Command;
import finn.exception.ParserException;
import finn.storage.Storage;
import finn.task.Deadline;
import finn.task.Event;
import finn.task.Task;
import finn.task.TaskList;
import finn.task.Todo;
import finn.ui.Ui;

/**
 * Tests {@link Parser#parse(String)}.
 *
 * <p>Commands are exercised end-to-end (parse, then execute against a real
 * {@link TaskList}) rather than by inspecting the returned {@link Command}
 * directly, since Command subclasses intentionally expose no getters.
 * This also verifies that Parser and Command divide validation correctly:
 * some checks (e.g. malformed index) fail at parse time, while others
 * (e.g. out-of-range index) only fail once the command is executed.
 */
class ParserTest {

    @TempDir
    Path tempDir;

    private Ui ui;
    private Storage storage;

    @BeforeEach
    void setUp() {
        ui = new Ui();
        storage = new Storage(tempDir.resolve("finn_test.txt").toString());
    }

    // ---------- Basic input validation ----------

    @Test
    void parse_nullInput_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse(null));
    }

    @Test
    void parse_blankInput_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse("   "));
    }

    @Test
    void parse_unknownCommandWord_throwsParserException() {
        ParserException ex = assertThrows(ParserException.class, () -> Parser.parse("frobnicate stuff"));
        assertTrue(ex.getMessage().contains("Unknown task type"));
    }

    // ---------- bye / list: commands that take no arguments ----------

    @Test
    void parse_bye_returnsExitCommand() throws Exception {
        Command c = Parser.parse("bye");
        assertTrue(c.isExit());
    }

    @Test
    void parse_byeWithTrailingArgs_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse("bye now"));
    }

    @Test
    void parse_list_returnsNonExitingCommand() throws Exception {
        Command c = Parser.parse("list");
        assertFalse(c.isExit());
        // Should not throw when executed against an empty list.
        c.execute(new TaskList(), ui, storage);
    }

    @Test
    void parse_listWithTrailingArgs_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse("list today"));
    }

    // ---------- todo ----------

    @Test
    void parse_todoWithEmptyDescription_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse("todo"));
        assertThrows(ParserException.class, () -> Parser.parse("todo   "));
    }

    @Test
    void parse_validTodo_addsTodoTaskToList() throws Exception {
        Command c = Parser.parse("todo read book");
        TaskList tasks = new TaskList();

        c.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
        assertEquals("read book", tasks.get(0).getName());
    }

    // ---------- deadline ----------

    @Test
    void parse_deadlineMissingByKeyword_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse("deadline submit report"));
    }

    @Test
    void parse_deadlineMissingDescriptionOrDate_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse("deadline /by 2024-01-01"));
        assertThrows(ParserException.class, () -> Parser.parse("deadline submit report /by "));
    }

    @Test
    void parse_deadlineInvalidDateFormat_throwsParserException() {
        ParserException ex = assertThrows(ParserException.class,
                () -> Parser.parse("deadline submit report /by tomorrow"));
        assertTrue(ex.getMessage().contains("valid date"));
    }

    @Test
    void parse_validDeadline_addsDeadlineTaskWithCorrectDate() throws Exception {
        Command c = Parser.parse("deadline submit report /by 2024-03-15");
        TaskList tasks = new TaskList();

        c.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        assertTrue(task instanceof Deadline);
        assertEquals("submit report", task.getName());
        assertEquals("2024-03-15", ((Deadline) task).getDeadline().toString());
    }

    // ---------- event ----------

    @Test
    void parse_eventMissingFromOrTo_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse("event trip"));
        assertThrows(ParserException.class, () -> Parser.parse("event trip /from 2024-01-01"));
    }

    @Test
    void parse_eventEndDateBeforeStartDate_throwsParserException() {
        ParserException ex = assertThrows(ParserException.class,
                () -> Parser.parse("event trip /from 2024-05-10 /to 2024-05-01"));
        assertTrue(ex.getMessage().contains("must not be before"));
    }

    @Test
    void parse_validEvent_addsEventTaskWithCorrectDates() throws Exception {
        Command c = Parser.parse("event trip /from 2024-05-01 /to 2024-05-10");
        TaskList tasks = new TaskList();

        c.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        assertTrue(task instanceof Event);
        assertEquals("2024-05-01", ((Event) task).getStart().toString());
        assertEquals("2024-05-10", ((Event) task).getEnd().toString());
    }

    // ---------- mark / unmark ----------

    @Test
    void parse_markNonNumericIndex_throwsParserExceptionAtParseTime() {
        assertThrows(ParserException.class, () -> Parser.parse("mark abc"));
    }

    @Test
    void parse_markZeroOrNegativeIndex_throwsParserExceptionAtParseTime() {
        assertThrows(ParserException.class, () -> Parser.parse("mark 0"));
        assertThrows(ParserException.class, () -> Parser.parse("mark -1"));
    }

    @Test
    void parse_markOutOfRangeIndex_isAcceptedAtParseTimeButFailsAtExecuteTime() throws Exception {
        // A syntactically valid but semantically invalid index is only
        // rejected once the command is executed against the actual list.
        Command c = Parser.parse("mark 5");
        TaskList emptyTasks = new TaskList();
        assertThrows(ParserException.class, () -> c.execute(emptyTasks, ui, storage));
    }

    @Test
    void parse_validMarkAndUnmark_togglesTaskCompletion() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("water plants"));

        Parser.parse("mark 1").execute(tasks, ui, storage);
        assertTrue(tasks.get(0).isCompleted());

        Parser.parse("unmark 1").execute(tasks, ui, storage);
        assertFalse(tasks.get(0).isCompleted());
    }

    // ---------- delete ----------

    @Test
    void parse_deleteNonNumericIndex_throwsParserExceptionAtParseTime() {
        assertThrows(ParserException.class, () -> Parser.parse("delete xyz"));
    }

    @Test
    void parse_validDelete_removesCorrectTaskAndShrinksList() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));

        Parser.parse("delete 1").execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals("second", tasks.get(0).getName());
    }

    // ---------- find ----------

    @Test
    void parse_findWithEmptyKeyword_throwsParserException() {
        assertThrows(ParserException.class, () -> Parser.parse("find"));
        assertThrows(ParserException.class, () -> Parser.parse("find   "));
    }

    @Test
    void parse_validFind_showsOnlyMatchingTasksWithoutModifyingList() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write report"));

        Parser.parse("find book").execute(tasks, ui, storage);

        // find() is read-only: the original list must be untouched.
        assertEquals(2, tasks.size());
    }
}