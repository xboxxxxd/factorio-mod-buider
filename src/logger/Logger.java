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

	private static Priority logPriority;

	private static Priority consolePriority;
	
	private static PrintWriter writer = null;
	
	private static SimpleDateFormat simple1 = new SimpleDateFormat("dd MM yyyy HH mm ss");
	
	private static SimpleDateFormat simple2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	private Logger() {}

	/**
	 * public static Logger init
	 * @param path Path fuer die Datei
	 * @param logPriority minimum Priority to print
	 * @param consolePriority minimum Priority to print
	 */
	public static void init(String path, Priority logPriority, Priority consolePriority) {
		
		Logger.logPriority = logPriority;
		Logger.consolePriority = consolePriority;
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String daytime = simple1.format(time);
		File dataFile = new File(speicherort);
		dataFile.mkdir();
		
		System.out.println("Logger init Speicherort:" + speicherort);
		String string = speicherort + "\\Benutzer Log " + daytime + ".txt";
		System.out.println("Logger init Speicherort:" + string);
		
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(string)));
		} catch (IOException e) {
			System.out.println("Failld to init logger IOException");
			e.printStackTrace();
		}
	}

	public static void closeFile() {
		writer.close();
	}
	
	private static void log(Priority messagePriority, String line) {
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String daytime = simple2.format(time);
		String massage = daytime + " : " + messagePriority + " : " + line;
		
		if ((messagePriority.compareTo(logPriority) >= 0)  && (messagePriority.compareTo(consolePriority) >= 0)) {
			System.out.println(massage);
			writer.println(massage);
			writer.flush();
		} else if (messagePriority.compareTo(logPriority) >= 0) {
			writer.println(massage);
			writer.flush();
		} else if (messagePriority.compareTo(consolePriority) >= 0) {
			System.out.println(massage);
		}
	}

	/**
	 * 
	 * @param messagePriority
	 * @param klasse
	 * @param method
	 * @param warum
	 * @param nachricht
	 */
	public static void log(Priority messagePriority, String klasse, String method, String warum, String nachricht) {
		log(messagePriority, klasse + " -> " + method + " : " + warum + " -> " + nachricht);
	}
}
