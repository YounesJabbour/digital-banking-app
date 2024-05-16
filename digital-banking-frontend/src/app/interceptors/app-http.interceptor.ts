import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
} from '@angular/common/http';
import { Observable, catchError, finalize, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {
  allowList = ['/auth/login', '/auth/register', '/auth/changePassword'];
  constructor(private authService: AuthService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (this.allowList.some((url) => request.url.includes(url)))
      return next.handle(request);

    let req = request.clone({
      headers: request.headers.set(
        'Authorization',
        'Bearer ' + this.authService.accessToken
      ),
    });

    return next.handle(req).pipe(
      catchError((error, caught) => {
        if (error.status === 401) {
          this.authService.logout();
        }
        return throwError(error.message);
      })
    );
  }
}
