import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {HttpService} from "../httpService/http.service";
import {Observable} from "rxjs/Observable";
import {Http, Response, Headers} from "@angular/http";
import {AppComponent} from "../../app.component";

@Injectable()
export class AuthService {
  au = new BehaviorSubject<boolean>(this.hasToken());
  data = this.au.asObservable();
  providers: [HttpService];
  baseUrl = AppComponent.globalUrl;

  constructor(private httpService: HttpService,
              private http: Http) { }

  logout(url: string) {
    return this.http.get(this.baseUrl + url, {})
      .map((res: Response) => {
        this.change();
        sessionStorage.removeItem('token');
        return res.totalBytes > 0 ? res.json() : null;
      })
      .catch((error: any) => Observable.throw(error));
  }

  login(url: string, params: string) {
    const headers = new Headers({'Content-Type': 'application/json'});
    return this.http.post(this.baseUrl + url, params, {headers: headers})
      .map((res: Response) => {
        this.change();
        sessionStorage.setItem('token', 'on');
        return res.totalBytes > 0 ? res.json() : null;
        //return res['_body'] ? res.json() : null;
      })
      //.catch((error: any) => Observable.throw(error));
  }

  change() {
    this.au.next(!this.au.value);
  }

  signVK() {
    this.change();
    sessionStorage.setItem('token', 'on');
  }

  check() {
    this.httpService.getData('/api/users/current', null)
      .subscribe(resp => {
        if (resp.json().username != 'anonymousUser') {
          this.au.next(true);
        }
      });
  }

  hasToken() : boolean {
    return !!sessionStorage.getItem('token');
  }
}
