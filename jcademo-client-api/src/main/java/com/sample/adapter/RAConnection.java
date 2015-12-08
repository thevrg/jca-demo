package com.sample.adapter;

public interface RAConnection extends AutoCloseable {

    /**
     *
     * write
     *
     * @return String
     *
     */
    public String write();

    /**
     *
     * write
     *
     * @param name A name
     *
     * @return String
     *
     */
    public String write(String name);

    /**
     *
     * Close
     *
     */
    @Override
    public void close();

}
