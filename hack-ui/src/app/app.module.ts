import { BrowserModule } from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';

import { AppComponent } from './app.component';
import {LoginComponent} from "./login-component/login.component";
import {HttpService} from "./services/httpService/http.service";
import {AuthService} from "./services/authService/auth.service";
import {MessageProcessingService} from "./services/messageService/message.processing.service";
import {HttpModule} from "@angular/http";
import {RouterModule} from "@angular/router";
import {routes} from "./appRouting.routs";
import {FormsModule} from "@angular/forms";
import {ModalComponent} from "./modal/modal.component";
import { ProfileComponent } from './profile/profile.component';
import { EventsComponent } from './events/events.component';
import {MatTabsModule} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ModalComponent,
    ProfileComponent,
    EventsComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    RouterModule.forRoot(routes),
    FormsModule,
    MatTabsModule,
    BrowserAnimationsModule
  ],
  providers: [HttpService, MessageProcessingService, AuthService],
  bootstrap: [AppComponent],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class AppModule { }
