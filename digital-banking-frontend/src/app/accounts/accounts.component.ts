import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AccountDetails } from '../models/account.model';
import { Operation } from '../models/operation.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css',
})
export class AccountsComponent {
  account!: AccountDetails;
  operation!: Operation;
  errorMessage: Object = '';
  searchFromGroup: FormGroup | undefined;
  operationFormGroup: FormGroup | undefined;
  currentPage: number = 0;
  size: number = 4;
  operationSuccessMessage: string = '';

  constructor(
    private accountService: AccountService,
    private fb: FormBuilder,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.searchFromGroup = this.fb.group({
      accountId: this.fb.control(''),
    });

    this.operationFormGroup = this.fb.group({
      amount: this.fb.control(''),
      operationType: this.fb.control(''),
      description: this.fb.control(''),
      destinationAccountId: this.fb.control(null),
    });
  }

  handleSearchAccount() {
    let accountId = this.searchFromGroup?.value.accountId;

    this.accountService
      .getAccount(accountId, this.currentPage, this.size)
      .subscribe({
        next: (data) => {
          this.account = data;
          console.log(data);
        },
        error: (error) => (this.errorMessage = error.message),
      });
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleOperation() {
    let operation = this.operation;

    operation = this.operationFormGroup?.value;
    operation.sourceAccountId = this.account.accountId;

    this.accountService
      .operation(
        operation.sourceAccountId,
        operation.operationType,
        operation.amount,
        operation.description,
        operation.destinationAccountId
      )
      .subscribe({
        next: (data) => {
          this.operation = data;
          this.handleSearchAccount();
          this.operationSuccessMessage = `${operation.operationType} operation was successful!`;

          setTimeout(() => {
            this.operationSuccessMessage = '';
          }, 3000);
        },
        error: (error) => {
          this.errorMessage = error.message;
          setTimeout(() => {
            this.errorMessage = '';
          }, 3000);
        },
      });
  }
}
