import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { HomePage } from '../home/home';
/*
  Generated class for the Menu page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-menu',
  templateUrl: 'menu.html'
})
export class MenuPage {
  rootPage:any;
  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.rootPage =  HomePage;
  }

   openPage(p) {
    this.rootPage = p;
  }

  

}
