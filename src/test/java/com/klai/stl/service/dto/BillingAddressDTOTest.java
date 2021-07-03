package com.klai.stl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.stl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingAddressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingAddressDTO.class);
        BillingAddressDTO billingAddressDTO1 = new BillingAddressDTO();
        billingAddressDTO1.setId(1L);
        BillingAddressDTO billingAddressDTO2 = new BillingAddressDTO();
        assertThat(billingAddressDTO1).isNotEqualTo(billingAddressDTO2);
        billingAddressDTO2.setId(billingAddressDTO1.getId());
        assertThat(billingAddressDTO1).isEqualTo(billingAddressDTO2);
        billingAddressDTO2.setId(2L);
        assertThat(billingAddressDTO1).isNotEqualTo(billingAddressDTO2);
        billingAddressDTO1.setId(null);
        assertThat(billingAddressDTO1).isNotEqualTo(billingAddressDTO2);
    }
}
