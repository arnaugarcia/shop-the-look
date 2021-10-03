import { Component, Input } from '@angular/core';

export interface Action {
  name: string;
  link: string;
  icon?: string;
}

@Component({
  selector: 'stl-action',
  templateUrl: './action.component.html',
})
export class ActionComponent {
  @Input() public actions: Action[] = [];
}
