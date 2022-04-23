package fr.iocean.arrosage.service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelayDTO implements Serializable, Comparable<RelayDTO> {

    private static final long serialVersionUID = 852746994060374360L;

    private int id;
    private String zone;

    public int compareTo(RelayDTO o) {

        return 0;
    }

}
