import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { LoginComponent } from './login.component';
import { RouterModule } from '@angular/router';
import { loginRoute } from './login.route';

@NgModule({
  declarations: [LoginComponent],
  imports: [SharedModule, RouterModule.forChild(loginRoute)],
})
export class LoginModule {}
