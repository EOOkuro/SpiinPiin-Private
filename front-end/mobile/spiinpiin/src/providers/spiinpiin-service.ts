import { Injectable } from '@angular/core';
import { Http,RequestOptions,Headers } from '@angular/http';
import { LoadingController,ToastController } from 'ionic-angular';

import { Storage } from '@ionic/storage';
import 'rxjs/add/operator/map';

import {SpiinpiinConfig} from '../app/spiinpiin-config';

@Injectable()
export class SpiinpiinService {

  constructor(public http: Http,public toastCtrl: ToastController,private storage: Storage,public loadingCtrl: LoadingController) {}

  validateEmail(email){
    let re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
  }

  getCountries(){
     //return this.http.get(SpiinpiinConfig.API_ENDPOINT+"/lcountry").map((response) => response.json());
     return this.http.get('dummy/lcountry.json').map((response) => response.json());
  }

   callPostApi(uri,data) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('AuthorizationKey', "p");  
    let options = new RequestOptions();
    return this.http.post(SpiinpiinConfig.API_ENDPOINT + uri,data,options).map((response) => response.json());
  }

   saveToLocalStorage(key,value){
    let data =  JSON.stringify(value);
    this.storage.set(key,data);
   }

  getFromLocalStorage(key){
    return this.storage.get(key);
  }



  toastMessage(message){
   let toast = this.toastCtrl.create({
      message: message,
      duration: 2000,
      position:'bottom',
      cssClass:"text-center"
    });
    toast.present();
  }

  showLoader(message){
   return this.loadingCtrl.create({
    content: message
  }); 
 }

  

}
