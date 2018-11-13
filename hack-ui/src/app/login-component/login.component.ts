import { Component, OnInit } from '@angular/core';
import {HttpService} from "../services/httpService/http.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {AuthService} from "../services/authService/auth.service";
import {MessageProcessingService} from "../services/messageService/message.processing.service";
import {FullUser} from "../data/full-user";
import {AppComponent} from "../app.component";
import {Observable} from "rxjs/Rx";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [HttpService]
})
export class LoginComponent implements OnInit {
  fullUser: FullUser = new FullUser();
  loginUrl = '/api/users/login';
  registerUrl = '/api/users/register';

  code: string;

  constructor(private httpService: HttpService,
              private router: Router,
              private auth: AuthService,
              private proc: MessageProcessingService,
              private route: ActivatedRoute) { }

  submit(info) {
    this.auth.login(this.loginUrl, info)
      .subscribe((data) => {
          this.router.navigate(['/profile']);
        },
        error => {
          if (error.status == 500) {
            this.proc.showMessage("Произошла ошибка");
          } else {
            this.proc.showMessage(error.json().message);
          }
        });
  }

  signup(fullUser) {
    this.httpService.postBody(fullUser, this.registerUrl)
      .subscribe((data) => {
          this.submit(fullUser);
        },
        error => {
          if (error.status == 500) {
            this.proc.showMessage("Произошла ошибка");
          } else {
            this.proc.showMessage(error.json().message);
          }
        });
  }

  ngOnInit() {
    this.route.queryParams
      .subscribe(params => {
        console.log(params);
        this.code = params.code;
        console.log(this.code);
        if (this.code != null) {
          this.auth.signVK();
          this.router.navigate(['/profile'])
        }
      })
  }

  redirect() {

    var temp='https://oauth.vk.com/authorize'
    var q = '?'
    var client='client_id=6742720'
    var a='&'
    var disp='display=page'
    var redir='redirect_uri='
    var url=AppComponent.globalUrl + "/getVk";
    //var url='http://172.20.37.229:4200' + "/getVk";
    var so='scope=groups'
    var resp='response_type=code'
    var vers='v=5.87'

    window.location.href=temp+q+client+a+disp+a+redir+url+a+so+a+resp+a+vers;

  }

}
