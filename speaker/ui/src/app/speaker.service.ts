import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SpeakerService {

  constructor(private http: HttpClient) { }

  speak(speech: Speech): void {
    this.http.post<Speech>('say', speech).subscribe((data: Speech) => {
      console.log('Got response from backend: ' + data);
    });
  }
}

export enum SpeechType {
  UNKNOWN,
  ORDER,
  QUESTION
}

export interface Speech {
  text: string;
  type?: SpeechType;
}

