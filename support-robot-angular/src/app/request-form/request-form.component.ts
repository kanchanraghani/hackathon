import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-request-form',
  templateUrl: './request-form.component.html',
  styleUrls: ['./request-form.component.css']
})
export class RequestFormComponent implements OnInit {

  model: any = {};
  constructor(private router: Router) { }

  ngOnInit() {
  }
  
  onSubmit() {
   console.log(JSON.stringify(this.model));
   //alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.model))
   this.router.navigateByUrl('/confirmed');
  }
}