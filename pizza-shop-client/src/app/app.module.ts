import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";
import { RouterModule, Routes } from "@angular/router";

import { AppRoutingModule } from "@app/app-routing.module";
import { AppComponent } from "@app/app.component";
import { MessagesComponent } from "@app/messages/messages.component";
import { CustomerComponent } from "@app/customer/customer.component";
import { LoginComponent } from "@app/login/login.component";
import { NavbarComponent } from "@app/navbar/navbar.component";
import { SignupComponent } from "@app/signup/signup.component";
import { HomepageComponent } from "@app/homepage/homepage.component";
import { MenuComponent } from "./menu/menu.component";
import { AuthGuard } from "./interceptors/auth.guard";
import { AlertComponent } from './alert/alert.component';

const appRoutes: Routes = [
  { path: "", redirectTo: "/home", pathMatch: "full" },
  { path: "home", component: HomepageComponent },
  { path: "login", component: LoginComponent },
  { path: "signup", component: SignupComponent },
  { path: "menu", component: MenuComponent, canActivate: [AuthGuard] }
];

@NgModule({
  declarations: [
    AppComponent,
    MessagesComponent,
    CustomerComponent,
    LoginComponent,
    NavbarComponent,
    SignupComponent,
    HomepageComponent,
    MenuComponent,
    AlertComponent
  ],
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    ),
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
