package it.unibo.almarima.tablut.local.config;


public class ServerConfig {

    private int timeout;
    private int errors;
    private int cache;
    private int repeated;
    private boolean gui;
    
    public ServerConfig(int timeout, int cache, int errors, int repeated, boolean gui) {
        this.timeout = timeout;
        this.cache = cache;
        this.errors = errors;
        this.repeated = repeated;
        this.gui = gui;
    }

    public ServerConfig() {
        this(300, 0, 0, 0, false);
    }

    public int getTimeout() {
        return this.timeout;
    }

    public int getCacheSize() {
        return this.cache;
    }

    public int getMaxErrors() {
        return this.errors;
    }

    public int getRepeated() {
        return this.repeated;
    }

    public boolean getGui() {
        return this.gui;
    }

}