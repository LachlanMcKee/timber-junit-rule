# Timber JUnit-Rule

This library provides a JUnit TestRule that plants a temporary Timber tree that pipes any logs sent via Timber to the standard System.out. Once a unit test has completed, the Timber tree is removed to avoid logging unintended test cases.

## Download
This library is available on Maven, you can add it to your project using the following gradle dependencies:

```gradle
testCompile 'net.lachlanmckee:timber-junit:1.0.0'
```
