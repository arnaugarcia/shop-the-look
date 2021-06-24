import { IProduct } from 'app/entities/product/product.model';
import { IGoogleFeedProduct } from 'app/entities/google-feed-product/google-feed-product.model';
import { ISpace } from 'app/entities/space/space.model';
import { IUser } from 'app/entities/user/user.model';
import { ISubscriptionPlan } from 'app/entities/subscription-plan/subscription-plan.model';
import { CompanyIndustry } from 'app/entities/enumerations/company-industry.model';
import { CompanySize } from 'app/entities/enumerations/company-size.model';

export interface ICompany {
  id?: number;
  name?: string;
  cif?: string;
  token?: string | null;
  industry?: CompanyIndustry | null;
  companySize?: CompanySize | null;
  products?: IProduct[] | null;
  importedProducts?: IGoogleFeedProduct[] | null;
  spaces?: ISpace[] | null;
  users?: IUser[] | null;
  subscriptionPlan?: ISubscriptionPlan | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public name?: string,
    public cif?: string,
    public token?: string | null,
    public industry?: CompanyIndustry | null,
    public companySize?: CompanySize | null,
    public products?: IProduct[] | null,
    public importedProducts?: IGoogleFeedProduct[] | null,
    public spaces?: ISpace[] | null,
    public users?: IUser[] | null,
    public subscriptionPlan?: ISubscriptionPlan | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
