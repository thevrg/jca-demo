package com.sample.adapter;

import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.TransactionSupport;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
/**
 *
 * SocketResourceAdapter
 *
 *
 *
 * @version $Revision: $
 *
 */
@Connector(
        reauthenticationSupport = false,
        transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction)

public class SocketResourceAdapter implements ResourceAdapter {

    /**
     * The logger
     */
    private static Logger log = Logger.getLogger("SocketResourceAdapter");

    /**
     *
     * Default constructor
     *
     */
    public SocketResourceAdapter() {

    }


    /**
     *
     * This is called during the activation of a message endpoint.
     *
     *
     *
     * @param endpointFactory A message endpoint factory instance.
     *
     * @param spec An activation spec JavaBean instance.
     *
     * @throws ResourceException generic exception      *
     */
    @Override
    public void endpointActivation(MessageEndpointFactory endpointFactory,
            ActivationSpec spec) throws ResourceException {

    }

    /**
     *
     * This is called when a message endpoint is deactivated.      *
     *
     *
     * @param endpointFactory A message endpoint factory instance.
     *
     * @param spec An activation spec JavaBean instance.
     *
     */
    @Override
    public void endpointDeactivation(MessageEndpointFactory endpointFactory,
            ActivationSpec spec) {

    }

    /**
     *
     * This is called when a resource adapter instance is bootstrapped.
     *
     *
     *
     * @param ctx A bootstrap context containing references      *
     * @throws ResourceAdapterInternalException indicates bootstrap failure.
     *
     */
    @Override
    public void start(BootstrapContext ctx)
            throws ResourceAdapterInternalException {
        log.info("Resource Adapter bootstrap!");
    }

    /**
     *
     * This is called when a resource adapter instance is undeployed or
     *
     * during application server shutdown.      *
     */
    @Override
    public void stop() {
        log.info("Resource adapter shutdown!");
    }

    /**
     *
     * This method is called by the application server during crash recovery.
     *
     *
     *
     * @param specs an array of ActivationSpec JavaBeans      *
     * @throws ResourceException generic exception      *
     * @return an array of XAResource objects
     *
     */
    @Override
    public XAResource[] getXAResources(ActivationSpec[] specs)
            throws ResourceException {

        return null;

    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
