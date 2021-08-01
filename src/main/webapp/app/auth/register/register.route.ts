import { Routes } from '@angular/router';

import { RegisterComponent } from './register.component';

export const registerRoute: Routes = [
  {
    path: '',
    component: RegisterComponent,
    data: {
      pageTitle: 'register.title',
    },
  },
];
