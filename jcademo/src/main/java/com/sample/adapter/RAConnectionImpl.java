package com.sample.adapter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * RAConnectionImpl
 *
 *
 *
 * @version $Revision: $
 *
 */
public class RAConnectionImpl implements RAConnection {

    private static final AtomicInteger counter = new AtomicInteger();
    /**
     * The logger
     */
    private static Logger log = Logger.getLogger("RAConnectionImpl");

    /**
     * ManagedConnection
     */
    private RAManagedConnection mc;

    /**
     * ManagedConnectionFactory
     */
    private RAManagedConnectionFactory mcf;
    
    private int id = counter.incrementAndGet();


    public RAConnectionImpl(RAManagedConnection mc,
            RAManagedConnectionFactory mcf) {

        this.mc = mc;

        this.mcf = mcf;
        log.log(Level.INFO, "RAConnectionImpl #{0}created", id);

    }

    public String write() {
        log.log(Level.INFO, "conn#{0}.write()", id);

        return mc.write();

    }

    public String write(String name) {
        log.log(Level.INFO, "conn#{0}.write({1})", new Object[]{id, name});

        return mc.write(name);

    }

    /**
     *
     * Close
     *
     */
    public void close() {

        mc.closeHandle(this);

    }

}
