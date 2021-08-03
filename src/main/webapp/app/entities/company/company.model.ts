import { IBillingAddress } from 'app/entities/billing-address/billing-address.model';
import { IGoogleFeedProduct } from 'app/entities/google-feed-product/google-feed-product.model';
import { ISpace } from 'app/entities/space/space.model';
import { IUser } from 'app/entities/user/user.model';
import { ISubscriptionPlan } from 'app/entities/subscription-plan/subscription-plan.model';
import { CompanyType } from 'app/entities/enumerations/company-type.model';
import { CompanyIndustry } from 'app/entities/enumerations/company-industry.model';
import { CompanySize } from 'app/entities/enumerations/company-size.model';
import { IProduct } from '../../features/products/models/product.model';

export interface ICompany {
  id?: number;
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
  billingAddress?: IBillingAddress | null;
  products?: IProduct[];
  importedProducts?: IGoogleFeedProduct[];
  spaces?: ISpace[] | null;
  users?: IUser[];
  subscriptionPlan?: ISubscriptionPlan | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
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
    public companySize?: CompanySize | null,
    public billingAddress?: IBillingAddress | null,
    public products?: IProduct[],
    public importedProducts?: IGoogleFeedProduct[],
    public spaces?: ISpace[] | null,
    public users?: IUser[],
    public subscriptionPlan?: ISubscriptionPlan | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
