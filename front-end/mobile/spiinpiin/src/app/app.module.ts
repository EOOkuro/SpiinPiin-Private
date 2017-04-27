import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
import { IonicStorageModule } from '@ionic/storage';

import { GettingStartedPage } from '../pages/getting-started/getting-started';
import { GettingStartedSlidesPage } from '../pages/getting-started-slides/getting-started-slides';
import { LoginPage } from '../pages/login/login';
import { SignupPage } from '../pages/signup/signup';
import { HomePage } from '../pages/home/home';
import { MenuPage } from '../pages/menu/menu';
import { SettingsPage } from '../pages/settings/settings';
import { AccountPage } from '../pages/account/account';
import { SpinchatPage } from '../pages/spinchat/spinchat';
import { SpinchatDetailPage } from '../pages/spinchat-detail/spinchat-detail';
import { ChannelsPage } from '../pages/channels/channels';
import { ChannelProfilePage } from '../pages/channel-profile/channel-profile';
import { SpiinpiinPage } from '../pages/spiinpiin/spiinpiin';


import { AngularFireModule } from 'angularfire2';
import { SpiinpiinService } from '../providers/spiinpiin-service';
import { Camera } from '@ionic-native/camera';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
// AF2 Settings
export const firebaseConfig = {
  apiKey: "AIzaSyBn8ytqLK5iAur4duIUBqbNSxDnUCgbYyE",
  authDomain: "spiinpiinv2.firebaseapp.com",
  databaseURL: "https://spiinpiinv2.firebaseio.com",
  projectId: "spiinpiinv2",
  storageBucket: "spiinpiinv2.appspot.com",
  messagingSenderId: "1017304184977"
};

@NgModule({
  declarations: [
    MyApp,
    GettingStartedPage,
    GettingStartedSlidesPage,
    LoginPage,
    SignupPage,
    HomePage,
    MenuPage,
    SettingsPage,
    AccountPage,
    SpinchatPage,
    SpinchatDetailPage,
    ChannelProfilePage,
    ChannelsPage,
    SpiinpiinPage

  ],
  imports: [
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot(),
    AngularFireModule.initializeApp(firebaseConfig),
    
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    GettingStartedPage,
    GettingStartedSlidesPage,
    LoginPage,
    SignupPage,
    HomePage,
    MenuPage,
    SettingsPage,
    AccountPage,
    SpinchatPage,
    SpinchatDetailPage,
    ChannelProfilePage,
    ChannelsPage,
    SpiinpiinPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    SpiinpiinService,
    Camera,
    { provide: ErrorHandler, useClass: IonicErrorHandler }
  ]
})
export class AppModule { }
