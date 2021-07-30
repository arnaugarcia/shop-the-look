import { NgModule } from '@angular/core';
import { ActivateComponent } from './activate/activate.component';
import { RegisterComponent } from './register/register.component';
import { PasswordComponent } from './password/password.component';
import { PasswordStrengthBarComponent } from './password/password-strength-bar/password-strength-bar.component';
import { PasswordResetInitComponent } from './password-reset/init/password-reset-init.component';
import { PasswordResetFinishComponent } from './password-reset/finish/password-reset-finish.component';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { authState } from './auth.route';

@NgModule({
  declarations: [
    ActivateComponent,
    RegisterComponent,
    PasswordComponent,
    PasswordStrengthBarComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
  ],
  imports: [SharedModule, RouterModule.forChild(authState)],
})
export class AuthModule {}
