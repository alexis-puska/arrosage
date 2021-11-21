package fr.iocean.arrosage.security;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import fr.iocean.arrosage.domain.User;
import fr.iocean.arrosage.repository.UserRepository;
import fr.iocean.arrosage.service.BlackListService;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final BlackListService blackListService;
    private final UserRepository userRepository;

    public DomainUserDetailsService(BlackListService blackListService, UserRepository userRepository) {
        this.blackListService = blackListService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        // check Ip locked
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        if (blackListService.isAdresseLocked(request.getRemoteAddr())) {
            log.info("User : {} with ip {} is locked and try to reconnect", login, request.getRemoteAddr());
            blackListService.updateLastTry(request.getRemoteAddr());
            throw new IpLockedException("Ip locked !");
        }

        if (new EmailValidator().isValid(login, null)) {
            Optional<User> userOptional = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login);
            if (userOptional.isPresent()) {
                return createSpringSecurityUser(login, userOptional.get());
            } else {
                throw new UsernameNotFoundException("User with email " + login + " was not found in the database");
            }
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userOptional = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);
        if (userOptional.isPresent()) {
            return createSpringSecurityUser(lowercaseLogin, userOptional.get());
        } else {
            throw new UsernameNotFoundException("User with email " + login + " was not found in the database");
        }
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin,
            User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                grantedAuthorities);
    }
}
