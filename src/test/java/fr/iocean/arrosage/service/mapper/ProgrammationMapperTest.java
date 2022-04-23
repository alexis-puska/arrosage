package fr.iocean.arrosage.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProgrammationMapperTest {

    private ProgrammationMapper programmationMapper;

    @BeforeEach
    public void setUp() {
        programmationMapper = new ProgrammationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(programmationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(programmationMapper.fromId(null)).isNull();
    }
}
