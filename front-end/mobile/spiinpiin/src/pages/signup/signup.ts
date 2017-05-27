import { Component } from '@angular/core';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { NavController, NavParams,LoadingController,ModalController } from 'ionic-angular';
import {Auth, User, UserDetails, IDetailedError } from '@ionic/cloud-angular';

import { SpiinpiinService } from '../../providers/spiinpiin-service';
import { SignupPasswordModalPage } from '../signup-password-modal/signup-password-modal';
import { CountriesObject } from "../../app/object-countries";
@Component({
  selector: 'page-signup',
  templateUrl: 'signup.html'
})

export class SignupPage {

  constructor(public f_auth: Auth, public user: User,private camera: Camera,public navCtrl: NavController,public modalCtrl: ModalController, public navParams: NavParams,private spiinpiinservice:SpiinpiinService  ) {
  }
  passwordType:string = "password";
  countries:CountriesObject[]=[];
  loader:any; 
  profile = null;
  /*auth = {
    fname:null,
    lname:null,
    email:null,
    country:null,
    phone:null,
    passworda:null,
    passwordb:null,
    accept_tc:false,
    showpwd:false,
    photo:"assets/user.png"
  }*/

   auth = {
    fname:"Richard",
    lname:"Omoka",
    email:"richard.omoka@gmail.com",
    country:"KEN",
    phone:"254717225954",
    passworda:null,
    passwordb:null,
    accept_tc:true,
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

  doSocialSignup(method){
    if(!this.auth.fname){
      this.spiinpiinservice.toastMessage("Enter your First name");
      return;
    }
     if(!this.auth.lname){
      this.spiinpiinservice.toastMessage("Enter your surname");
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
  
    if(!this.auth.accept_tc){
      this.spiinpiinservice.toastMessage("Accept Terms and Conditions to proceed");
      return;
    } 
    this.spiinpiinservice.getFromLocalStorage('authdata').then((data)=>{
      console.log(JSON.parse(data));
    });
  switch (method) {
      case "twitter":
        this.f_auth.login('twitter').then(()=>{
          alert(this.user.social.twitter.uid);
          let authId = this.user.social.twitter.uid;
          this.spiinpiinservice.saveToLocalStorage('authObject',this.user.social.twitter);
          this.sendToSpiinpiinServer('twitter',authId);
        });
        break;
     case "facebook":
        //doFacebookSignUp();
        console.log(method);
        break;
     case "google":
       // doGoogleSignUp();
       console.log(method);
        break;
      default:
      this.showPasswordModal();
        break;
    }

}

  sendToSpiinpiinServer(provider,fuid){
       this.loader = this.spiinpiinservice.showLoader("Please wait ...");
       this.loader.present();
       let data = {
         'fname':this.auth.fname,
         'lname':this.auth.lname,
         'email':this.auth.email,
         'countryCode':this.auth.country,
         'phone':this.auth.phone,
         'provider':provider,
         'fuid':fuid
       };  

       
      
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
