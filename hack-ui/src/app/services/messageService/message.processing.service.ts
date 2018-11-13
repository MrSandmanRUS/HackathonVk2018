import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class MessageProcessingService {
  message = new BehaviorSubject<string>('');
  data = this.message.asObservable();

  constructor() { }

  showMessage(message : string) {
    this.message.next(message);
  }
}
