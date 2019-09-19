import { Injectable } from '@angular/core';
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class SseService {

  eventSource(): Observable<EventSource> {
    return new Observable((sink) => {
      sink.next(new EventSource('sse'));
      sink.complete();
    });
  }
}
