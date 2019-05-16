import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material';
import { ConfirmationComponent } from '../confirmation/confirmation.component';
import { DataService } from '../data.service';

@Component({
  selector: 'app-request-form',
  templateUrl: './request-form.component.html',
  styleUrls: ['./request-form.component.css']
})
export class RequestFormComponent implements OnInit {

  model: any = {};
  constructor(private router: Router, public dialog: MatDialog, private dataservice: DataService) { }

  ngOnInit() {
  }

  onSubmit(f) {
    console.log(JSON.stringify(this.model));
    this.dataservice.sendDetails(JSON.stringify(this.model)).subscribe(
      Response => console.log('Success!!!', Response),
      error => console.error('Error Encountered!',error)
    );
    // alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.model))
    //  this.router.navigateByUrl('/confirmed');
    this.openDialog(f);
  }

  openDialog(f): void {
    const dialogRef = this.dialog.open(ConfirmationComponent, {
      width: '250px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      f.reset();
    });
  }
}