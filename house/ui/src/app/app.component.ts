import {Component, OnInit} from '@angular/core';
import {SseService} from "./sse.service";

export interface Motion {
  question: string;
  ayes: number;
  noes: number;
}

export interface Member {
  id: string;
  name: string;
}

export interface MemberListItem {
  member: Member;
  vote?: string;
}

export interface Message {
  name?: string;
  text?: string;
  image?: string;
}

export interface MemberSeatedEvent {
  member: Member;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'House of Commons';
  motion : Motion = {
    question: "No question yet",
    ayes: 0,
    noes: 0
  };
  members : MemberListItem[] = [];
  messages : Message[] = [];

  private memberSeated;
  private motionStarted;
  private motionUpdated;
  private motionStopped;
  private memberVoted;
  private memberLeft;
  private addMessage;

  constructor(private sseService:SseService){}

  ngOnInit() {
    this.memberSeated = (e) => {
      let found = false;
      let member = JSON.parse(e.data);
      this.members.forEach((item) => {
        if(item.member.id === member.id){
          found = true;
        }
      });
      if(!found) {
        this.members.push({
          "member": member
        });
      }
    };

    this.motionStarted = (event) => {
      this.motion = {
        "question": JSON.parse(event.data).question,
        "ayes": 0,
        "noes": 0
      }
    };

    this.motionUpdated = (event) => {
      this.motion = JSON.parse(event.data);
    };

    this.memberVoted = (e) => {
      let event = JSON.parse(e.data);
      let found = false;
      this.members.forEach((item) => {
        if(item.member.id === event.member.id){
          item.vote = event.vote;
          found = true;
        }
      });

      if(found) {
        this.addMessage({
          "text": event.member.name + " has voted " + event.vote + "!"
        });
      }
    };

    this.memberLeft = (event) => {
    };

    this.motionStopped = (event) => {
      this.members.forEach((item) => {
        item.vote = '';
      });
      this.messages = [];
      this.motion = {
        "question": "Motion stopped",
        "ayes": 0,
        "noes": 0
      };
      this.addMessage({
        "image": "assets/bercrow.gif"
      });
    };

    this.addMessage = (msg : Message) => {
      this.messages.push(msg);
    };

    this.sseService.eventSource().subscribe((es)=> {
      console.log("Connected to EventSource");

      es.addEventListener("member-seated", this.memberSeated);
      es.addEventListener("motion-started", this.motionStarted);
      es.addEventListener("motion-updated", this.motionUpdated);
      es.addEventListener("member-voted", this.memberVoted);
      es.addEventListener("member-left", this.memberLeft);
      es.addEventListener("motion-stopped", this.motionStopped);
    });
  }

}
