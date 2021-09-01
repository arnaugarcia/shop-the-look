import { EventEmitter, Injectable } from '@angular/core';

type CurrentStep = 'template' | 'create' | 'customize' | 'enjoy' | 'publish';

export type StudioTemplates = 'option1' | 'option2' | 'option3';

interface IStudioData {
  template: StudioTemplates;
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
      template: 'option1',
    };
  }

  navigate(step: CurrentStep): void {
    this.currentStep = step;
    this.navigation.emit(step);
  }

  setTemplate(template: StudioTemplates): void {
    this.data.template = template;
  }
}
