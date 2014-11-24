package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Logger {

	private String speicherort;
	private static Priority priority;
	private static PrintWriter writer = null;
	private SimpleDateFormat simple1 = new SimpleDateFormat("dd MM yyyy HH mm ss");
	private static SimpleDateFormat simple2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	public Logger(String path, Priority messagePriority) {
		this.speicherort = path + "\\Factorio_Mod_Builder";
		Logger.priority = messagePriority;
		init();
		System.out.println(speicherort);
	}

	// Erzeugen
	private void init() {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String daytime = simple1.format(time);
		File dataFile = new File(speicherort);
		dataFile.mkdir();
		try {
			System.out.println(speicherort);
			String string = speicherort + "\\Benutzer Log " + daytime + ".txt";
			System.out.println(string);
			writer = new PrintWriter(new BufferedWriter(new FileWriter(string)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Schliessen des Writers
	public void closeFile() {
		writer.close();
	}
	
	private static void log(Priority messagePriority, String text) {
		if (messagePriority.compareTo(priority) < 0) {
			return;
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String daytime = simple2.format(time);
		System.out.println(daytime + " : " + text);
		writer.println(daytime + " : " + messagePriority + " : " +text);
		writer.flush();
	}

	/**
	 * Der Logger
	 * 
	 * @param klasse
	 * @param was
	 * @param nachricht
	 */
	public static void log(Priority messagePriority, String klasse, String was,
			String nachricht) {
		log(messagePriority, klasse + " -> " + was + " -> " + nachricht);
	}
	
	/**
	 * 
	 * @param messagePriority
	 * @param klasse
	 * @param was
	 * @param warum
	 * @param nachricht
	 */
	public static void logAll(String klasse, String was, String warum,
			String nachricht) {
		log(Priority.ALL, klasse + " -> " + was + " : " + warum + " -> "
				+ nachricht);
	}

	/**
	 * 
	 * @param messagePriority
	 * @param klasse
	 * @param was
	 * @param warum
	 * @param nachricht
	 */
	public static void logJustSo(String klasse, String was, String warum,
			String nachricht) {
		log(Priority.JUSTSO, klasse + " -> " + was + " : " + warum + " -> "
				+ nachricht);
	}

	/**
	 * 
	 * @param klasse
	 * @param was
	 * @param warum
	 * @param nachricht
	 */
	public static void logInfo(String klasse, String was, String warum,
			String nachricht) {
		log(Priority.INFO, klasse + " -> " + was + " : " + warum + " -> "
				+ nachricht);
	}

	/**
	 * 
	 * @param klasse
	 * @param was
	 * @param warum
	 * @param nachricht
	 */
	public static void logErrorMod(String klasse, String was, String warum,
			String nachricht) {
		log(Priority.ERRORMOD, klasse + " -> " + was + " : " + warum + " -> "
				+ nachricht);
	}

	/**
	 * 
	 * @param klasse
	 * @param was
	 * @param warum
	 * @param nachricht
	 */
	public static void logErrorCode(String klasse, String was, String warum,
			String nachricht) {
		log(Priority.ERRORCODE, klasse + " -> " + was + " : " + warum + " -> "
				+ nachricht);
	}

	/**
	 * 
	 * @param klasse
	 * @param was
	 * @param warum
	 * @param nachricht
	 */
	public static void logErrorFatale(String klasse, String was, String warum,
			String nachricht) {
		log(Priority.ERRORFATALE, klasse + " -> " + was + " : " + warum
				+ " -> " + nachricht);
	}
}
