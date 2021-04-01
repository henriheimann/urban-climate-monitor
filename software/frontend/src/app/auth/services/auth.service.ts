import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {combineLatest, Observable, of} from 'rxjs';
import {environment} from '../../../environments/environment';
import {concatMap, map, mergeMap, switchMap, withLatestFrom} from 'rxjs/operators';
import {User} from '../../user/models/user.model';
import {EntityCollectionService, EntityCollectionServiceFactory} from '@ngrx/data';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private userService: EntityCollectionService<User>;

  constructor(private httpClient: HttpClient, private EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory) {
    this.userService = EntityCollectionServiceFactoryClass.create<User>('User');
  }

  loginUser(username: string, password: string): Observable<{token: string, user: User}> {
    return this.httpClient.post<{access_token: string}>(`${environment.backendUrl}/oauth/token`, null, {
      params: new HttpParams()
        .append('username', username)
        .append('password', password)
        .append('grant_type', 'password'),
      headers: new HttpHeaders()
        .append('Authorization', 'Basic ' + btoa(environment.oauthClientLogin))
    }).pipe(
      map(tokenResponse => tokenResponse.access_token),
      switchMap((token) => {
        return combineLatest([of(token), this.userService.getByKey(username)]);
      }),
      map(([token, user]) => ({ token, user }))
    );
  }
}
