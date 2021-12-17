import { CompanyType } from './enumerations/company-type.model';
import { CompanyIndustry } from './enumerations/company-industry.model';
import { CompanySize } from './enumerations/company-size.model';

export interface ICompany {
  name?: string;
  commercialName?: string | null;
  nif?: string;
  logo?: string | null;
  vat?: string | null;
  url?: string;
  phone?: string;
  email?: string;
  type?: CompanyType | null;
  token?: string;
  reference?: string;
  industry?: CompanyIndustry | null;
  companySize?: CompanySize | null;
}

export class Company implements ICompany {
  constructor(
    public name?: string,
    public commercialName?: string | null,
    public nif?: string,
    public logo?: string | null,
    public vat?: string | null,
    public url?: string,
    public phone?: string,
    public email?: string,
    public type?: CompanyType | null,
    public token?: string,
    public reference?: string,
    public industry?: CompanyIndustry | null,
    public companySize?: CompanySize | null
  ) {}
}
