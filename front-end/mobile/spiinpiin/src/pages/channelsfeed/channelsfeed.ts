import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

@Component({
  selector: 'page-channelsfeed',
  templateUrl: 'channelsfeed.html'
})
export class ChannelsfeedPage {

  constructor(public navCtrl: NavController, public navParams: NavParams) {}

  ionViewDidLoad() {
    console.log('ionViewDidLoad ChannelsfeedPage');
  }

}
