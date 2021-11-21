package fr.iocean.arrosage.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.iocean.arrosage.web.rest.TestUtil;

public class BlackListDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlackListDTO.class);
        BlackListDTO blackListDTO1 = new BlackListDTO();
        blackListDTO1.setId(1L);
        BlackListDTO blackListDTO2 = new BlackListDTO();
        assertThat(blackListDTO1).isNotEqualTo(blackListDTO2);
        blackListDTO2.setId(blackListDTO1.getId());
        assertThat(blackListDTO1).isEqualTo(blackListDTO2);
        blackListDTO2.setId(2L);
        assertThat(blackListDTO1).isNotEqualTo(blackListDTO2);
        blackListDTO1.setId(null);
        assertThat(blackListDTO1).isNotEqualTo(blackListDTO2);
    }
}
