import {Injectable} from '@angular/core';
import {Headers, Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {AppComponent} from "../../app.component";
import {throwError} from "rxjs/index";


@Injectable()
export class HttpService {

  constructor(private http: Http) { }

  baseUrl = AppComponent.globalUrl;

  postData(params: string, url: string) {
    const headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded'});
    return this.http.post(this.baseUrl + url, params, {headers: headers})
      .map((res: Response) => {
        return res['_body'] ? res.json() : null;
      })
      .catch((error: any) => Observable.throw(error));
  }

  postBody(params: string, url: string) {
    let headers = new Headers({'Content-Type': 'application/json'});
    return this.http.post(this.baseUrl + url, params, {headers: headers})
      .map((res: Response) => {
        return res['_body'] ? res.json() : null;
      })

  }

  getData(url: string, params: any) {
    return this.http.get(this.baseUrl + url, {params});
  }

  /*
 getDataById(url: string, id: string) {
     return this.http.get(url);
   }
*/
}
