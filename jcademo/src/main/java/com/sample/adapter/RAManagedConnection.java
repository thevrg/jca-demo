package com.sample.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

/**
 *
 * RAManagedConnection
 *
 *
 *
 * @version $Revision: $
 *
 */
public class RAManagedConnection implements ManagedConnection {

    private static final AtomicInteger counter = new AtomicInteger();
    /**
     * The logger
     */
    private static Logger log = Logger.getLogger("RAManagedConnection");

    /**
     * MCF
     */
    private RAManagedConnectionFactory mcf;

    /**
     * Log writer
     */
    private PrintWriter logWriter;

    /**
     * Listeners
     */
    private List<ConnectionEventListener> listeners;

    /**
     * Connection
     */
    private Object connection;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int id = counter.incrementAndGet();

    /**
     *
     * default constructor
     *
     * @param mcf mcf
     *
     */
    public RAManagedConnection(RAManagedConnectionFactory mcf) {

        this.mcf = mcf;

        this.logWriter = null;

        this.listeners = new ArrayList<>(1);

        this.connection = null;

        try {
            socket = new Socket(mcf.getHost(), mcf.getPort());
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     *
     * Creates a new connection handle for the underlying physical connection *
     * represented by the ManagedConnection instance. *
     *
     *
     * @param subject Security context as JAAS subject
     *
     * @param cxRequestInfo ConnectionRequestInfo instance
     *
     * @return generic Object instance representing the connection handle.
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    public Object getConnection(Subject subject,
            ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {

        if (socket == null || socket.isInputShutdown() || socket.isOutputShutdown() || socket.isClosed()) {
            throw new ResourceException("Phisical connection is terminated");
        }
        connection = new RAConnectionImpl(this, mcf);

        return connection;

    }

    /**
     *
     * Used by the container to change the association of an * application-level
     * connection handle with a ManagedConneciton instance.
     *
     *
     *
     * @param connection Application-level connection handle
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    @Override
    public void associateConnection(Object connection) throws ResourceException {

        this.connection = connection;

    }

    /**
     *
     * Application server calls this method to force any cleanup on * the
     * ManagedConnection instance.
     *
     *
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    @Override
    public void cleanup() throws ResourceException {

    }

    /**
     *
     * Destroys the physical connection to the underlying resource manager.
     *
     *
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    @Override
    public void destroy() throws ResourceException {

        this.connection = null;

        try {
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     *
     * Adds a connection event listener to the ManagedConnection instance.
     *
     *
     *
     * @param listener A new ConnectionEventListener to be registered
     *
     */
    public void addConnectionEventListener(ConnectionEventListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException("Listener is null");
        }

        listeners.add(listener);

    }

    /**
     *
     * Removes an already registered connection event listener * from the
     * ManagedConnection instance.
     *
     *
     *
     * @param listener Already registered connection event listener to be
     * removed
     *
     */
    public void removeConnectionEventListener(ConnectionEventListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException("Listener is null");
        }

        listeners.remove(listener);

    }

    /**
     *
     * Gets the log writer for this ManagedConnection instance.
     *
     *
     *
     * @return Character ourput stream associated with this * Managed-Connection
     * instance
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    public PrintWriter getLogWriter() throws ResourceException {

        return logWriter;

    }

    /**
     *
     * Sets the log writer for this ManagedConnection instance.
     *
     *
     *
     * @param out Character Output stream to be associated
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {

        this.logWriter = out;

    }

    /**
     *
     * Returns an <code>javax.resource.spi.LocalTransaction</code> instance.
     *
     *
     *
     * @return LocalTransaction instance
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {

        throw new NotSupportedException("LocalTransaction not supported");

    }

    /**
     *
     * Returns an <code>javax.transaction.xa.XAresource</code> instance. *
     *
     *
     * @return XAResource instance
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    public XAResource getXAResource() throws ResourceException {

        throw new NotSupportedException("GetXAResource not supported");

    }

    /**
     *
     * Gets the metadata information for this connection's underlying * EIS
     * resource manager instance. *
     *
     *
     * @return ManagedConnectionMetaData instance
     *
     * @throws ResourceException generic exception if operation fails
     *
     */
    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {

        return new RAManagedConnectionMetaData();

    }

    /**
     *
     * Call write
     *
     * @param name String text
     *
     * @return returnVal *
     */
    String write(String text) {
        log.log(Level.INFO, "MC#{0}: write({1})", new Object[]{id, text});
        String returnVal = null;
        try {
            out.println(text);
            out.flush();
            returnVal = in.readLine();
            if (returnVal == null) {
                socket = null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                socket = null;
            }
        }

        return returnVal;

    }

    String write() {
        return write(mcf.getName());
    }

    /**
     *
     * Close handle
     *
     * @param handle The handle
     *
     */
    void closeHandle(RAConnection handle) {

        ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);

        event.setConnectionHandle(handle);

        for (ConnectionEventListener cel : listeners) {

            cel.connectionClosed(event);

        }

    }

}
