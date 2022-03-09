import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormGroupDirective } from '@angular/forms';
import { CountryService } from 'src/app/services/country.service';

@Component({
  selector: 'app-add-country',
  templateUrl: './add-country.component.html',
  styleUrls: ['./add-country.component.scss']
})
export class AddCountryComponent implements OnInit {

  countryForm = this.createForm();
  submitted = false;

  constructor(private formBuilder: FormBuilder, private countryService: CountryService) { }

  ngOnInit(): void { }

  onSubmit(formDirective: FormGroupDirective) {
    this.countryService.create(this.countryForm.value)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.submitted = true;
          this.refreshForm(formDirective);
        },
        error: (e) => console.error(e)
    });
  }

  createForm(): FormGroup {
    return this.formBuilder.group({
      name: [''],
      countrycode: ['']
    });
  }

  refreshForm(formDirective: FormGroupDirective) {
    formDirective.resetForm();
  }
 
}
