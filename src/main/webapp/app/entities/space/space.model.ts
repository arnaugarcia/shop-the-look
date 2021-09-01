import { IPhoto } from 'app/entities/photo/photo.model';
import { ICompany } from 'app/features/company/model/company.model';

export interface ISpace {
  name?: string;
  reference?: string;
  active?: boolean | null;
  description?: string | null;
  maxPhotos?: number | null;
  visible?: boolean | null;
  photos?: IPhoto[];
  company?: ICompany;
}

export class Space implements ISpace {
  constructor(
    public name?: string,
    public reference?: string,
    public active?: boolean | null,
    public description?: string | null,
    public maxPhotos?: number | null,
    public visible?: boolean | null,
    public photos?: IPhoto[],
    public company?: ICompany
  ) {
    this.active = this.active ?? false;
    this.visible = this.visible ?? false;
  }
}

export class SpaceRequest {
  constructor(public name?: string, public description?: string | null) {}
}
