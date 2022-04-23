package fr.iocean.arrosage.service.mapper;


import fr.iocean.arrosage.domain.*;
import fr.iocean.arrosage.service.dto.ProgrammationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Programmation} and its DTO {@link ProgrammationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProgrammationMapper extends EntityMapper<ProgrammationDTO, Programmation> {



    default Programmation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Programmation programmation = new Programmation();
        programmation.setId(id);
        return programmation;
    }
}
