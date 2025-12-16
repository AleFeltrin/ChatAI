import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, ChatRequest, Message, Conversation } from '../../types';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  loading = signal<boolean>(false);
  messages = signal<Message[]>([]);
  currentConversationId = signal<number | null>(null);
  conversations = signal<Conversation[]>([]);

  private readonly apiUrl = 'http://localhost:8080/api/chat';
  private readonly conversationsUrl = 'http://localhost:8080/api/conversations';
  private readonly userId = 1;

  constructor(private http: HttpClient) {
    this.loadUserConversations();
  }

  postMessage(message: string): void {
    this.messages.update((msgs) => [...msgs, { role: 'user', content: message, createdAt: new Date().toISOString() }]);

    this.loading.set(true);

    const request: ChatRequest = {
      message: message,
      userId: this.userId,
      conversationId: this.currentConversationId(),
    };

    this.http.post<ApiResponse>(this.apiUrl, request).subscribe({
      next: (response) => {
        this.messages.update((msgs) => [...msgs, { role: 'assistant', content: response.data, createdAt: new Date().toISOString() }]);

        if (this.currentConversationId() === null) {
          this.refreshConversationsAfterNew();
        } else {
          this.loadUserConversations().subscribe();
        }

        this.loading.set(false);
      },
      error: (error) => {
        console.error('Error sending message:', error);
        this.messages.update((msgs) => [...msgs, { role: 'assistant', content: 'Error processing message.', createdAt: new Date().toISOString() }]);
        this.loading.set(false);
      },
    });
  }

  private refreshConversationsAfterNew() {
    this.loadUserConversations().subscribe((conversations) => {
      if (conversations.length > 0) {
        this.currentConversationId.set(conversations[0].id);
      }
    });
  }

  startNewConversation(): void {
    this.currentConversationId.set(null);
    this.messages.set([]);
  }

  loadConversation(conversationId: number): void {
    this.loading.set(true);
    this.currentConversationId.set(conversationId);

    this.http.get<Message[]>(`${this.apiUrl}/conversation/${conversationId}/messages`).subscribe({
      next: (messages) => {
        this.messages.set(messages);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Error loading conversation:', error);
        this.loading.set(false);
      },
    });
  }

  loadUserConversations(): Observable<Conversation[]> {
    const observable = this.http.get<Conversation[]>(`${this.conversationsUrl}/user/${this.userId}`);

    observable.subscribe({
      next: (conversations) => {
        this.conversations.set(conversations);
      },
      error: (error) => {
        console.error('Error loading conversations:', error);
      },
    });

    return observable;
  }

  deleteConversation(conversationId: number): Observable<void> {
    return this.http.delete<void>(`${this.conversationsUrl}/${conversationId}`);
  }

  updateConversationTitle(conversationId: number, newTitle: string): Observable<Conversation> {
    return this.http.put<Conversation>(`${this.conversationsUrl}/${conversationId}/title`, { title: newTitle });
  }

  clearConversation(): void {
    this.startNewConversation();
  }
}
