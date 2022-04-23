package fr.iocean.arrosage.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.iocean.arrosage.web.rest.TestUtil;

public class ProgrammationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Programmation.class);
        Programmation programmation1 = new Programmation();
        programmation1.setId(1L);
        Programmation programmation2 = new Programmation();
        programmation2.setId(programmation1.getId());
        assertThat(programmation1).isEqualTo(programmation2);
        programmation2.setId(2L);
        assertThat(programmation1).isNotEqualTo(programmation2);
        programmation1.setId(null);
        assertThat(programmation1).isNotEqualTo(programmation2);
    }
}
