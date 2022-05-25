import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ArrosageSharedModule } from 'app/shared/shared.module';
import { ArrosageCoreModule } from 'app/core/core.module';
import { ArrosageAppRoutingModule } from './app-routing.module';
import { ArrosageHomeModule } from './home/home.module';
import { ArrosageEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { CountdownModule } from 'ngx-countdown';

@NgModule({
  imports: [
    BrowserModule,
    ArrosageSharedModule,
    ArrosageCoreModule,
    ArrosageHomeModule,
    CountdownModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ArrosageEntityModule,
    ArrosageAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class ArrosageAppModule {}
