import { Component,OnInit } from '@angular/core';
import { Storage } from '@ionic/storage';
import { NavController, NavParams,ToastController,LoadingController } from 'ionic-angular';
import {AngularFire,FirebaseAuthState} from 'angularfire2';
import { SignupPage } from '../signup/signup';
import { MenuPage } from '../menu/menu';
import { SpiinpiinService } from '../../providers/spiinpiin-service';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage implements OnInit {
  auth={
    email:null,
    password:null
  }

    loader:any; 
 showLoader(){
    this.loader = this.loadingCtrl.create({
    content: "Please wait..."
  });
  this.loader.present();
 }

 ngOnInit(){
   
  
 }

  constructor(public navCtrl: NavController,public storage: Storage,private spiinpiinservice:SpiinpiinService,
   public navParams: NavParams,public loadingCtrl: LoadingController,public af:AngularFire,private toastCtrl: ToastController,) {
     this.af.auth.subscribe(
     (user:FirebaseAuthState) =>{
       if(user){

          this.storage.ready().then(() => {

       // set a key/value
       this.storage.set('name', 'Max');

       // Or to get a key/value pair
       this.storage.get('name').then((val) => {
         console.log('Your age is', val);
       })
     });
         
        this.navCtrl.setRoot(MenuPage);
       }       
     }
   );
     
   }
  doLogin(){
    this.navCtrl.setRoot(MenuPage);
     if(!this.auth.email){
      this.presentToast("Enter your email address");
      return;
    }

    if(!this.spiinpiinservice.validateEmail(this.auth.email)){
      this.presentToast("The email address is invalid");
      return;
    }

    if(!this.auth.password){
      this.presentToast("Enter your password");
      return;
    }
    this.showLoader();

    this.af.auth.login({email:this.auth.email,password:this.auth.password}).then(
      response=>{
        //Query Profile,save locally       


        //dismiss loader
        this.loader.dismiss();
        //go home
         this.navCtrl.setRoot(MenuPage);
      }
    ).catch(error =>{
       this.loader.dismiss();
       console.log(error);
       this.presentToast(error.message);
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

  goToPage(nextpage){
    switch (nextpage) {
      case 'signup':
        this.navCtrl.push(SignupPage);
        break;
       case 'home':
        this.navCtrl.setRoot(MenuPage);
        break;

    }
  }
}
