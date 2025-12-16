import { Component, ElementRef, ViewChild, effect, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-chat-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chat-view.component.html',
  styleUrl: './chat-view.component.scss',
})
export class ChatViewComponent {
  @ViewChild('scrollContainer') private scrollContainer!: ElementRef;

  constructor(public apiService: ApiService) {
    effect(() => {
      const msgs = this.apiService.messages();

      const loading = this.apiService.loading();

      setTimeout(() => {
        this.scrollToBottom();
      }, 50);
    });
  }

  scrollToBottom(): void {
    try {
      if (this.scrollContainer) {
        this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
      }
    } catch (err) {
      console.error(err);
    }
  }
}
