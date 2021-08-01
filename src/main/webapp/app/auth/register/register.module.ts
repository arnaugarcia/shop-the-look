import { NgModule } from '@angular/core';
import { RegisterComponent } from './register.component';
import { RouterModule } from '@angular/router';
import { registerRoute } from './register.route';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [RegisterComponent],
  imports: [SharedModule, RouterModule.forChild(registerRoute)],
})
export class RegisterModule {}
