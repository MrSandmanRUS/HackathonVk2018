import {Routes} from "@angular/router";
import {LoginComponent} from "./login-component/login.component";
import {ProfileComponent} from "./profile/profile.component";
import {EventsComponent} from "./events/events.component";
import {AppComponent} from "./app.component";


export const routes: Routes = [
  {
    path: 'login', component: LoginComponent
  },
  {
    path: 'profile', component: ProfileComponent
  },
  {
    path: 'events', component: EventsComponent
  },
  {
    path: 'getVk', component: LoginComponent
  },
  { path: '', redirectTo: 'login', pathMatch: 'full'}
];
