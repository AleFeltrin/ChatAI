import { Component } from '@angular/core';
import { SideBarComponent } from '../components/side-bar/side-bar.component';
import { ChatViewComponent } from '../components/chat-view/chat-view.component';
import { ChatInputComponent } from '../components/chat-input/chat-input.component';

@Component({
  selector: 'app-chatbot',
  imports: [SideBarComponent, ChatViewComponent, ChatInputComponent],
  templateUrl: './chatbot.component.html',
  styleUrl: './chatbot.component.scss',
})
export class ChatbotComponent {
  constructor() {}
}
