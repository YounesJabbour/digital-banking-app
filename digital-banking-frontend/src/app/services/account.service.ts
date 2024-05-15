import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AccountDetails } from '../models/account.model';
import { Operation } from '../models/operation.model';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  constructor(private http: HttpClient) {}

  baseUrl: string = environment.baseUrl;

  public getAccount(
    accountId: string,
    page: number = 0,
    size: number = 4
  ): Observable<AccountDetails> {
    return this.http.get<AccountDetails>(
      `${this.baseUrl}/bankAccounts/${accountId}/pageOperation?page=${page}&size=${size}`
    );
  }

  /*
  NOTE: the object in the post method will have props with the same name as the props passed in the operation method
  in this case the object will have the following props:
      {
        bankAccountSourceId: string,
        operationType: string,
        amount: number,
        description: string,
        bankAccountDestinationId?: string
      }

  So in the backend the object must be with the same props names
  */
  operation(
    accountId: string,
    operationType: string,
    amount: number,
    description: string,
    bankAccountDestinationId?: string
  ): Observable<Operation> {
    let data;
    if (operationType === 'TRANSFER') {
      data = {
        bankAccountSourceId: accountId,
        amount,
        description,
        bankAccountDestinationId,
      };
    } else {
      data = {
        accountId,
        amount,
        description,
      };
    }

    return this.http.post<Operation>(
      `${this.baseUrl}/bankAccounts/${operationType.toLowerCase()}`,
      data
    );
  }
}
