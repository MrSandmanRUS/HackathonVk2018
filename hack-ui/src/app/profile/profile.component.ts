import { Component, OnInit } from '@angular/core';
import {Profile} from "../data/profile";
import {HttpService} from "../services/httpService/http.service";
import {MessageProcessingService} from "../services/messageService/message.processing.service";
import {Router} from "@angular/router";
import {AuthService} from "../services/authService/auth.service";

@Component({
  selector: 'profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  providers: [HttpService]
})
export class ProfileComponent implements OnInit {

  profile: Profile = new Profile();
  url = "/profile"

  constructor(private httpService: HttpService,
              private proc: MessageProcessingService,
              private router: Router,
              private auth: AuthService) { }

  ngOnInit() {
    this.getData()
  }

  getData() {
    this.profile.achievements = [];
    this.httpService.getData(this.url,"")
      .map(resp => resp.json() as Profile)
      .subscribe(data => {
        this.profile = data
      },
        error2 => {
        this.proc.showMessage(error2);
        this.auth.check();
        this.router.navigate(['/login']);
      })
    this.profile.achievements.push("")
  }

  updateData(updInfo) {
    this.httpService.postBody(updInfo,this.url)
      .subscribe()
  }
}
