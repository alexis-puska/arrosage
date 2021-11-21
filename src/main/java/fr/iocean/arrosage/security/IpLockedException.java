package fr.iocean.arrosage.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
public class IpLockedException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public IpLockedException(String message) {
        super(message);
    }

    public IpLockedException(String message, Throwable t) {
        super(message, t);
    }
}
