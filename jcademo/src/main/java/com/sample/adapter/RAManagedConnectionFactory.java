package com.sample.adapter;

import java.io.PrintWriter;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.security.auth.Subject;

/**
 *
 * RAManagedConnectionFactory
 *
 *
 *
 * @version $Revision: $
 *
 */
@ConnectionDefinition(connectionFactory = RAConnectionFactory.class,
        connectionFactoryImpl = RAConnectionFactoryImpl.class,
        connection = RAConnection.class,
        connectionImpl = RAConnectionImpl.class)

public class RAManagedConnectionFactory
        implements ManagedConnectionFactory, ResourceAdapterAssociation {

    /**
     * The serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The logger
     */
    private static Logger log = Logger.getLogger("RAManagedConnectionFactory");
    
    private String name;
    private String host = "localhost";
    private Integer port;

    /**
     * The resource adapter
     */
    private ResourceAdapter ra;

    /**
     * The logwriter
     */
    private PrintWriter logwriter;

    /**
     *
     * Default constructor
     *
     */
    public RAManagedConnectionFactory() {

        this.ra = null;

        this.logwriter = null;

    }
    
    @ConfigProperty(defaultValue = "DefaultMessage", supportsDynamicUpdates = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    @ConfigProperty(defaultValue = "6789", type = Integer.class)
    public void setPort(Integer port) {
        this.port = port;
    }
    

    /**
     *
     * Creates a Connection Factory instance.      *
     *
     *
     * @return EIS-specific Connection Factory instance or      *
     * javax.resource.cci.ConnectionFactory instance
     *
     * @throws ResourceException Generic exception
     *
     */
    @Override
    public Object createConnectionFactory() throws ResourceException {

        throw new ResourceException("This resource adapter doesn't support non-managed environments");

    }

    /**
     *
     * Creates a Connection Factory instance.      *
     *
     *
     * @param cxManager ConnectionManager to be associated with created EIS      *
     * connection factory instance
     *
     * @return EIS-specific Connection Factory instance or      *
     * javax.resource.cci.ConnectionFactory instance
     *
     * @throws ResourceException Generic exception
     *
     */
    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {

        return new RAConnectionFactoryImpl(this, cxManager);

    }

    /**
     *
     * Creates a new physical connection to the underlying EIS resource manager.
     *
     *
     *
     * @param subject Caller's security information
     *
     * @param cxRequestInfo Additional resource adapter specific connection      *
     * request information
     *
     * @throws ResourceException generic exception
     *
     * @return ManagedConnection instance      *
     */
    @Override
    public ManagedConnection createManagedConnection(Subject subject,
            ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {

        log.info("RAManagedConnectionFactory created managed connection ");
        return new RAManagedConnection(this);

    }

    /**
     *
     * Returns a matched connection from the candidate set of connections.      *
     *
     *
     * @param connectionSet Candidate connection set
     *
     * @param subject Caller's security information
     *
     * @param cxRequestInfo Additional resource adapter specific connection
     * request information
     *
     * @throws ResourceException generic exception
     *
     * @return ManagedConnection if resource adapter finds an acceptable match
     * otherwise null      *
     */
    @Override
    public ManagedConnection matchManagedConnections(Set connectionSet,
            Subject subject, ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {

        Set<ManagedConnection>candidateConnectionSet = connectionSet;
        
        Optional<ManagedConnection> firstMC = candidateConnectionSet.stream()
                .filter((mc) -> mc instanceof RAManagedConnection)
                .findFirst();
        if (firstMC.isPresent()) {
            return firstMC.get();
        } else {
            return null;
        }
        
//        ManagedConnection result = null;
//
//        Iterator it = connectionSet.iterator();
//
//        while (result == null && it.hasNext()) {
//
//            ManagedConnection mc = (ManagedConnection) it.next();
//
//            if (mc instanceof RAManagedConnection) {
//
//                RAManagedConnection hwmc = (RAManagedConnection) mc;
//
//                result = hwmc;
//
//            }
//
//        }
//
//        return result;

    }

    /**
     *
     * Get the log writer for this ManagedConnectionFactory instance.
     *
     *
     *
     * @return PrintWriter
     *
     * @throws ResourceException generic exception
     *
     */
    @Override
    public PrintWriter getLogWriter() throws ResourceException {

        return logwriter;

    }

    /**
     *
     * Set the log writer for this ManagedConnectionFactory instance.
     *
     *
     *
     * @param out PrintWriter - an out stream for error logging and tracing
     *
     * @throws ResourceException generic exception
     *
     */
    public void setLogWriter(PrintWriter out) throws ResourceException {

        logwriter = out;

    }

    /**
     *
     * Get the resource adapter
     *
     *
     *
     * @return The handle
     *
     */
    @Override
    public ResourceAdapter getResourceAdapter() {

        return ra;

    }

    /**
     *
     * Set the resource adapter
     *
     *
     *
     * @param ra The handle
     *
     */
    @Override
    public void setResourceAdapter(ResourceAdapter ra) {

        this.ra = ra;
    }
    

    
    /**      *
     * Returns a hash code value for the object.
     *
     * @return A hash code value for this object.
     *
     */
    @Override

    public int hashCode() {

        int result = 17;

        return result;

    }

    /**      *
     * Indicates whether some other object is equal to this one.
     *
     * @param other The reference object with which to compare.
     *
     * @return true If this object is the same as the obj argument, false
     * otherwise.
     *
     */
    @Override

    public boolean equals(Object other) {

        if (other == null) {
            return false;
        }

        if (other == this) {
            return true;
        }

        if (!(other instanceof RAManagedConnectionFactory)) {
            return false;
        }

        RAManagedConnectionFactory obj = (RAManagedConnectionFactory) other;

        boolean result = true;

        return result;

    }

}
