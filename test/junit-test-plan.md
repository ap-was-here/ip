# JUnit test selection

Target: focus on approximately the highest-value 50% of implemented methods,
not an exact line-coverage percentage. The pre-GUI selection targets 26 of roughly
48 implemented non-constructor methods. The GUI increment also prioritizes shared
session execution/loading, output routing and GUI submission/rendering instead of
trivial layout factories or accessors. This keeps the focus around the highest-value
half as the code grows; constructors and collaborators are also exercised indirectly.
No coverage percentage is inferred from test counts.

| Area | Primary methods | Why |
| --- | --- | --- |
| Parser (2) | `parse`, `parseTask` | Dispatch, required arguments, date validation |
| Storage (3) | `load`, `save`, `parseRecord` via `load` | Data integrity, round-trips, corrupted data, I/O errors |
| Task (6) | `markAsDone`, `markAsNotDone`, `setDone`, `parseDate`, `parseDateTime`, `formatDateTime` via display | Completion state and date semantics |
| TaskList (3) | `add`, `remove`, `getTasks` | Ordering, index boundaries, ownership of the collection |
| Task subtypes (5) | `Todo.toString`, `Deadline.toString`, `Deadline.toStorageRecord`, `Event.toString`, `Event.toStorageRecord` | Polymorphic display and persistence contracts |
| Commands (4) | `AddCommand.execute`, `DeleteCommand.execute`, `MarkCommand.execute`, `OnCommand.execute` | State changes, persistence, errors, inclusive date filtering |
| Search (3) | `Task.matchesDescription`, `TaskList.find`, `FindCommand.execute` | Literal matching, locale independence, stable results, input validation, no writes |
| Shared session | `Mary.execute`, constructor/loading; `Ui.showMessage`, `showLine` via commands | One output destination, restart persistence, validation and storage errors, no writes after bye |
| GUI interaction | `ChatWindow.submit`, `addMessage`, suggestion action through controls | Send/Enter, errors, close lifecycle, startup errors, draft preservation, resizing/scrolling |
| Grouped replies | `Ui.showMessages` | Zero, one, or multiple messages; order, formatting, Unicode and existing array inputs |

Test files mirror source packages under `src/test/java/mary`, with names such
as `ParserTest.java` and `StorageTest.java`. Test names use
`feature_scenario_expectedBehavior`. Private record parsing is tested through
the public storage API, without reflection. No additional mocking library is
needed. JUnit `@TempDir` isolates every storage test. Console tests restore
`System.out` after each test and rely on JUnit's default sequential execution.
Gradle sets the test JVM's language to English for stable month-name assertions.

Run `gradlew.bat test` with Java 25. See `build/reports/tests/test/index.html`
and `build/test-results/test/TEST-*.xml`. Run the project's `test-ui` workflow
separately: the UI suite verifies the existing end-to-end scenarios and does
not replace unit tests for boundaries and failure paths.

Review and update JUnit tests after every code change to maintain the prioritized
50% target. Leave trivial enums, basic getters, and console decoration to lower
priority. The GUI refactor removes the unused duplicate task parser in `Mary`;
the active parser remains tested in `ParserTest`.
An existing defect should be reported with its failing regression test rather
than changing an assertion to bless incorrect behavior.

## Latest execution

2026-10-03, internal assertions: Java 25 `check guiTest jarGuiSmoke` passed
72 unit tests and eight GUI tests, with zero failures, errors, skipped tests, or
Checkstyle violations. Test JVMs explicitly enable Java assertions. Added tests
for physical file-line numbering after blank lines and rejected off-FX-thread
submission. Existing dispatch, corrupt-file, all-task-type round-trip and GUI
tests exercise the other assertions through normal entry points, without
reflection or artificial production hooks. The prioritized method selection is
unchanged. All eight console cases passed both with and without `-ea` (18 process
sessions); full shared input/output records appear in `ui-test-plan.md`.

### Earlier cross-platform packaging execution

2026-10-03, cross-platform packaging: Java 25 Gradle
`check guiTest jarGuiSmoke --rerun-tasks` passed all 71 unit tests and seven
GUI tests, with no failures or skipped tests and zero Checkstyle violations.
No business logic changed, so the prioritized JUnit method selection is unchanged.
Added `PackagedGuiSmoke`, a standalone test helper run by `jarGuiSmoke`, to render
the real GUI against the fat JAR without development runtime dependencies.
`verifyJar` checks platform classes, native libraries and launch manifest as part
of `check`. The helper passed on Windows and Linux/WSLg with Java 25. macOS was
not executed. See `ui-test-plan.md` for limitations and full console records.

### Earlier varargs execution

2026-10-02, varargs: Java 25 Gradle `check guiTest shadowJar` passed 71 unit
tests and seven GUI tests, with no failures, errors, skipped tests, or Checkstyle
violations. Four new tests cover the grouped-message helper, supplementing the
existing high-value method selection. Console cases R1, R2, P1–P4, F1 and F2
passed in nine sessions with unchanged expected output; the full record is in
`ui-test-plan.md`. The fat JAR was rebuilt.

### Earlier JavaFX execution

GUI validation uses `gradlew.bat check guiTest shadowJar` with Java 25.
`test` covers the 62 existing tests and five shared-session tests without starting
JavaFX. `guiTest` runs seven additional tests tagged `gui` on a graphical desktop,
using temporary files and bounded FX-thread waits; it stops on the first failure.
The GUI suite is deliberately separate from `check` so headless CI can still run
the unit suite. Java sources for both test suites are checked by `checkstyleTest`.
2026-10-02 result: 67 unit tests and seven GUI tests passed, with zero skipped
tests and zero Checkstyle violations. The rendered scene was visually reviewed;
the packaged GUI launched and closed normally in a separate smoke test.
See the JavaFX session record in `ui-test-plan.md` for executed results.

### Earlier Checkstyle execution

2026-10-02, Checkstyle setup: Java 25 Gradle `check shadowJar --rerun-tasks`
passed all 62 tests across 14 test classes (zero failures, errors, or skipped tests).
Both Checkstyle tasks passed with zero violations. Reviewed the prioritized
method selection; it is unchanged because production behavior is unchanged.
Test changes retain all existing checks and expected values while replacing
wildcard imports, correcting wrapping/indentation, and encapsulating fixtures.
Parser dispatch uses individual assertions instead of `assertAll`; a failed
assertion now stops that test method rather than collecting its remaining failures.
UI cases R1, R2, P1–P4, F1 and F2 passed in nine sessions; full input/output is
recorded in `ui-test-plan.md`.

### Earlier keyword-search execution

2026-09-27, keyword search: Java 25 Gradle `test shadowJar --rerun-tasks`
passed all 62 tests. Search tests cover parser boundaries, blank keywords,
case-insensitive literal matching, phrases, punctuation, non-ASCII descriptions,
locale independence, metadata exclusion, duplicate matches, result ownership,
numbering, completion state, and unchanged saved data. UI cases R1, R2, P1–P4,
F1 and F2 also passed (nine sessions); see `ui-test-plan.md` for full records.

### Earlier ASCII banner execution

2026-09-27, ASCII cat banner: Java 25 Gradle `test shadowJar --rerun-tasks`
passed all 53 tests. Added `UiTest.showWelcome_catBanner_printsExactAsciiGreeting`
to verify exact artwork, greeting, separators, and ASCII-only output. The existing
high-value method selection remains unchanged; this focused UI regression test
supplements it. See `ui-test-plan.md` for the end-to-end console records.

### Earlier production-fix execution

2026-09-27 after the production fixes: Gradle `test shadowJar` with Java 25.0.4.1
succeeded. **52 tests passed, 0 failed, 0 skipped** across 11 test classes.
The four formerly failing regression tests remain enabled and now pass.
Mark syntax is validated before slicing, todo record field counts are checked,
and both date parsers use strict resolution. Expanded tests also cover malformed
mark prefixes, extra empty record fields, non-leap-century dates, and hour 24.
The UI suite passed six cases/seven sessions, including the new bug regressions;
complete console records are in `ui-test-plan.md`.

## Initial execution (before fixes)

2026-09-27: Gradle 9.6.1 with Temurin Java 25.0.4.1 compiled and executed
51 tests across 11 test classes: **47 passed, 4 failed, 0 skipped**.
The application source was not modified. The four failing assertions expose
pre-existing defects and remain enabled as regression tests:

| Test | Expected | Actual |
| --- | --- | --- |
| `MarkCommandTest.execute_missingNumber_reportsUsageInsteadOfCrashing` | `mark` without a number prints usage | `StringIndexOutOfBoundsException` before the error handler |
| `StorageTest.load_extraTodoFields_rejectsCorruptRecord` | Reject `T \| 0 \| read \| unexpected` with `MaryException` | Extra field silently accepted |
| `TaskTest.parseDate_impossibleCalendarDate_rejectsRatherThanChangingDate` | Reject `29/2/2023` | No exception; default SMART date resolution accepts it |
| `TaskTest.parseDateTime_impossibleCalendarDate_rejectsRatherThanChangingDate` | Reject `31/4/2024 1800` | No exception; default SMART date resolution accepts it |

At that point `gradlew.bat test` exited with failure. These tests must
not be disabled or changed to expect the defects simply to obtain a green build.
The production fixes described in the latest execution section resolve these
failures without disabling the tests or weakening their expectations.
