import { Component } from '@angular/core';
import { Platform } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { SpiinpiinService } from '../providers/spiinpiin-service';


//import { GettingStartedPage } from '../pages/getting-started/getting-started';
import { GettingStartedSlidesPage } from '../pages/getting-started-slides/getting-started-slides';


@Component({
  templateUrl: 'app.html'
  
})
export class MyApp {  
  rootPage:any = GettingStartedSlidesPage;
  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen,private spiinpiinservice:SpiinpiinService) {
    platform.ready().then(() => {    
      statusBar.styleDefault();
      splashScreen.hide();
    });
  }
}

