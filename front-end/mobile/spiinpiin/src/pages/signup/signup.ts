import { Component } from '@angular/core';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { NavController, NavParams,LoadingController,ModalController } from 'ionic-angular';
import { SpiinpiinService } from '../../providers/spiinpiin-service';
import { SignupPasswordModalPage } from '../signup-password-modal/signup-password-modal';
import { CountriesObject } from "../../app/object-countries";
@Component({
  selector: 'page-signup',
  templateUrl: 'signup.html'
})

export class SignupPage {

  constructor(private camera: Camera,public navCtrl: NavController,public modalCtrl: ModalController, public navParams: NavParams,private spiinpiinservice:SpiinpiinService  ) {
  }
  passwordType:string = "password";
  countries:CountriesObject[]=[];
  loader:any; 
  profile = null;
  auth = {
    fullname:null,
    email:null,
    country:null,
    phone:null,
    username:null,
    passworda:null,
    passwordb:null,
    accept_tc:false,
    showpwd:false,
    photo:"assets/user.png"
  }

  ionViewDidLoad() {
    this.loader = this.spiinpiinservice.showLoader("Please wait ...");
    this.loader.present();
    this.spiinpiinservice.getCountries().subscribe((response)=>{
      if(response.status == 1){
        this.countries = response.data;
      }else{
        this.spiinpiinservice.toastMessage(response.msg);
      }
      this.loader.dismiss();      
    },(error)=>{
      this.loader.dismiss();
      this.spiinpiinservice.toastMessage("Could not get countries");
    })
 }

  doSignup(){
    
    if(!this.auth.fullname){
      this.spiinpiinservice.toastMessage("Enter your full name");
      return;
    }

     if(!this.auth.email){
      this.spiinpiinservice.toastMessage("Enter your email Address");
      return;
    }

    if(!this.spiinpiinservice.validateEmail(this.auth.email)){
      this.spiinpiinservice.toastMessage("The email address is invalid");
      return;
    }
    if(!this.auth.country){
      this.spiinpiinservice.toastMessage("Choose your country");
      return;
    }
    if(!this.auth.phone){
      this.spiinpiinservice.toastMessage("Enter your phone number");
      return;
    }
    if(!this.auth.username){
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
    }
    if(!this.auth.accept_tc){
      this.spiinpiinservice.toastMessage("Accept Terms and Conditions to proceed");
      return;
    }
  //  this.doSocialSignup();

}

doSocialSignup(method){
switch (method) {
      case "twitter":
        //doTwitterSignUp();
        break;
     case "facebook":
        //doFacebookSignUp();
        break;
     case "google":
       // doGoogleSignUp();
        break;
      default:
      this.showPasswordModal();
        break;
    }
}
  

uploadPicture(){
  let camOptions: CameraOptions = {
  quality: 100,
  destinationType: this.camera.DestinationType.DATA_URL,
  encodingType: this.camera.EncodingType.JPEG,
  mediaType: this.camera.MediaType.PICTURE
}
  this.camera.getPicture(camOptions).then((imageData) => {
 // imageData is either a base64 encoded string or a file URI
 // If it's base64:
 this.auth.photo = 'data:image/jpeg;base64,' + imageData;
}, (err) => {
 // Handle error
});

}



  toggleShowPassword(){
    switch (this.auth.showpwd) {
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


showPasswordModal(){
    if(!this.auth.email){
      this.spiinpiinservice.toastMessage("Enter your email Address");
      return;
    }

    if(!this.spiinpiinservice.validateEmail(this.auth.email)){
      this.spiinpiinservice.toastMessage("The email address is invalid");
      return;
    }
let profileModal = this.modalCtrl.create(SignupPasswordModalPage, { "email": this.auth.email });
   profileModal.present();
}

goToSignIn(){
  this.navCtrl.pop();
}



}
