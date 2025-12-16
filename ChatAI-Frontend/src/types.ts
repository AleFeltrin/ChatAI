export interface ApiResponse<T = any> {
  success: boolean;
  data: T;
  timestamp: string;
  message?: string;
}

export interface ChatRequest {
  message: string;
  userId: number;
  conversationId: number | null;
}

export interface Message {
  id?: number;
  role: 'user' | 'assistant';
  content: string;
  createdAt: string;
}

export interface Conversation {
  id: number;
  title: string;
  createdAt: string;
  updatedAt: string;
  messageCount: number;
}
