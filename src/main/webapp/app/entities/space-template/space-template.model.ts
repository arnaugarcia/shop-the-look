import { IPhoto } from 'app/entities/photo/photo.model';

export interface ISpaceTemplate {
  id?: number;
  name?: string;
  description?: string | null;
  maxProducts?: number;
  maxPhotos?: number;
  active?: boolean | null;
  photos?: IPhoto[] | null;
}

export class SpaceTemplate implements ISpaceTemplate {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public maxProducts?: number,
    public maxPhotos?: number,
    public active?: boolean | null,
    public photos?: IPhoto[] | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getSpaceTemplateIdentifier(spaceTemplate: ISpaceTemplate): number | undefined {
  return spaceTemplate.id;
}
