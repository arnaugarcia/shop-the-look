import { SubscriptionCategory } from 'app/entities/enumerations/subscription-category.model';
import { ICompany } from '../../features/company/model/company.model';

export interface ISubscriptionPlan {
  id?: number;
  name?: string;
  description?: string | null;
  category?: SubscriptionCategory;
  maxProducts?: number;
  maxSpaces?: number;
  maxRequests?: number;
  companies?: ICompany[];
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
    public companies?: ICompany[]
  ) {}
}

export function getSubscriptionPlanIdentifier(subscriptionPlan: ISubscriptionPlan): number | undefined {
  return subscriptionPlan.id;
}
