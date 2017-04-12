import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { HomePage } from '../home/home';
import { SettingsPage } from '../settings/settings';
import { AccountPage } from '../account/account';

@Component({
  selector: 'page-menu',
  templateUrl: 'menu.html'
})
export class MenuPage {
  rootPage:any;
  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.rootPage =  HomePage;
  }

   openPage(page) {
     switch (page) {
       case 'settings':
         this.navCtrl.push(SettingsPage);
         break;
         case 'account':
         this.navCtrl.push(AccountPage);
         break;
     
       default:
         break;
     }
    
  }

  

}
