import { EventEmitter, Injectable } from '@angular/core';

type CurrentStep = 'template' | 'create' | 'customize' | 'enjoy' | 'publish';

export enum StudioTemplate {
  ONE_PHOTO = 'ONE_PHOTO',
  THREE_PHOTOS_VERTICAL = 'THREE_PHOTOS_VERTICAL',
  THREE_PHOTOS_HORIZONTAL = 'THREE_PHOTOS_HORIZONTAL',
}

interface IStudioData {
  title: string;
  description: string;
  template: StudioTemplate;
}

@Injectable({
  providedIn: 'root',
})
export class StudioService {
  currentStep: CurrentStep = 'create';
  navigation: EventEmitter<string>;
  private _data: IStudioData;

  constructor() {
    this.navigation = new EventEmitter<string>();
    this._data = {
      title: '',
      description: '',
      template: StudioTemplate.ONE_PHOTO,
    };
  }

  navigate(step: CurrentStep): void {
    this.currentStep = step;
    this.navigation.emit(step);
  }

  set template(template: StudioTemplate) {
    this._data.template = template;
  }

  get template(): StudioTemplate {
    return this._data.template;
  }

  get title(): string {
    return this._data.title;
  }

  set title(title: string) {
    this._data.title = title;
  }

  get description(): string {
    return this._data.description;
  }

  set description(description: string) {
    this._data.description = description;
  }
}
