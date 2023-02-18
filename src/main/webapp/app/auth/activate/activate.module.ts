import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { RouterModule } from '@angular/router';
import { activateRoute } from './activate.route';
import { ActivateComponent } from './activate.component';

@NgModule({
  declarations: [ActivateComponent],
  imports: [SharedModule, RouterModule.forChild(activateRoute)],
})
export class ActivateModule {}
