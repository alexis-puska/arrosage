package fr.iocean.arrosage.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Properties specific to Arrosage.
 * <p>
 * Properties are configured in the {@code application.yml} file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@Getter
@Setter
@NoArgsConstructor
public class RelayProperties {
    private int relay;
    private String zone;
    private int pin;
}
