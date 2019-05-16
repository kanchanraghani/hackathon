import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit {

   constructor(
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit() {
  }
  
   goBack(): void {
    this.location.back();
  }

}
