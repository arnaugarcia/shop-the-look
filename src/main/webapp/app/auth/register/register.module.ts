import { NgModule } from '@angular/core';
import { RegisterComponent } from './register.component';
import { SharedModule } from '../../shared/shared.module';
import { RouterModule } from '@angular/router';
import { registerRoute } from './register.route';
import { PasswordStrengthBarComponent } from '../password/password-strength-bar/password-strength-bar.component';

@NgModule({
  declarations: [RegisterComponent, PasswordStrengthBarComponent],
  imports: [SharedModule, RouterModule.forChild(registerRoute)],
})
export class RegisterModule {}
