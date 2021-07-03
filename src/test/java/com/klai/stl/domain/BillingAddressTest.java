package com.klai.stl.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.stl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingAddress.class);
        BillingAddress billingAddress1 = new BillingAddress();
        billingAddress1.setId(1L);
        BillingAddress billingAddress2 = new BillingAddress();
        billingAddress2.setId(billingAddress1.getId());
        assertThat(billingAddress1).isEqualTo(billingAddress2);
        billingAddress2.setId(2L);
        assertThat(billingAddress1).isNotEqualTo(billingAddress2);
        billingAddress1.setId(null);
        assertThat(billingAddress1).isNotEqualTo(billingAddress2);
    }
}
