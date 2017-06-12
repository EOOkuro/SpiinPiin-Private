import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { SpiinpiinService } from '../../providers/spiinpiin-service';

@Component({
  selector: 'page-spiinpiin',
  templateUrl: 'spiinpiin.html'
})
export class SpiinpiinPage {
loader:any;
countries;
  constructor(public navCtrl: NavController, public navParams: NavParams,private spiinpiinservice: SpiinpiinService) {}

  ionViewDidLoad() {
    
  }
postData(){
      this.loader = this.spiinpiinservice.showLoader("Please wait ...");
    this.loader.present();
    this.spiinpiinservice.getCountries().subscribe((response) => {
      if (response.status == 1) {
        this.countries = response.data;
      } else {
        this.spiinpiinservice.toastMessage(response.msg);
      }
      this.loader.dismiss();
    }, (error) => {
      this.loader.dismiss();
      this.spiinpiinservice.toastMessage("Error Initializing page");
    })
}
}


/*postData(){
      this.loader = this.spiinpiinservice.showLoader("Please wait ...");
    this.loader.present();
    this.spiinpiinservice.getCountries().subscribe((response) => {
      if (response.status == 1) {
        this.countries = response.data;
      } else {
        this.spiinpiinservice.toastMessage(response.msg);
      }
      this.loader.dismiss();
    }, (error) => {
      this.loader.dismiss();
      this.spiinpiinservice.toastMessage("Error Initializing page");
    })
}*/
