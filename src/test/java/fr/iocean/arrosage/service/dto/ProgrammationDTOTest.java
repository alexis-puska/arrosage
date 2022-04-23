package fr.iocean.arrosage.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.iocean.arrosage.web.rest.TestUtil;

public class ProgrammationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgrammationDTO.class);
        ProgrammationDTO programmationDTO1 = new ProgrammationDTO();
        programmationDTO1.setId(1L);
        ProgrammationDTO programmationDTO2 = new ProgrammationDTO();
        assertThat(programmationDTO1).isNotEqualTo(programmationDTO2);
        programmationDTO2.setId(programmationDTO1.getId());
        assertThat(programmationDTO1).isEqualTo(programmationDTO2);
        programmationDTO2.setId(2L);
        assertThat(programmationDTO1).isNotEqualTo(programmationDTO2);
        programmationDTO1.setId(null);
        assertThat(programmationDTO1).isNotEqualTo(programmationDTO2);
    }
}
