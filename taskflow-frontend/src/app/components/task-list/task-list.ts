import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Task } from '../../services/task';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css',
})
export class TaskList implements OnInit {
  tasks: any[] = [];
  loading: boolean = true;

  constructor(
    private taskService: Task,
    private auth: Auth,
    private router: Router
  ){}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe({
      next: (data) => {  
        this.tasks = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar tareas', error);
        this.loading = false;
      }
    });
  }

  createTask(): void {
    this.router.navigate(['/tasks/new']);
  }

  editTask(id: string): void {
    this.router.navigate(['/tasks/edit', id]);
  }

  toggleTask(id: string): void {
    this.taskService.toggleTask(id).subscribe({
      next: () => {
        this.loadTasks();
      },
      error: (error) => {
        console.error('Error al camhbiar estado:', error);
      }
    });
  }

  deleteTask(id: string): void {
    if (confirm('¿Estás seguro de que deseas eliminar esta tarea?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.loadTasks();
        },
        error: (error) => {
          console.error('Error al eliminar tarea:', error);
        }
      });
    }
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
