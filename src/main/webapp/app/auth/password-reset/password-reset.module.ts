import { NgModule } from '@angular/core';
import { PasswordResetInitComponent } from './init/password-reset-init.component';
import { PasswordResetFinishComponent } from './finish/password-reset-finish.component';
import { RouterModule } from '@angular/router';
import { passwordResetInitRoute } from './init/password-reset-init.route';
import { passwordResetFinishRoute } from './finish/password-reset-finish.route';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [PasswordResetInitComponent, PasswordResetFinishComponent],
  imports: [SharedModule, RouterModule.forChild([passwordResetInitRoute, passwordResetFinishRoute])],
})
export class PasswordResetModule {}
