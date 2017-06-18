import { Component } from '@angular/core';
import { NavController, NavParams, Platform } from 'ionic-angular';
import { SpiinpiinService } from '../../providers/spiinpiin-service';
import { Geolocation } from '@ionic-native/geolocation';
import { GoogleMaps, GoogleMap, GoogleMapsEvent, LatLng, CameraPosition, MarkerOptions, Marker } from '@ionic-native/google-maps';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {  
  map: GoogleMap;
  myCoords:LatLng;
  loader:any;
  constructor(private geolocation: Geolocation, private spiinpiinservice: SpiinpiinService, private platform: Platform, private googleMaps: GoogleMaps, public navCtrl: NavController, public navParams: NavParams) { 
    platform.ready().then(()=>{
      this.loader = this.spiinpiinservice.showLoader("Initializing feed ...");
      this.loader.present();
       this.geolocation.getCurrentPosition({
      maximumAge: 3000,
      timeout: 5000,
      enableHighAccuracy: true
    }).then((resp) => {
      this.loader.dismiss();
      this.myCoords = new LatLng(resp.coords.latitude, resp.coords.longitude);
       this.loadMap();
    },(err)=>{
      this.loader.dismiss();
      alert(err);
    });
    });
  }
  
  loadMap() {
    this.map = new GoogleMap('map', {
      'backgroundColor': 'white',
      'controls': {
        'compass': true,
        'myLocationButton': true,
        'indoorPicker': true,
        'zoom': true
      },
      'gestures': {
        'scroll': true,
        'tilt': true,
        'rotate': true,
        'zoom': true
      },
      'camera': {
        'latLng': this.myCoords,
        'tilt': 30,
        'zoom': 15,
        'bearing': 50
      }
    });

    this.map.on(GoogleMapsEvent.MAP_READY).subscribe(() => {
        let position: CameraPosition = {
          target: this.myCoords,
          zoom: 18,
          tilt: 30
        };
        this.map.moveCamera(position);
        let markerOptions:MarkerOptions = {
          position: this.myCoords
        //  title: ''
        };

        this.map.addMarker(markerOptions)
          .then((marker: Marker) => {
            marker.showInfoWindow();
          });
    });

  }

}
