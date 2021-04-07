import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {delay, map} from 'rxjs/operators';
import {User} from '../../user/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private static LOCAL_STORAGE_AUTH_USER = 'auth.user';
  private static LOCAL_STORAGE_AUTH_TOKEN = 'auth.token';
  private static LOCAL_STORAGE_AUTH_REFRESH_TOKEN = 'auth.refreshToken';

  constructor(private httpClient: HttpClient) {
  }

  getTokens(username: string, password: string): Observable<{ token: string, refreshToken: string }> {
    return this.httpClient.post<{ access_token: string, refresh_token: string }>(`${environment.backendUrl}/oauth/token`, null, {
      params: new HttpParams()
        .append('username', username)
        .append('password', password)
        .append('grant_type', 'password'),
      headers: new HttpHeaders()
        .append('Authorization', 'Basic ' + btoa(environment.oauthClientLogin))
    }).pipe(map(response => ({
        token: response.access_token,
        refreshToken: response.refresh_token
      })));
  }

  getTokensViaRefresh(refreshToken: string): Observable<{ token: string, refreshToken: string }> {
    return this.httpClient.post<{ access_token: string, refresh_token: string }>(`${environment.backendUrl}/oauth/token`, null, {
      params: new HttpParams()
        .append('refresh_token', refreshToken)
        .append('grant_type', 'refresh_token'),
      headers: new HttpHeaders()
        .append('Authorization', 'Basic ' + btoa(environment.oauthClientLogin))
    }).pipe(map(response => ({
      token: response.access_token,
      refreshToken: response.refresh_token
    })));
  }

  revokeRefreshToken(refreshToken: string): Observable<any> {
    return this.httpClient.post<any>(`${environment.backendUrl}/oauth/revoke`, null, {
      params: new HttpParams()
        .append('token', refreshToken)
    });
  }

  clearLocalStorage(): void {
    localStorage.removeItem(AuthService.LOCAL_STORAGE_AUTH_USER);
    localStorage.removeItem(AuthService.LOCAL_STORAGE_AUTH_TOKEN);
    localStorage.removeItem(AuthService.LOCAL_STORAGE_AUTH_REFRESH_TOKEN);
  }

  saveLocalStorage(user: User, token: string, refreshToken: string): void {
    localStorage.setItem(AuthService.LOCAL_STORAGE_AUTH_USER, JSON.stringify(user));
    localStorage.setItem(AuthService.LOCAL_STORAGE_AUTH_TOKEN, token);
    localStorage.setItem(AuthService.LOCAL_STORAGE_AUTH_REFRESH_TOKEN, refreshToken);
  }

  loadLocalStorage(): { token: string, refreshToken: string, user: User } | null {
    if (localStorage.getItem(AuthService.LOCAL_STORAGE_AUTH_USER) != null) {
      return {
        user: JSON.parse(localStorage.getItem(AuthService.LOCAL_STORAGE_AUTH_USER) || '{}') as User,
        token: localStorage.getItem(AuthService.LOCAL_STORAGE_AUTH_TOKEN) || '',
        refreshToken: localStorage.getItem(AuthService.LOCAL_STORAGE_AUTH_REFRESH_TOKEN) || ''
      };
    } else {
      return null;
    }
  }
}
