import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.scss',
})
export class SideBarComponent {
  constructor(public apiService: ApiService) {}

  newChat(): void {
    this.apiService.clearConversation();
  }

  selectChat(id: number): void {
    if (this.apiService.currentConversationId() !== id) {
      this.apiService.loadConversation(id);
    }
  }

  deleteChat(event: Event, id: number): void {
    event.stopPropagation();

    if (confirm('Are you sure you want to delete this chat?')) {
      this.apiService.deleteConversation(id).subscribe(() => {
        this.apiService.loadUserConversations().subscribe();

        if (this.apiService.currentConversationId() === id) {
          this.apiService.clearConversation();
        }
      });
    }
  }
}
