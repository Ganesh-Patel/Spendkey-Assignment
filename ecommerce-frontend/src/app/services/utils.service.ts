import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {
  public isLoggedIn = false;

  constructor(private http: HttpClient) {}

  fetchData() {
    return ["ganesh", "sunny", "karan"];
  }

  onLogin(mobile = '9876543210') {
    const url = 'https://auth-api-blyp.parkmate.in/api/v1/users/login';
    const headers = { 'Content-Type': 'application/json' };
    const payload = {
      mobile: mobile,
      channel: 'sms',
      countryCode: '+91'
    };

    return this.http.post(url, payload, { headers });
  }

  setLoginStatus(status: boolean) {
    this.isLoggedIn = status;
  }

  getLoginStatus() {
    return this.isLoggedIn;
  }
}
