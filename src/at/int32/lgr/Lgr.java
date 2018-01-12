package at.int32.lgr;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Lgr {

	private static List<ILgr> LOGGERS = new ArrayList<ILgr>();
	public static String FORMAT = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS:%1$tL [%2$s]\t%3$s";

	static {
		LOGGERS.add(new ConsoleLgr());
	}

	public static void set(ILgr... loggers) {
		LOGGERS.clear();
		LOGGERS.addAll(Arrays.asList(loggers));
	}

	public static void i(String msg) {
		i("", msg);
	}

	public static void i(String format, String msg) {
		l(Level.INFO, format, msg);
	}

	public static void w(String msg) {
		i("", msg);
	}

	public static void w(String format, String msg) {
		l(Level.WARN, format, msg);
	}

	public static void e(String msg) {
		e("", msg);
	}

	public static void e(Exception e) {
		e(e, "", "");
	}

	public static void e(String format, String msg) {
		e(null, format, msg);
	}

	public static void e(Exception e, String msg) {
		e(e, "", msg);
	}

	public static void e(Exception e, String format, String msg) {
		msg = msg + (msg.isEmpty() ? "" : " ") + getExceptionText(e).trim();
		l(Level.ERROR, format, msg);
	}

	private static String getExceptionText(Exception e) {
		if (e == null)
			return "";

		try {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			return errors.toString();
		} catch (Exception ex) {
			return ex.toString();
		}
	}

	private static void l(Level level, String format, String msg) {
		if (format != null && !format.isEmpty())
			msg = String.format(format, msg);

		Date date = new Date();
		String res = String.format(FORMAT, date, level, msg);
		LogEntry entry = new LogEntry(level, date, res);
		for (ILgr lgr : LOGGERS) {
			try {
				lgr.log(entry);
			} catch (Exception e) {
				System.out.println(String.format(
						"failed to log %s with logger %s", res, lgr));
			}
		}
	}

	private enum Level {
		INFO, WARN, ERROR
	}

	public interface ILgr {
		public void log(LogEntry entry);
	}

	public static class LogEntry {
		private Level level;
		private Date date;
		private String msg;

		public LogEntry(Level level, Date date, String msg) {
			this.level = level;
			this.date = date;
			this.msg = msg;
		}

		public Level getEntry() {
			return this.level;
		}

		public String getMessage() {
			return this.msg;
		}

		public Date getDate() {
			return this.date;
		}

		@Override
		public String toString() {
			return this.getMessage();
		}
	}

	public static class ConsoleLgr implements ILgr {
		@Override
		public void log(LogEntry msg) {
			System.out.println(msg);
		}
	}

	public static class FileLgr implements ILgr {

		private Path path;

		public FileLgr(String path) {
			this.path = Paths.get(path);

			try {
				if (!Files.exists(this.path))
					Files.createFile(this.path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void log(LogEntry msg) {
			try {
				Files.write(this.path,
						(msg + System.lineSeparator()).getBytes(),
						StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
