import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { CurrentStep, StudioState, StudioTemplate } from './models/state.model';
import { Observable } from 'rxjs';
import { EmptySpace } from '../model/space.model';

@Injectable()
export class StudioStore extends ComponentStore<StudioState> {
  public readonly currentStep$: Observable<CurrentStep> = this.select(state => state.step);
  public readonly template$: Observable<StudioTemplate> = this.select(state => state.template);

  public readonly navigate = this.updater((state: StudioState, step: CurrentStep) => ({
    space: state.space,
    step: step,
    template: state.template,
  }));

  public readonly template = this.updater((state: StudioState, template: StudioTemplate) => ({
    space: state.space,
    step: state.step,
    template: template,
  }));

  constructor() {
    super({ space: new EmptySpace(), step: 'create', template: StudioTemplate.ONE_PHOTO });
  }
}
