import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { GettingStartedSlidesPage } from '../getting-started-slides/getting-started-slides';
import { LoginPage } from '../login/login';

@Component({
  selector: 'page-getting-started',
  templateUrl: 'getting-started.html'
})
export class GettingStartedPage {

  constructor(public navCtrl: NavController, public navParams: NavParams) {}

goToPage(nextpage){
    switch (nextpage) {
      case 'gs-slides':
        this.navCtrl.push(GettingStartedSlidesPage);
        break;
        case 'login':        
        this.navCtrl.push(LoginPage);
        break;
    
      default:
        break;
    }
  }

}
