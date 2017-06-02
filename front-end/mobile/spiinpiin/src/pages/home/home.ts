import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import {
  GoogleMaps,
  GoogleMap,
  GoogleMapsEvent,
  LatLng,
  CameraPosition,
  MarkerOptions,
  Marker
} from '@ionic-native/google-maps';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
// create LatLng object
    myCoords: LatLng;
  constructor(private googleMaps: GoogleMaps, public navCtrl: NavController, public navParams: NavParams) { }
  // Load map only after view is initialized
  ngAfterViewInit() {
    this.loadMap();
  }
  ionViewDidLoad() {
    console.log('ionViewDidLoad HomePage');
  }

  loadMap() {
    // create a new map by passing HTMLElement
    let element: HTMLElement = document.getElementById('map');
    let map: GoogleMap = this.googleMaps.create(element);
    map.one(GoogleMapsEvent.MAP_READY).then(
      () => {
        map.getMyLocation().then((location) => {
          this.myCoords =  new LatLng(location.latLng.lat, location.latLng.lng);
          map.addMarker(markerOptions)
            .then((marker: Marker) => {
              marker.showInfoWindow();

            })
        });
      });
    
    /// create CameraPosition
    let position: CameraPosition = {
      target: this.myCoords,
      zoom: 18,
      tilt: 30
    };

    // move the map's camera to position
    map.moveCamera(position);

    // create new marker
    let markerOptions: MarkerOptions = {
      position: this.myCoords
    };




  }

}
