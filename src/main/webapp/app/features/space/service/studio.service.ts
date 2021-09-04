import { EventEmitter, Injectable } from '@angular/core';

type CurrentStep = 'template' | 'create' | 'customize' | 'enjoy' | 'publish';

export enum StudioTemplate {
  ONE_PHOTO = 'ONE_PHOTO',
  THREE_PHOTOS_VERTICAL = 'THREE_PHOTOS_VERTICAL',
  THREE_PHOTOS_HORIZONTAL = 'THREE_PHOTOS_HORIZONTAL',
}

interface IStudioData {
  template: StudioTemplate;
}

@Injectable({
  providedIn: 'root',
})
export class StudioService {
  currentStep: CurrentStep = 'create';
  navigation: EventEmitter<string>;
  data: IStudioData;

  constructor() {
    this.navigation = new EventEmitter<string>();
    this.data = {
      template: StudioTemplate.ONE_PHOTO,
    };
  }

  navigate(step: CurrentStep): void {
    this.currentStep = step;
    this.navigation.emit(step);
  }

  setTemplate(template: StudioTemplate): void {
    this.data.template = template;
  }
}
