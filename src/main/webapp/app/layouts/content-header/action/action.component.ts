import { Component } from '@angular/core';

export interface Action {
  name: string;
  link: string;
  icon?: string;
}

@Component({
  selector: 'stl-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.scss'],
})
export class ActionComponent {}
