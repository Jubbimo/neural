package com.dockdev.neural;

import java.util.Calendar;

public class Logger {

	public enum LogType {
		DEBUG, INFO, WARN, ERROR, FATAL, TRACE
	}
	
	@SuppressWarnings(
		"static-method"
	)
	public void log(LogType type, String msg) {
		System.out.print(String.format("[%s] %s %s: %s%n",
				Thread.currentThread().getName(),
				Calendar.getInstance().getTime().toString().split(" ")[3],
				type.name(),
				msg
			)
		);
	}
	
	public void debug(String msg) {
		log(LogType.DEBUG, msg);
	}
	public void info(String msg) {
		log(LogType.INFO, msg);
	}
	public void warn(String msg) {
		log(LogType.WARN, msg);
	}
	public void error(String msg) {
		log(LogType.ERROR, msg);
	}
	public void fatal(String msg) {
		log(LogType.FATAL, msg);
	}
	public void trace(String msg) {
		log(LogType.TRACE, msg);
	}
}
