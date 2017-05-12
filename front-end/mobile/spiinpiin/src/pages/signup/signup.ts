import { Component } from '@angular/core';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { NavController, NavParams,ToastController,LoadingController,ModalController } from 'ionic-angular';
import {AngularFire} from 'angularfire2';
import { SpiinpiinService } from '../../providers/spiinpiin-service';
import { SignupPasswordModalPage } from '../signup-password-modal/signup-password-modal';
@Component({
  selector: 'page-signup',
  templateUrl: 'signup.html'
})

export class SignupPage {

  constructor(private camera: Camera,public navCtrl: NavController,public modalCtrl: ModalController, public navParams: NavParams
  ,public loadingCtrl: LoadingController,public af:AngularFire,private toastCtrl: ToastController,private spiinpiinservice:SpiinpiinService  ) {

  }
  passwordType:string = "password";
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
 //Create spinner
    loader:any; 
 showLoader(){
    this.loader = this.loadingCtrl.create({
    content: "Please wait..."
  });
  this.loader.present();
 }
  doSignup(){
    
    if(!this.auth.fullname){
      this.presentToast("Enter your full name");
      return;
    }

     if(!this.auth.email){
      this.presentToast("Enter your email Address");
      return;
    }

    if(!this.spiinpiinservice.validateEmail(this.auth.email)){
      this.presentToast("The email address is invalid");
      return;
    }
    if(!this.auth.country){
      this.presentToast("Choose your country");
      return;
    }
    if(!this.auth.phone){
      this.presentToast("Enter your phone number");
      return;
    }
    if(!this.auth.username){
      this.presentToast("Enter your prefered username");
      return;
    }
    if(!this.auth.passworda || !this.auth.passwordb){
      this.presentToast("Enter your prefered password");
      return;
    }
     if(!(this.auth.passworda === this.auth.passwordb) ){
      this.presentToast("Your Passwords do not Match");
      return;
    }
    if(!this.auth.accept_tc){
      this.presentToast("Accept Terms and Conditions to proceed");
      return;
    }

   
//Show Spinner
this.showLoader();
    this.af.auth.createUser({
      email:this.auth.email,
      password:this.auth.passworda,
    }).then(
      (response) =>{        
        //Remove plaintext passwords
        delete this.auth.passworda;
        delete this.auth.passwordb;
        //Save data online
        this.profile = this.af.database.list("/profiles");
        this.profile.push(this.auth);
        //Hide Loader
        this.loader.dismiss();
       //Show Success
       this.presentToast("Thanks "+this.auth.username+" for registering with us. You can now log in");
       //Go back
       this.navCtrl.pop();
      } 
    )
    .catch(
      (error) => {
        this.loader.dismiss();
       this.presentToast(error.message);
      }
    );

    
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

presentToast(message) {
  let toast = this.toastCtrl.create({
    message: message,
    duration: 2000,
    position: 'top'
  });
  toast.present();
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
      this.presentToast("Enter your email Address");
      return;
    }

    if(!this.spiinpiinservice.validateEmail(this.auth.email)){
      this.presentToast("The email address is invalid");
      return;
    }
let profileModal = this.modalCtrl.create(SignupPasswordModalPage, { "email": this.auth.email });
   profileModal.present();
}

goToSignIn(){
  this.navCtrl.pop();
}



}
