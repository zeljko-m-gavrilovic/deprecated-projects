package rs.os.checklist.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

/**
 * Log messages to a file. If file doesn't exist new one will be created.
 * 
 * @author zgavrilovic
 *
 */
public class FileLogger {

	private static FileLogger fileLogger;
	private String logFileName = "/checklist.log";
	private File logFile;
	private LogLevel logLevel;
	enum LogLevel {
		DEBUG, INFO, WARNING, ERROR, FATAL; 
	}
	
	private FileLogger() throws IOException {
		String fileName = Environment.getExternalStorageDirectory() + logFileName;
		logFile = new File(fileName);
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
	}
	
	static public FileLogger getInstance() throws IOException {
		if(fileLogger == null) {
			fileLogger = new FileLogger();
		}
		return fileLogger;
	}
	
	public void logFatal(String logMessage, Throwable throwable) {
		log(logMessage, LogLevel.FATAL);
		log(throwable.getMessage(), LogLevel.FATAL);
	}
	
	public void logError(String logMessage, Throwable throwable) {
		log(logMessage, LogLevel.ERROR);
		log(throwable.getMessage(), LogLevel.ERROR);
	}
	
	public void logWarn(String logMessage) {
		log(logMessage, LogLevel.WARNING);
	}
	
	public void logInfo(String logMessage) {
		log(logMessage, LogLevel.INFO);
	}
	
	public void logDebug(String logMessage) {
		log(logMessage, LogLevel.DEBUG);
	}
	
	public void log(String logMessage, LogLevel ll) {
		if(ll.ordinal() >= logLevel.ordinal()) {
			appendLog(logMessage);
		}
	}
	
	private void appendLog(String text) {
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter  buf = new BufferedWriter(new FileWriter(logFile, true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	
}