import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {HttpService} from "../services/httpService/http.service";
import {Events} from "../data/events";

@Component({
  selector: 'events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css'],
  encapsulation: ViewEncapsulation.None,
  providers: [HttpService]
})
export class EventsComponent implements OnInit {

  events: Events[] = [];
  userEvents: Events[] = [];

  // colors: string[] = ["red","green"]
  // ages: string[] = ["0+","16+"]

  urlCommon = "/allEvents"
  urlRec = "/events"

  constructor(private httpService: HttpService) { }

  // getColor(param: string) : string {
  //   return this.colors[this.ages.indexOf(param)];
  // }

  ngOnInit() {
    this.getEvents()
    this.getRecommendations()

    // this.demo = new Events();
    // this.demo.name = "Хакатон ВК"
    // this.demo.city = "Санкт-Петербург"
    // this.demo.age = "16+"
    // this.demo.cost = "Бесплатно"
    // this.demo.date = "09/11/2018"
    // this.demo.owner = "ВКонтакте"
    // this.demo.link = "https://vk.com/hack_onboard"
    // this.demo.tag = "IT"
  }

  getEvents() {
    this.httpService.getData(this.urlCommon,"")
      .map(resp => resp.json() as Events[])
      .subscribe(data => this.events = data)
  }

  getRecommendations() {
    this.httpService.getData(this.urlRec,"")
      .map(resp => resp.json() as Events[])
      .subscribe(data => this.userEvents = data)
  }

}
