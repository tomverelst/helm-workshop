import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class MemberService {

  constructor(private http: HttpClient) { }

  speak(speech: Speech): void {
    this.http.post<Speech>('say', speech).subscribe((data: Speech) => {
      console.log('Speech detected: ' + data);
    });
  }

  name(): Observable<String> {
    return new Observable((sink) => {
      this.http.get("name")
        .subscribe(
          (data: NameRequest) => {
        sink.next(data.name);
        sink.complete();
      }, error => {
          sink.next("Unknown");
          sink.complete();
      })
    });
  }
}

export enum SpeechType {
  UNKNOWN,
  ORDER,
  QUESTION
}

export interface NameRequest {
  name: string;
}

export interface Speech {
  text: string;
  type?: SpeechType;
}

