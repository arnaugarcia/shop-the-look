import { ISpace } from '../../model/space.model';

export type CurrentStep = 'template' | 'create' | 'customize' | 'enjoy';

export enum StudioTemplate {
  ONE_PHOTO = 'ONE_PHOTO',
  THREE_PHOTOS_VERTICAL = 'THREE_PHOTOS_VERTICAL',
  THREE_PHOTOS_HORIZONTAL = 'THREE_PHOTOS_HORIZONTAL',
}

export interface StudioState {
  readonly space: ISpace;
  readonly step: CurrentStep;
  readonly template: StudioTemplate;
}
