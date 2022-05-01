# Error Handling

For a variety of reasons, including `null` values from your code, you want to effectively use the [Option](./Options.md)
[Try](./Try.md), and [Either](./Either.md) for handling errors.

Pure functions never throw exceptions, so in practice writing pure functions requires us to wrap exceptions in some
monadic collections like Option, Try or Either

## Summary of Standard Error Handlers

| Base Type | Success Case | Failure Case |
| --------------------------------------- |
| Option    | Some         | Node         |
| Try       | Success      | Failure      |
| Either    | Right        | Left         |


## When to use

Option - When you don't need the error message. Or as a replacement for `null`
Try - Good for wrapping Exceptions
Either - An alternative to Try when you need to keep the error message 
