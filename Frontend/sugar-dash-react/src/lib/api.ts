const API_BASE_URL = '/api';

export interface Sweet {
  id: number;
  name: string;
  category_name: string;
  description: string;
  price: number;
  stock_quantity: number;
  image_url?: string;
  category_id?: number;
  created_at?: string;
  updated_at?: string;
}

export interface CreateSweetRequest {
  name: string;
  category_id: number;
  description: string;
  price: number;
  stock_quantity: number;
  image_url?: string;
}

export interface PurchaseRequest {
  quantity: number;
}

export interface RestockRequest {
  quantity: number;
}

class ApiService {
  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  }

  // Get all sweets
  async getAllSweets(): Promise<Sweet[]> {
    return this.request<Sweet[]>('/sweets');
  }

  // Get sweet by ID
  async getSweetById(id: number): Promise<Sweet> {
    return this.request<Sweet>(`/sweets/${id}`);
  }

  // Create new sweet
  async createSweet(sweet: CreateSweetRequest): Promise<Sweet> {
    return this.request<Sweet>('/sweets', {
      method: 'POST',
      body: JSON.stringify(sweet),
    });
  }

  // Update sweet
  async updateSweet(id: number, sweet: Partial<Sweet>): Promise<Sweet> {
    return this.request<Sweet>(`/sweets/${id}`, {
      method: 'PUT',
      body: JSON.stringify(sweet),
    });
  }

  // Delete sweet
  async deleteSweet(id: number): Promise<void> {
    await this.request<void>(`/sweets/${id}`, {
      method: 'DELETE',
    });
  }

  // Search sweets
  async searchSweets(query: string): Promise<Sweet[]> {
    return this.request<Sweet[]>(`/sweets/search?q=${encodeURIComponent(query)}`);
  }

  // Purchase sweet (reduce stock)
  async purchaseSweet(id: number, quantity: number): Promise<{ message: string; sweet: Sweet }> {
    return this.request<{ message: string; sweet: Sweet }>(`/sweets/${id}/purchase`, {
      method: 'POST',
      body: JSON.stringify({ quantity }),
    });
  }

  // Restock sweet (increase stock)
  async restockSweet(id: number, quantity: number): Promise<{ message: string; sweet: Sweet }> {
    return this.request<{ message: string; sweet: Sweet }>(`/sweets/${id}/restock`, {
      method: 'POST',
      body: JSON.stringify({ quantity }),
    });
  }

  // Health check
  async healthCheck(): Promise<{ status: string }> {
    return this.request<{ status: string }>('/actuator/health');
  }
}

export const apiService = new ApiService();