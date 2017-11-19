# Timber JUnit-Rule

This library provides a JUnit TestRule that plants a temporary Timber tree that pipes any logs sent via Timber to the standard System.out. Once a unit test has completed, the Timber tree is removed to avoid logging unintended test cases.

## Usage
Using the library is very straight forward. An example is as follows:

```java
public class TestExample {
    @Rule
    public TimberTestRule mTimberTestRule = TimberTestRule.builder()
            .minPriority(Log.ERROR)
            .showThread(true)
            .showTimestamp(false)
            .onlyLogWhenTestFails(true)
            .build();
}
```

### Configuration
As seen in the example above, there are many ways to modify the output using the following behaviours:
- The minimum log level to output.
- Whether thread ids are shown.
- Whether timestamps are shown.
- Whether to always log, or only log when a unit test fails.

## Download
This library is available on Maven, you can add it to your project using the following gradle dependencies:

```gradle
testCompile 'net.lachlanmckee:timber-junit-rule:1.0.0'
```
