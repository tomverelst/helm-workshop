import { Component, OnInit, OnDestroy} from '@angular/core';
import {SpeechRecognitionService} from './speech.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [SpeechRecognitionService]
})
export class AppComponent implements OnInit, OnDestroy {
  showSearchButton: boolean;
  speechData: string;

  constructor(private speechRecognitionService: SpeechRecognitionService) {
    this.showSearchButton = true;
    this.speechData = '';
  }

  ngOnInit() {
    console.log('hello');
  }

  ngOnDestroy() {
    this.speechRecognitionService.DestroySpeechObject();
  }

  activateSpeech(): void {
    this.showSearchButton = false;

    this.speechRecognitionService.record()
        .subscribe(
            // listener
            (value) => {
              this.speechData = value;
              console.log(value);
            },
            // errror
            (err) => {
              console.log(err);
              if (err.error === 'no-speech') {
                console.log('--restarting service--');
                this.activateSpeech();
              }
            },
            // completion
            () => {
              this.showSearchButton = true;
              console.log('--complete--');
              this.activateSpeech();
            });
  }

}
