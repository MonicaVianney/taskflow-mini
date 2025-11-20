import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root',
})
export class Task {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient, private auth: Auth) {}

  private getHeaders(): HttpHeaders {
    const token = this.auth.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getTasks(): Observable<any> {
    return this.http.get(`${this.apiUrl}/tasks`, { headers: this.getHeaders() });
  }

  getTaskById(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/tasks/${id}`, { headers: this.getHeaders() });
  }

  createTask(task: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/tasks`, task, { headers: this.getHeaders() });
  }

  updateTask(id: string, task: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/tasks/${id}`, task, { headers: this.getHeaders() });
  }

  toggleTask(id: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/tasks/${id}/toggle`, {}, { headers: this.getHeaders() });
  }

  deleteTask(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/tasks/${id}`, { 
      headers: this.getHeaders(),
      responseType: 'text' as 'json'
    });
  }
}
