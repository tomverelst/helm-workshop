import {Component, OnDestroy, OnInit} from '@angular/core';
import {SpeechRecognitionService} from './speech.service';
import {SpeakerService} from './speaker.service';
import {HttpClientModule} from '@angular/common/http';
import {Subject} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [SpeechRecognitionService, SpeakerService, HttpClientModule]
})
export class AppComponent implements OnInit, OnDestroy {
  listening: boolean;
  speech$ = new Subject<string>();
  speechData: string;
  icon: string;

  constructor(
      private speechRecognitionService: SpeechRecognitionService,
      private speakerService: SpeakerService) {
    this.listening = false;
  }

  ngOnInit() {
    this.icon = 'mic_off';
    this.speech$.subscribe(data => this.speak(data));
  }

  speak(data: string): void {
    this.speechData = data;
    this.speakerService.speak({
      text: data
    });
  }

  ngOnDestroy() {
    this.speechRecognitionService.DestroySpeechObject();
  }

  toggleSpeech(): void {
    this.listening = !this.listening;
    this.toggleSpeechRecognition();
  }

  private toggleSpeechRecognition() {
    if (this.listening) {
      this.activateSpeech();
    } else {
      this.disableSpeech();
    }
  }

  activateSpeech(): void {
    this.icon = 'mic';

    this.speechRecognitionService.record()
        .subscribe(
            // listener
            (value) => {
              this.speech$.next(value);
            },
            // errror
            (err) => {
              this.toggleSpeechRecognition();
            },
            // completion
            () => {
              this.toggleSpeechRecognition();
            });
  }

  disableSpeech(): void {
    console.log('Disabling...' + this.icon);
    this.speechRecognitionService.stopRecording();
    this.listening = false;
    this.icon = 'mic_off';
  }

}
