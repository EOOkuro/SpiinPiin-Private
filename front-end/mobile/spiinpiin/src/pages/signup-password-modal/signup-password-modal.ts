import { Component } from '@angular/core';
import { NavController, NavParams,ViewController } from 'ionic-angular';


@Component({
  selector: 'page-signup-password-modal',
  templateUrl: 'signup-password-modal.html'
})
export class SignupPasswordModalPage {
email:string;
showpwd:boolean = false;
passwordType:string = "password;"
  constructor(public navCtrl: NavController, public navParams: NavParams,public viewCtrl: ViewController) {
  this.email = this.navParams.get('email')
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad SignupPasswordModalPage');
  }
  toggleShowPassword(){
    switch (this.showpwd) {
      case true:
        this.passwordType ="text";
        break;
     case false:
        this.passwordType ="password";
        break;
      default:
        this.passwordType ="password";
        break;
    }
  }

  dismiss(){
     this.viewCtrl.dismiss();
  }


}
 /* if(!this.auth.username){
      this.spiinpiinservice.toastMessage("Enter your prefered username");
      return;
    }
    if(!this.auth.passworda || !this.auth.passwordb){
      this.spiinpiinservice.toastMessage("Enter your prefered password");
      return;
    }
     if(!(this.auth.passworda === this.auth.passwordb) ){
      this.spiinpiinservice.toastMessage("Your Passwords do not Match");
      return;
    }*/

