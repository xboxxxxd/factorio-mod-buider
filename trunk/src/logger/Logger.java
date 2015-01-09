package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Logger {

	private static String speicherort;
	private static String logname;
	private static Priority priorityLog;
	private static Priority priorityCons;
	private static PrintWriter writer = null;
	private static SimpleDateFormat simple1 = new SimpleDateFormat("dd MM yyyy HH mm ss");
	private static SimpleDateFormat simple2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private Logger(String path, Priority priorityLog, Priority priorityCons) {
		Logger.speicherort = path + "\\Factorio_Mod_Builder";
		Logger.priorityLog = priorityLog;
		Logger.priorityCons = priorityCons;
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String daytime = simple1.format(time);
		File dataFile = new File(speicherort);
		dataFile.mkdir();
		
		Logger.logname = speicherort + "\\Benutzer Log " + daytime + ".txt";
		
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(Logger.logname)));
			
			Logger.logINFO("Logger", "Logger", "Speicherort", speicherort);
			Logger.logINFO("Logger", "Logger", "Logname", Logger.logname);
			Logger.logINFO("Logger", "Logger", "start", "end");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	// Erzeugen
	public static void init(String path, Priority priorityLog, Priority priorityCons) {
		new Logger(path, priorityLog, priorityCons);
	}

	// Schliessen des Loggers
	public static void closeFile() {
		writer.close();
	}
	
	private static void log(Priority messagePriority, String text) {
		
		if ((messagePriority.compareTo(Logger.priorityLog) < 0) && (messagePriority.compareTo(Logger.priorityCons) < 0)) {
			return;
		}
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String daytime = simple2.format(time);
		String log = daytime + " : " + messagePriority + " : " + text;
		
		if (messagePriority.compareTo(Logger.priorityLog) >= 0) {
			Logger.writer.println(log);
			Logger.writer.flush();
		}
		
		if (messagePriority.compareTo(Logger.priorityCons) >= 0) {
			System.out.println(log);
		}
		
		return;
	}

	/**
	 * 
	 * @param messagePriority
	 * @param klasse
	 * @param was
	 * @param warum
	 * @param nachricht
	 */
	public static void log(Priority messagePriority, String klasse, String was, String warum, String nachricht) {
		log(messagePriority, klasse + " -> " + was + " -> " + warum + " -> " + nachricht);
	}
	
	public static void logJUSTSO(String klasse, String was, String warum, String nachricht) {
		Logger.log(Priority.JUSTSO, klasse, was, warum, nachricht);
	}
	
	public static void logINFO(String klasse, String was, String warum, String nachricht) {
		Logger.log(Priority.INFO, klasse, was, warum, nachricht);
	}
	
	public static void logDEBUG(String klasse, String was, String warum, String nachricht) {
		Logger.log(Priority.DEBUG, klasse, was, warum, nachricht);
	}
	
	public static void logERRORMOD(String klasse, String was, String warum, String nachricht) {
		Logger.log(Priority.ERRORMOD, klasse, was, warum, nachricht);
	}
	
	public static void logERRORCODE(String klasse, String was, String warum, String nachricht) {
		Logger.log(Priority.ERRORCODE, klasse, was, warum, nachricht);
	}
	
	public static void logERRORFATALE(String klasse, String was, String warum, String nachricht) {
		Logger.log(Priority.ERRORFATALE, klasse, was, warum, nachricht);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
