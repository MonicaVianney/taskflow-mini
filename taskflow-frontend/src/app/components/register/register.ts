import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private auth: Auth, private router: Router) {}

  onSubmit() {
    this.errorMessage = '';
    this.successMessage = ''; 

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }

    if (this.password.length < 6) {
      this.errorMessage = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }

    this.auth.register(this.email, this.password).subscribe({
      next: (response) => {
        this.successMessage = 'Usuario registrado exitosamente';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        if (error.status === 400) {
          this.errorMessage = 'El email ya está registrado';
        } else {
          this.errorMessage = 'Error al registrar usuario';
        }
        console.error('Error en el registro', error);
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}
