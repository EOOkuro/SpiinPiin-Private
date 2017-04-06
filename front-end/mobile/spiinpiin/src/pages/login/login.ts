import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { SignupPage } from '../signup/signup';
import { MenuPage } from '../menu/menu';

/*
  Generated class for the Login page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  constructor(public navCtrl: NavController, public navParams: NavParams) {}
  goToPage(nextpage){
    switch (nextpage) {
      case 'signup':
        this.navCtrl.push(SignupPage);
        break;
       case 'home':
        this.navCtrl.setRoot(MenuPage);
        break;

    }
  }
}
