import { IPhoto } from '../../features/space/model/photo.model';

export interface ISpaceTemplate {
  id?: number;
  name?: string;
  description?: string | null;
  maxProducts?: number;
  maxPhotos?: number;
  active?: boolean | null;
  photos?: IPhoto[];
}

export class SpaceTemplate implements ISpaceTemplate {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public maxProducts?: number,
    public maxPhotos?: number,
    public active?: boolean | null,
    public photos?: IPhoto[]
  ) {
    this.active = this.active ?? false;
  }
}

export function getSpaceTemplateIdentifier(spaceTemplate: ISpaceTemplate): number | undefined {
  return spaceTemplate.id;
}
