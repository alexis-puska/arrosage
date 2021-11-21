package fr.iocean.arrosage.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BlackListMapperTest {

    private BlackListMapper blackListMapper;

    @BeforeEach
    public void setUp() {
        blackListMapper = new BlackListMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(blackListMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(blackListMapper.fromId(null)).isNull();
    }
}
