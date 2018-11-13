import {Component, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/Rx";
import {ModalComponent} from "./modal/modal.component";
import {MessageProcessingService} from "./services/messageService/message.processing.service";
import {Router} from "@angular/router";
import {AuthService} from "./services/authService/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  static globalUrl = 'http://172.20.38.119:8081';
  loginUrl = '/login';
  logoutUrl = '/logout';
  sign = 'Войти';
  authenticated = false;
  errorOccured = true;
  authenticationSubscription: Subscription;
  messageProcessingSubscription: Subscription;
  message = '';
  @ViewChild('errormodal') errorModal: ModalComponent;

  constructor(private auth: AuthService,
              private proc: MessageProcessingService,
              private router: Router) {

    this.authenticationSubscription = auth.data.subscribe(value => {
      this.authenticated = value;
    });
    this.messageProcessingSubscription = proc.data.subscribe(value =>{
      this.message = value;
      if (value != '') {
        this.errorModal.show()
        console.log("Errorrrr: " + value)
      }
    });
  }

  logout() {
    this.auth.logout(this.logoutUrl)
      .subscribe(data => {
        this.router.navigate(['/']);
      }),
      error => {
        if (error.status == 500) {
          this.proc.showMessage("Произошла ошибка при выходе из аккаунта");
        } else {
          this.proc.showMessage(error.json().message);
        }
      }
  }

  public AuthVk() {
    this.authenticated = true;
  }

  isAuthenticated() {
    return this.authenticated;
  }

  isErrorOccured() {
    return this.errorOccured;
  }

  ngOnInit() {
    this.auth.check();
  }

  ngOnDestroy() {
    this.authenticationSubscription.unsubscribe();
    this.messageProcessingSubscription.unsubscribe();
  }

}
