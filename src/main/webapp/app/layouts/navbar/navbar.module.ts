import { NgModule } from '@angular/core';
import { NavbarComponent } from './navbar.component';
import { SharedModule } from '../../shared/shared.module';
import { ActiveMenuDirective } from './active-menu.directive';

@NgModule({
  declarations: [NavbarComponent, ActiveMenuDirective],
  imports: [SharedModule],
  exports: [NavbarComponent],
})
export class NavbarModule {}
