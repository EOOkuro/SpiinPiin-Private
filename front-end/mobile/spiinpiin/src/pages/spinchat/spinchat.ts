import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';


import { SpinchatDetailPage } from '../spinchat-detail/spinchat-detail';

@Component({
  selector: 'page-spinchat',
  templateUrl: 'spinchat.html'
})
export class SpinchatPage {
 items:any[];
  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.items = [];
 for (let i = 1; i < 10; i++) {  
   this.items.push({    
      username:"username"+i,
      message:"Lorem Ipsum... ",
      time:"05:0"+i    
   });
 }
  }
  openChat(item){
     this.navCtrl.push(SpinchatDetailPage);
  }


}
