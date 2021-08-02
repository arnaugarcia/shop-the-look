import { NgModule } from '@angular/core';
import { NavbarComponent } from './navbar.component';
import { SharedModule } from '../../shared/shared.module';
import { ActiveMenuDirective } from './active-menu.directive';
import { RoleNavbarPipe } from './role-navbar.pipe';

@NgModule({
  declarations: [NavbarComponent, ActiveMenuDirective, RoleNavbarPipe],
  imports: [SharedModule],
  exports: [NavbarComponent],
})
export class NavbarModule {}
