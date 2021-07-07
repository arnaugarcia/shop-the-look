package com.klai.stl.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.klai.stl.domain.User} entity. This class is used
 * in {@link com.klai.stl.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter name;

    private StringFilter login;

    private String company;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.name = other.name == null ? null : other.name.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.company = other.company;
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getLogin() {
        return login;
    }

    public StringFilter login() {
        if (login == null) {
            login = new StringFilter();
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeCriteria)) return false;
        EmployeeCriteria that = (EmployeeCriteria) o;
        return Objects.equals(name, that.name) && Objects.equals(login, that.login) && Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, company);
    }

    @Override
    public String toString() {
        return "EmployeeCriteria{" + "name=" + name + ", login=" + login + ", company=" + company + '}';
    }
}
