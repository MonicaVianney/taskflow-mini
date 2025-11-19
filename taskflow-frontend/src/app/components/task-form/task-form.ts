import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Task } from '../../services/task';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm implements OnInit{
  taskId: string | null = null;
  title: string = '';
  description: string = '';
  completed: boolean = false;
  isEditMode: boolean = false;

  constructor(
    private taskService: Task,
    private router: Router,
    private route: ActivatedRoute
  ){}

  ngOnInit(): void {
      this.taskId = this.route.snapshot.paramMap.get('id');
      this.isEditMode = !!this.taskId;

      if (this.isEditMode && this.taskId) {
        this.loadTask(this.taskId);
      }
  }

  loadTask(id: string): void {
    this.taskService.getTaskById(id).subscribe({
      next: (task) => {
        this.title = task.title;
        this.description = task.description;
        this.completed = task.completed;
      },
      error: (error) => {
        console.error('Error al cargar tarea', error);
        this.router.navigate(['/tasks']);
      }
    });
  }

  onSubmit(): void {
    const taskData = {
      title: this.title,
      description: this.description,
      completed: this.completed
    };

    if (this.isEditMode && this.taskId) {
      this.taskService.updateTask(this.taskId, taskData).subscribe({
        next: () => {
          this.router.navigate(['/tasks']);
        },
        error: (error) => {
          console.error('Error al actualizar tarea', error);
        }
      });
    } else {
      this.taskService.createTask(taskData).subscribe({
        next: () => {
          this.router.navigate(['/tasks']);
        },
        error: (error) => {
          console.error('Error al crear tarea', error);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/tasks']);
  }
}
