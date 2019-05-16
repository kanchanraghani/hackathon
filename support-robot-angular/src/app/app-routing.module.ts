import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ConfirmationComponent }  from './confirmation/confirmation.component';
import { RequestFormComponent } from './request-form/request-form.component';
import { AppComponent }  from './app.component';

const routes: Routes = [
  { path: '', redirectTo: '/supportrobot', pathMatch: 'full' },
  { path: 'confirmed', component: ConfirmationComponent },
  { path: 'supportrobot', component: RequestFormComponent },
];

@NgModule({
  declarations: [],
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
