import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { FooterModule } from './footer/footer.module';
import { ErrorComponent } from './error/error.component';
import { MainComponent } from './main/main.component';
import { PageRibbonComponent } from './profiles/page-ribbon.component';
import { NavbarModule } from './navbar/navbar.module';

@NgModule({
  declarations: [ErrorComponent, MainComponent, PageRibbonComponent],
  imports: [SharedModule, FooterModule, NavbarModule],
})
export class LayoutsModule {}
