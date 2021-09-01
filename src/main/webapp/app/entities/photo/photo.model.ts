import { ICoordinate } from 'app/entities/coordinate/coordinate.model';
import { ISpaceTemplate } from 'app/entities/space-template/space-template.model';
import { PhotoOrientation } from 'app/entities/enumerations/photo-orientation.model';
import { ISpace } from '../../features/space/model/space.model';

export interface IPhoto {
  id?: number;
  name?: string | null;
  description?: string | null;
  link?: string;
  order?: number;
  height?: number;
  width?: number;
  orientation?: PhotoOrientation | null;
  demo?: boolean | null;
  coordinates?: ICoordinate[] | null;
  space?: ISpace | null;
  spaceTemplate?: ISpaceTemplate | null;
}

export class Photo implements IPhoto {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public link?: string,
    public order?: number,
    public height?: number,
    public width?: number,
    public orientation?: PhotoOrientation | null,
    public demo?: boolean | null,
    public coordinates?: ICoordinate[] | null,
    public space?: ISpace | null,
    public spaceTemplate?: ISpaceTemplate | null
  ) {
    this.demo = this.demo ?? false;
  }
}

export function getPhotoIdentifier(photo: IPhoto): number | undefined {
  return photo.id;
}
