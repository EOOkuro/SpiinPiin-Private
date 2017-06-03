import { Component } from '@angular/core';
import { NavController, NavParams,ViewController } from 'ionic-angular';
import { SpiinpiinService } from '../../providers/spiinpiin-service';


@Component({
  selector: 'page-signup-password-modal',
  templateUrl: 'signup-password-modal.html'
})
export class SignupPasswordModalPage {
email:string;passworda:string;passwordb:string;
showpwd:boolean = false;
passwordType:string = "password";
  constructor(private spiinpiinservice:SpiinpiinService,public navCtrl: NavController, public navParams: NavParams,public viewCtrl: ViewController) {
  this.email = this.navParams.get('email');
  this.passwordType = "password";
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

  setPassword(){
    if(!this.passworda || !this.passwordb){
      this.spiinpiinservice.toastMessage("Enter your prefered password");
      return;
    }
    if((this.passworda).length <8 || (this.passwordb).length < 8){
      this.spiinpiinservice.toastMessage("Password needs to be greater than 8 characters");
      return;
    }
     if(!(this.passworda === this.passwordb) ){
      this.spiinpiinservice.toastMessage("Your Passwords do not Match");
      return;
    }

    this.viewCtrl.dismiss(this.passworda);
  }


}
