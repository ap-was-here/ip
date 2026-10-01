# JUnit test selection

Target: focus on approximately the highest-value 50% of implemented methods,
not an exact line-coverage percentage. There are about 45 non-constructor
implemented methods before search support, plus three search methods. The following
26 methods are the primary targets (roughly 54% of 48); collaborators and accessors may also be
exercised indirectly. No coverage percentage is inferred from test counts.

| Area | Primary methods | Why |
| --- | --- | --- |
| Parser (2) | `parse`, `parseTask` | Dispatch, required arguments, date validation |
| Storage (3) | `load`, `save`, `parseRecord` via `load` | Data integrity, round-trips, corrupted data, I/O errors |
| Task (6) | `markAsDone`, `markAsNotDone`, `setDone`, `parseDate`, `parseDateTime`, `formatDateTime` via display | Completion state and date semantics |
| TaskList (3) | `add`, `remove`, `getTasks` | Ordering, index boundaries, ownership of the collection |
| Task subtypes (5) | `Todo.toString`, `Deadline.toString`, `Deadline.toStorageRecord`, `Event.toString`, `Event.toStorageRecord` | Polymorphic display and persistence contracts |
| Commands (4) | `AddCommand.execute`, `DeleteCommand.execute`, `MarkCommand.execute`, `OnCommand.execute` | State changes, persistence, errors, inclusive date filtering |
| Search (3) | `Task.matchesDescription`, `TaskList.find`, `FindCommand.execute` | Literal matching, locale independence, stable results, input validation, no writes |

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
priority; avoid testing the unused duplicate task parser in `MARY` directly.
An existing defect should be reported with its failing regression test rather
than changing an assertion to bless incorrect behavior.

## Latest execution

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
