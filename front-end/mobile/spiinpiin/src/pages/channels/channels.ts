import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { ChannelProfilePage } from '../channel-profile/channel-profile';

@Component({
  selector: 'page-channels',
  templateUrl: 'channels.html'
})
export class ChannelsPage {

  items:any[];
  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.items = [];
 for (let i = 1; i < 10; i++) {  
   this.items.push({    
      username:"username"+i,
      time:"Mobile"    
   });
 }
  }

  viewChannel(channel){
    this.navCtrl.push(ChannelProfilePage);
  }

}
