import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { LoginComponent } from './login/login.component';
import { AdminTemplateComponent } from './admin-template/admin-template.component';
import { authorizationGuard } from './guards/authorization.guard';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { EditCustomerComponent } from './edit-customer/edit-customer.component';
import { RegisterComponent } from './register/register.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    // children: [],
  },
  { path: 'change-password', component: ChangePasswordComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'admin',
    component: AdminTemplateComponent,
    children: [
      { path: 'customers', component: CustomersComponent },
      { path: 'accounts', component: AccountsComponent },
      {
        path: 'newcustomer',
        component: NewCustomerComponent,
        canActivate: [authorizationGuard],
        data: { roles: ['ADMIN'] },
      },
      { path: 'edit-customer/:id', component: EditCustomerComponent },
      { path: 'notAuthorized', component: NotAuthorizedComponent },
    ],
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
