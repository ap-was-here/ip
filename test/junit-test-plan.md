# JUnit test selection

Target: focus on approximately the highest-value 50% of implemented methods,
not an exact line-coverage percentage. There are about 45 non-constructor
implemented methods in the current application. The following 23 methods are
the primary targets (roughly 51%); collaborators and accessors may also be
exercised indirectly. No coverage percentage is inferred from test counts.

| Area | Primary methods | Why |
| --- | --- | --- |
| Parser (2) | `parse`, `parseTask` | Dispatch, required arguments, date validation |
| Storage (3) | `load`, `save`, `parseRecord` via `load` | Data integrity, round-trips, corrupted data, I/O errors |
| Task (6) | `markAsDone`, `markAsNotDone`, `setDone`, `parseDate`, `parseDateTime`, `formatDateTime` via display | Completion state and date semantics |
| TaskList (3) | `add`, `remove`, `getTasks` | Ordering, index boundaries, ownership of the collection |
| Task subtypes (5) | `Todo.toString`, `Deadline.toString`, `Deadline.toStorageRecord`, `Event.toString`, `Event.toStorageRecord` | Polymorphic display and persistence contracts |
| Commands (4) | `AddCommand.execute`, `DeleteCommand.execute`, `MarkCommand.execute`, `OnCommand.execute` | State changes, persistence, errors, inclusive date filtering |

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
priority; avoid testing the unused duplicate task parser in `Mary` directly.
An existing defect should be reported with its failing regression test rather
than changing an assertion to bless incorrect behavior.

## Latest execution

### SE-EDU cleanup — pending verification (2026-09-27)

Reviewed the existing highest-value 50% method selection. Style changes retain
the same methods and assertions, with explicit imports and clearer local names;
no additional behavior requires new cases. The entry point is now `mary.Mary`.
Permission to run Java 25 Gradle `test javadoc shadowJar --rerun-tasks` was declined,
so the 52 JUnit tests and UI sessions have not been rerun for this cleanup.
Earlier passing results below apply to the earlier revisions only.

### Earlier Javadoc-only execution

2026-09-27 after Javadoc-only changes: reviewed the prioritized 50% method
selection and existing assertions; no new behavior requires additional tests.
Gradle `test javadoc shadowJar --rerun-tasks` with Java 25 passed all 52 tests.
All six UI cases/seven sessions also passed; complete records are in `ui-test-plan.md`.

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
