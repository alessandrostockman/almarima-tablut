package it.unibo.almarima.tablut.local.control;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;

public class TablutLogger {
    
    private String mainDirectory; 
    private String loggerName;
    private String logName;

    private String baseDirectory; 
    private Path path;

    public TablutLogger(String loggerName, String mainDirectory, String logName) {
        this.loggerName = loggerName;
        this.mainDirectory = mainDirectory;
        this.logName = logName;

		this.baseDirectory = "src/main/java/it/unibo/almarima/tablut/local/match_history";
		this.path = Paths.get(this.baseDirectory + File.separator + this.mainDirectory);
		this.path = this.path.toAbsolutePath();

    }

    public Logger generate() throws AgentStoppedException {
		Logger logger = Logger.getLogger(this.loggerName);

		try {
			File logDir = new File(this.path.toString());
			if (!logDir.exists()) {
				logDir.mkdirs();
			}

			this.path = Paths.get(this.path.toString() + File.separator + this.logName + "_" + new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString() + ".txt");
			
			File log = new File(this.path.toString());
			if (!log.exists()) {
				log.createNewFile();
			}
			FileHandler fh = null;
			fh = new FileHandler(this.path.toString(), true);
			logger.addHandler(fh);
			fh.setFormatter(new TablutFormatter());
            logger.setLevel(Level.FINE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AgentStoppedException();
        }
        
        return logger;
    }

   
}