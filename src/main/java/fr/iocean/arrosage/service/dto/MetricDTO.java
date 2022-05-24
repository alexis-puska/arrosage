package fr.iocean.arrosage.service.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class MetricDTO implements Serializable {

    private static final long serialVersionUID = -2450424919545451324L;

    private int zone;
    private int blackList;
    private int programmation;

}
