package fr.iocean.arrosage.service.mapper;


import fr.iocean.arrosage.domain.*;
import fr.iocean.arrosage.service.dto.BlackListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BlackList} and its DTO {@link BlackListDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BlackListMapper extends EntityMapper<BlackListDTO, BlackList> {



    default BlackList fromId(Long id) {
        if (id == null) {
            return null;
        }
        BlackList blackList = new BlackList();
        blackList.setId(id);
        return blackList;
    }
}
