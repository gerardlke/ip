package finn;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests {@link Finn}'s constructor and {@link Finn#run()}: the integration
 * point where Parser, Command, TaskList, Storage, and Ui are wired together.
 *
 * <p>Finn exposes no getters, so behaviour is verified end-to-end through
 * captured console output (its only observable interface) with System.in
 * scripted as a sequence of commands. This is deliberately an integration
 * test of the control loop itself, distinct from the unit tests already
 * covering Parser/Command/Storage/Ui in isolation: it specifically checks
 * that (1) the loop recovers from a bad command instead of crashing, and
 * (2) the constructor falls back to an empty list instead of propagating
 * a storage read failure.
 */
class FinnTest {

    @TempDir
    Path tempDir;

    private final ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        originalIn = System.in;
        System.setOut(new PrintStream(capturedOut, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    /** Runs Finn against the given storage file with the given scripted input, and returns everything printed. */
    private String runFinn(String storageFilePath, String scriptedInput) {
        System.setIn(new ByteArrayInputStream(scriptedInput.getBytes(StandardCharsets.UTF_8)));
        new Finn(storageFilePath).run();
        return capturedOut.toString(StandardCharsets.UTF_8);
    }

    private static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    // ---------- run(): normal command dispatch ----------

    @Test
    void run_addListAndExit_producesExpectedOutputSequence() {
        String output = runFinn(
                tempDir.resolve("finn.txt").toString(),
                "todo buy milk\nlist\nbye\n");

        assertTrue(output.contains("Hello! I'm Finn."));
        assertTrue(output.contains("Got it. I've added this task:"));
        assertTrue(output.contains("1.[T][ ] buy milk"));
        assertTrue(output.contains("Bye. Hope to see you again soon!"));
    }

    @Test
    void run_deleteCommand_reflectsRemovalInSubsequentList() {
        String output = runFinn(
                tempDir.resolve("finn.txt").toString(),
                "todo a\ntodo b\ndelete 1\nlist\nbye\n");

        // "a" was removed; "b" shifts into slot 1 and is the only item left.
        assertTrue(output.contains("Now you have 1 task(s) in the list."));
        assertTrue(output.contains("1.[T][ ] b"));
        assertFalse(output.contains("2.[T]"));
    }

    // ---------- run(): error recovery is the critical behaviour here ----------

    @Test
    void run_invalidCommand_showsErrorButLoopContinuesToSubsequentCommands() {
        String output = runFinn(
                tempDir.resolve("finn.txt").toString(),
                "blah\nlist\nbye\n");

        assertTrue(output.contains("Unknown task type"));
        // If the loop had crashed instead of recovering, these would never print.
        assertTrue(output.contains("Here are the tasks in your list:"));
        assertTrue(output.contains("Bye. Hope to see you again soon!"));
    }

    @Test
    void run_multipleConsecutiveInvalidCommands_allRecoveredFrom() {
        String output = runFinn(
                tempDir.resolve("finn.txt").toString(),
                "todo\ndeadline nodate\nmark 99\nbye\n");

        assertTrue(output.contains("Sorry! Please follow the format: todo DESCRIPTION"));
        assertTrue(output.contains("Sorry! Please follow the format: deadline DESCRIPTION /by DATE"));
        assertTrue(output.contains("Bye. Hope to see you again soon!"));
    }

    // ---------- run(): end of input without an explicit "bye" ----------

    @Test
    void run_inputEndsWithoutByeCommand_exitsLoopWithoutGoodbyeMessage() {
        String output = runFinn(tempDir.resolve("finn.txt").toString(), "");

        assertTrue(output.contains("Hello! I'm Finn."));
        assertFalse(output.contains("Bye. Hope to see you again soon!"));
    }

    // ---------- constructor: loading from storage ----------

    @Test
    void constructor_existingValidStorageFile_loadsTasksSuccessfully() throws Exception {
        Path file = tempDir.resolve("preloaded.txt");
        Files.write(file, List.of("T | 0 | " + encode("existing task")));

        String output = runFinn(file.toString(), "list\nbye\n");

        assertTrue(output.contains("1.[T][ ] existing task"));
    }

    @Test
    void constructor_noExistingStorageFile_startsWithEmptyList() {
        String output = runFinn(tempDir.resolve("brand_new.txt").toString(), "list\nbye\n");

        assertTrue(output.contains("Here are the tasks in your list:"));
        assertFalse(output.contains("[T]"));
        assertFalse(output.contains("Error loading tasks"));
    }

    @Test
    void constructor_corruptStorageFile_fallsBackToEmptyListWithErrorMessage() throws Exception {
        Path file = tempDir.resolve("corrupt.txt");
        // Invalid UTF-8 byte sequence: Files.readAllLines() will throw a
        // MalformedInputException (an IOException), exercising Finn's catch block.
        Files.write(file, new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0xFF });

        String output = runFinn(file.toString(), "list\nbye\n");

        assertTrue(output.contains("Error loading tasks from file. Starting with an empty list."));
        // The app must still be usable afterwards, just with an empty list.
        assertTrue(output.contains("Here are the tasks in your list:"));
        assertTrue(output.contains("Bye. Hope to see you again soon!"));
    }
}