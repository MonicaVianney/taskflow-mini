import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email : string = '';
  password : string = '';
  errorMessage : string = '';

  constructor(private auth: Auth, private router: Router) {}

  onSubmit(): void{
    this.auth.login(this.email, this.password).subscribe({
      next: (response) => {
        this.auth.saveToken(response.token);
        this.router.navigate(['/tasks']);
      },
      error: (error) => {
        this.errorMessage = 'Credenciales inválidas';
        console.error('Error en el login:', error);
      }
    });
  }

  goToRegister(): void{
    this.router.navigate(['/register']);
  }
}
