# lgr
Lgr is yet another Java Logging Framework

## Why another Java Logging Framework?
Because, quite frankly, most logging frameworks are over-engineered and hard to use. Logging should be a "no-brainer" and not yet another tool that requires a couple of hours/days to set up. 

## Installation
1. Copy [Lgr.java](src/at/int32/lgr/Lgr.java) into your project. 

## Usage
```java
import at.int32.lgr;

Lgr.i("Hello World!");
Lgr.w("This is a warning: %s", "Don't try this at home.");

try {
  throw new IllegalArgumentException("error");
} catch (Exception e) {
  Lgr.e(e, "This is a custom error message: %s", "Quite the failure.");
}
```

which will produce the following result:

```java
2018-01-12 13:02:51:056 [INFO]	Hello World!
2018-01-12 13:02:51:110 [WARN]	This is a warning: Don't try this at home.
2018-01-12 13:02:51:113 [ERROR]	This is a custom error message: Quite the failure. java.lang.IllegalArgumentException: error
	at at.int32.lgr.App.main(App.java:9)
```

## Loggers
By default `Lgr` will print to System.out using the integrated `Lgr.ConsoleLgr`class. 

Following loggers are already contained within `Lgr`:
- `Lgr.ConsoleLgr`
- `Lgr.FileLgr`

To write your own custom Lgr simply implemented the `Lgr.ILgr` interface.

To use a different, multiple, or even custom Loggers simply do the following:
```java
Lgr.set(new ConsoleLgr(), new FileLgr("output.log"), new MyCustomLgr());
```

## Format
To change the desired output format use the static `Lgr.FORMAT` field. By default it's formated like this:

```
//Example: 2018-01-12 13:02:51:056 [INFO]	Hello World!
%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS:%1$tL [%2$s]\t%3$s
```

Example: `2018-01-12 13:02:51:056 [INFO]	Hello World!`

- **%1** = Date (java.util.Date)
- **%2** = Level (Lgr.Level)
- **%3** = Message (System.String)
