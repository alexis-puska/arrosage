package fr.iocean.arrosage.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.iocean.arrosage.web.rest.TestUtil;

public class BlackListTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlackList.class);
        BlackList blackList1 = new BlackList();
        blackList1.setId(1L);
        BlackList blackList2 = new BlackList();
        blackList2.setId(blackList1.getId());
        assertThat(blackList1).isEqualTo(blackList2);
        blackList2.setId(2L);
        assertThat(blackList1).isNotEqualTo(blackList2);
        blackList1.setId(null);
        assertThat(blackList1).isNotEqualTo(blackList2);
    }
}
