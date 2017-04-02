import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

@Component({
  selector: 'page-getting-started-slides',
  templateUrl: 'getting-started-slides.html'
})
export class GettingStartedSlidesPage {

  constructor(public navCtrl: NavController, public navParams: NavParams) {}

 goToLogin(){
   this.navCtrl.pop();
 }
}
