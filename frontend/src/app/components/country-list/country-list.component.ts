import { Component, Inject, OnInit } from '@angular/core';
import { Country } from 'src/app/models/country.model';
import { CountryService } from 'src/app/services/country.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup } from '@angular/forms';
export interface DialogData {
  id: string;
  name: string;
  countrycode: string;
}

@Component({
  selector: 'app-country-list',
  templateUrl: './country-list.component.html',
  styleUrls: ['./country-list.component.scss']
})
export class CountryListComponent implements OnInit {
  countries?: Country[];
  formname: any;
  displayedColumns: string[] = ['id', 'name', 'countrycode', 'action'];
  dataSource = new MatTableDataSource<Country>();

  constructor(private countryService: CountryService, public dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.countryService.getAll()
      .subscribe({
        next: (data) => {
          this.countries = data;
          this.dataSource = new MatTableDataSource<Country>(this.countries);
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  deleteCountry(id: any) {
    this.countryService.delete(id).subscribe({
      next: () => {
        this.refreshList();
      }
    });
  }
  
  refreshList(): void {
    this.loadData();
  }

  resetSearch() {
    this.formname = '';
    this.searchName();
  }

  searchName(): void {
    this.countryService.findByName(this.formname)
      .subscribe({
        next: (data) => {
          this.countries = data;
          this.dataSource = new MatTableDataSource<Country>(this.countries);
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  openDialog(country: Country): void {
    const dialogRef = this.dialog.open(CountryListDialog, {
      width: '250px',
      data: {id: country.id, name: country.name, countrycode: country.countrycode},
    });

    dialogRef.afterClosed().subscribe(result => {
      // update data
    });
  }

}


@Component({
  selector: 'country-list-dialog',
  templateUrl: 'country-list-dialog.html',
})
export class CountryListDialog {
  countryForm = this.createForm();
  submitted = false;

  constructor(
    public dialogRef: MatDialogRef<CountryListDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private countryService: CountryService
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit() {
    this.countryService.update(this.data.id ,this.countryForm.value)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.dialogRef.close();
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
}
