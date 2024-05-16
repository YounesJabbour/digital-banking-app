import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { environment } from 'src/environments/environment';

interface AuthResponse {
  'access-token': string;
  // include other properties as needed
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  isAuthenticated: boolean = false;
  username: any;
  name: any;
  roles: any;
  accessToken: string = '';

  public login(customer: any) {
    let options = new HttpHeaders().set('Content-Type', 'application/json');

    // let params = new HttpParams()
    //   .set('username', username)
    //   .set('password', password);

    return this.http.post<AuthResponse>(
      `${environment.baseUrl}/auth/login`,
      customer,
      { headers: options }
    );
  }

  public register(customer: any) {
    let options = new HttpHeaders().set('Content-Type', 'application/json');

    return this.http.post<any>(
      `${environment.baseUrl}/auth/register`,
      customer,
      {
        headers: options,
      }
    );
  }

  loadProfile(data: AuthResponse) {
    this.isAuthenticated = true;
    this.accessToken = data['access-token'];

    let decodedJwt: any = jwtDecode(this.accessToken);

    this.username = decodedJwt.sub;
    this.roles = decodedJwt.scope;
    this.name = decodedJwt.name;

    // set the access token in local storage
    localStorage.setItem('jwt-token', this.accessToken);
  }

  logout() {
    this.isAuthenticated = false;
    this.accessToken = '';
    this.username = '';
    this.roles = '';
    localStorage.removeItem('jwt-token');
    this.router.navigateByUrl('/login');
  }

  loadJwtFromLocalStorage() {
    let jwt = localStorage.getItem('jwt-token');
    if (jwt) {
      this.loadProfile({ 'access-token': jwt });
      this.router.navigateByUrl('/admin/customers');
    }
  }

  changePassword(email: string, currentPassword: string, newPassword: string) {
    let options = new HttpHeaders().set('Content-Type', 'application/json');
    let passwordChange = {
      email: email,
      oldPassword: currentPassword,
      newPassword: newPassword,
    };
    return this.http.put<any>(
      `${environment.baseUrl}/auth/changePassword`,
      passwordChange,
      {
        headers: options,
      }
    );
  }
}
