import { ICompany } from 'app/entities/company/company.model';
import { SubscriptionCategory } from 'app/entities/enumerations/subscription-category.model';

export interface ISubscriptionPlan {
  id?: number;
  name?: string;
  description?: string | null;
  category?: SubscriptionCategory;
  maxProducts?: number;
  maxSpaces?: number;
  maxRequests?: number;
  companies?: ICompany[] | null;
}

export class SubscriptionPlan implements ISubscriptionPlan {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public category?: SubscriptionCategory,
    public maxProducts?: number,
    public maxSpaces?: number,
    public maxRequests?: number,
    public companies?: ICompany[] | null
  ) {}
}

export function getSubscriptionPlanIdentifier(subscriptionPlan: ISubscriptionPlan): number | undefined {
  return subscriptionPlan.id;
}
