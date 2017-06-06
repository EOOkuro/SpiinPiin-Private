import { Component } from '@angular/core';
import { Platform } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { SpiinpiinService } from '../providers/spiinpiin-service';

import { HomePage } from "../pages/home/home";
import { MenuPage } from '../pages/menu/menu';
import { GettingStartedSlidesPage } from '../pages/getting-started-slides/getting-started-slides';


@Component({
  templateUrl: 'app.html'
  
})
export class MyApp {  
  rootPage:any = GettingStartedSlidesPage;
  loader:any;
  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen,private spiinpiinservice:SpiinpiinService) {
    platform.ready().then(() => {
      this.spiinpiinservice.showLoader("Please Wait...");
      this.loader.present();   
      this.spiinpiinservice.getFromLocalStorage('sessionkey').then((sesskey)=>{
        
        if(sesskey != undefined){
          this.loader.dismiss();
          this.rootPage = MenuPage;
        }else{
          this.loader.dismiss();
          this.rootPage = GettingStartedSlidesPage;
        }
      });
      statusBar.styleDefault();
      splashScreen.hide();
    });
  }
}

