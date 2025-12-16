import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-chat-input',
  standalone: true,
  imports: [],
  templateUrl: './chat-input.component.html',
  styleUrl: './chat-input.component.scss',
})
export class ChatInputComponent {
  constructor(public apiService: ApiService) {}

  sendMessage(inputElement: HTMLInputElement) {
    const message = inputElement.value;

    if (message.trim() && !this.apiService.loading()) {
      this.apiService.postMessage(message);
      inputElement.value = '';
    }
  }
}
