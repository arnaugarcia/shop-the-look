package com.klai.stl.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.klai.stl.domain.BillingAddress} entity.
 */
public class BillingAddressDTO implements Serializable {

    private String address;

    private String city;

    private String province;

    private String zipCode;

    private String country;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillingAddressDTO)) return false;
        BillingAddressDTO that = (BillingAddressDTO) o;
        return (
            Objects.equals(getAddress(), that.getAddress()) &&
            Objects.equals(getCity(), that.getCity()) &&
            Objects.equals(getProvince(), that.getProvince()) &&
            Objects.equals(getZipCode(), that.getZipCode()) &&
            Objects.equals(getCountry(), that.getCountry())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getCity(), getProvince(), getZipCode(), getCountry());
    }

    @Override
    public String toString() {
        return (
            "BillingAddressDTO{" +
            "address='" +
            address +
            '\'' +
            ", city='" +
            city +
            '\'' +
            ", province='" +
            province +
            '\'' +
            ", zipCode='" +
            zipCode +
            '\'' +
            ", country='" +
            country +
            '\'' +
            '}'
        );
    }
}
