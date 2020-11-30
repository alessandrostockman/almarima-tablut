package it.unibo.almarima.tablut.local.config;


public class ClientConfig {
    
    private int timeout;

    public ClientConfig(int timeout) {
        this.timeout = timeout;
        //TODO: Add firstIter / timeoutMargin ???
    }

    public ClientConfig() {
        this(60);
    }

    public int getTimeout() {
        return this.timeout;
    }
    
}