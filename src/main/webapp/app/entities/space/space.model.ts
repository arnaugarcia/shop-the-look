import { IPhoto } from 'app/entities/photo/photo.model';
import { ICompany } from 'app/features/company/model/company.model';

export interface ISpace {
  id?: number;
  name?: string;
  active?: boolean | null;
  reference?: string;
  description?: string | null;
  maxPhotos?: number | null;
  visible?: boolean | null;
  photos?: IPhoto[];
  company?: ICompany;
}

export class Space implements ISpace {
  constructor(
    public id?: number,
    public name?: string,
    public active?: boolean | null,
    public reference?: string,
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

export function getSpaceIdentifier(space: ISpace): number | undefined {
  return space.id;
}
