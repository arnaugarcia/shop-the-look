import { Routes } from '@angular/router';

import { ActivateComponent } from './activate.component';

export const activateRoute: Routes = [
  {
    path: '',
    component: ActivateComponent,
    data: {
      pageTitle: 'activate.title',
    },
  },
];
