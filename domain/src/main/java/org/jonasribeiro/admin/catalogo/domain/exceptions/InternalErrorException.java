package org.jonasribeiro.admin.catalogo.domain.exceptions;

public class InternalErrorException extends NoStackTraceException{

    protected InternalErrorException(final String aMessage, final Throwable e ) {
        super(aMessage, e);
    }

    public static InternalErrorException with(final String aMessage, final Throwable e) {
        return new InternalErrorException(aMessage, e);
    }
}
