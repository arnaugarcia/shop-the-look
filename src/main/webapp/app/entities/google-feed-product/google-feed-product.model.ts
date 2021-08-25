import * as dayjs from 'dayjs';
import { GoogleFeedProductAvailability } from 'app/entities/enumerations/google-feed-product-availability.model';
import { GoogleFeedProductCondition } from 'app/entities/enumerations/google-feed-product-condition.model';
import { GoogleFeedAgeGroup } from 'app/entities/enumerations/google-feed-age-group.model';
import { ICompany } from 'app/features/company/model/company.model';

export interface IGoogleFeedProduct {
  id?: number;
  sku?: string;
  name?: string;
  description?: string;
  link?: string;
  imageLink?: string;
  additionalImageLink?: string | null;
  mobileLink?: string | null;
  availability?: GoogleFeedProductAvailability;
  availabilityDate?: dayjs.Dayjs | null;
  price?: string;
  salePrice?: string;
  brand?: string | null;
  condition?: GoogleFeedProductCondition | null;
  adult?: boolean | null;
  ageGroup?: GoogleFeedAgeGroup | null;
  company?: ICompany;
}

export class GoogleFeedProduct implements IGoogleFeedProduct {
  constructor(
    public id?: number,
    public sku?: string,
    public name?: string,
    public description?: string,
    public link?: string,
    public imageLink?: string,
    public additionalImageLink?: string | null,
    public mobileLink?: string | null,
    public availability?: GoogleFeedProductAvailability,
    public availabilityDate?: dayjs.Dayjs | null,
    public price?: string,
    public salePrice?: string,
    public brand?: string | null,
    public condition?: GoogleFeedProductCondition | null,
    public adult?: boolean | null,
    public ageGroup?: GoogleFeedAgeGroup | null,
    public company?: ICompany
  ) {
    this.adult = this.adult ?? false;
  }
}

export function getGoogleFeedProductIdentifier(googleFeedProduct: IGoogleFeedProduct): number | undefined {
  return googleFeedProduct.id;
}
