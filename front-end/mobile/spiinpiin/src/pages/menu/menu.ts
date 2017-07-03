import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { HomePage } from '../home/home';
import { SettingsPage } from '../settings/settings';
import { AccountPage } from '../account/account';
import { SpinchatPage } from '../spinchat/spinchat';
import { ChannelsPage } from '../channels/channels';
import { SpiinpiinPage } from '../spiinpiin/spiinpiin';
import {GoogleMaps} from '@ionic-native/google-maps';
import { SpiinpiinService } from '../../providers/spiinpiin-service';


@Component({
  selector: 'page-menu',
  templateUrl: 'menu.html'
})
export class MenuPage {
  rootPage:any;
  username:string =null;
  profilepic:string = null;
  constructor(private googleMaps: GoogleMaps,public navCtrl: NavController, public navParams: NavParams, private spiinpiinservice: SpiinpiinService) {
    this.rootPage =  HomePage;
  }

  ionViewDidLoad(){
    
     this.spiinpiinservice.getFromLocalStorage("profilepic").then((data)=>{
      this.profilepic = JSON.parse(data);
    });
    this.spiinpiinservice.getFromLocalStorage("name").then((data)=>{
      this.username = JSON.parse(data);
    });
  }

   openPage(page) {
     switch (page) {
       case 'settings':
         this.navCtrl.push(SettingsPage);
         break;
         case 'account':
         this.navCtrl.push(AccountPage);
         break;
         case 'spinchat':
         this.navCtrl.push(SpinchatPage);
         break;
         case 'channels':
         this.navCtrl.push(ChannelsPage);
         break;
         case 'spiinpiin':
         this.navCtrl.push(SpiinpiinPage);
         break;
     
     
       default:
         break;
     }
    
  }

  

}
