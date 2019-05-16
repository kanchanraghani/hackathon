import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { ConfirmationComponent } from './confirmation/confirmation.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDatepickerModule, MatInputModule, MatNativeDateModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { RequestFormComponent } from './request-form/request-form.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
	  ConfirmationComponent,
	  RequestFormComponent
  ],
  imports: [
      BrowserModule,
      BrowserAnimationsModule,
      MatDatepickerModule, MatInputModule,MatNativeDateModule,
      FormsModule,
      ReactiveFormsModule,
      AppRoutingModule,
      HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
