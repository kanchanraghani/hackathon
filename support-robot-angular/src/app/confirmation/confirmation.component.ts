import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    public dialogRef: MatDialogRef<ConfirmationComponent>
  ) { }

  ngOnInit() {
  }

  goBack(): void {
    this.location.back();
  }

}
