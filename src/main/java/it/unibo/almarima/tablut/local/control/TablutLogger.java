package it.unibo.almarima.tablut.local.control;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;

public class TablutLogger {

	enum LogSpace {
		GAME,
		SYSTEM,
		WHITE,
		BLACK,
		REPORT
	}

	private static TablutLogger instance = null;
	private static String matchDirectory = "";
	private static String runDirectory = "";
	
	public static void reset() {
		if (instance == null) {
			return;
		}
		
		try {
			TablutLogger tl = TablutLogger.get();
			for (LogSpace s : tl.loggers.keySet()) {
				Logger l = tl.loggers.get(s);
				l.removeHandler(tl.handlers.get(s));
				tl.loggers.remove(s);
			}
		} catch (Exception e) { }
	}
	
	public static void init(Heuristic hWhite, Heuristic hBlack) {
		TablutLogger.matchDirectory = hWhite.toString() + "[W]_vs_" + hBlack.toString() + "[B]_" + (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date());
	}

	public static void setup(int game) {
		TablutLogger.reset();
		TablutLogger.runDirectory = "game_" + game;
	}

	public static TablutLogger get() throws AgentStoppedException {
		if (matchDirectory == "" || runDirectory == "") {
			throw new AgentStoppedException();
		}

		if (instance == null) {
			instance = new TablutLogger();
		}

		return instance;
	}

	public static Logger get(LogSpace space) throws AgentStoppedException {
		return get().getSpace(space);
	}
    
    private String baseDirectory; 
	private Map<LogSpace, Logger> loggers;
	private Map<LogSpace, FileHandler> handlers;

    private TablutLogger() throws AgentStoppedException {
		this.loggers = new HashMap<>();
		this.handlers = new HashMap<>();
		this.baseDirectory = "src/main/java/it/unibo/almarima/tablut/local/match_history";
	}
	
	public Logger getSpace(LogSpace space) throws AgentStoppedException {
		Logger l = this.loggers.get(space);
		if (l == null) {
			this.loggers.put(space, this.generate(space));
			l = this.loggers.get(space);
		}
		return l;
	}

    private Logger generate(LogSpace logSpace) throws AgentStoppedException {
		String logName = logSpace.name().toLowerCase();
        String loggerName = logName.substring(0, 1).toUpperCase() + logName.substring(1) + "Log";
		
		String directory = this.baseDirectory + File.separator + TablutLogger.matchDirectory;
		if (!logSpace.equals(LogSpace.REPORT)) {
			directory += File.separator + TablutLogger.runDirectory;
		}
		Path path = Paths.get(directory);
		path = path.toAbsolutePath();

		Logger logger = Logger.getLogger(loggerName);

		try {
			File logDir = new File(path.toString());
			if (!logDir.exists()) {
				logDir.mkdirs();
			}

			path = Paths.get(path.toString() + File.separator + logName + ".txt");
			
			File log = new File(path.toString());
			if (!log.exists()) {
				log.createNewFile();
			}
			FileHandler fh = null;
			fh = new FileHandler(path.toString(), true);
			logger.addHandler(fh);
			fh.setFormatter(new TablutFormatter());
			logger.setLevel(Level.FINE);
			
			handlers.put(logSpace, fh);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AgentStoppedException();
        }
        
        return logger;
	}

   
}