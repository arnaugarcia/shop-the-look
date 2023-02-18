import { ISpace } from '../../model/space.model';

export type CurrentStep = 'template' | 'create' | 'customize' | 'enjoy';

export enum StudioTemplate {
  ONE_PHOTO = 'ONE_PHOTO',
  TWO_PHOTO = 'TWO_PHOTO',
}

export interface StudioState {
  readonly space: ISpace;
  readonly step: CurrentStep;
  readonly template: StudioTemplate;
}
