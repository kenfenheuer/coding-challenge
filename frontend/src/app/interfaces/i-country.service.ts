import { Observable } from 'rxjs';
import { Country } from '../models/country.model';

export interface ICountryService {

    getAll(): Observable<Array<Country>>;

    findByName(name: string): Observable<Array<Country>>;

    update(id: string, country: Country): Observable<any>;

    create(country: Country): Observable<any>;

    delete(id: number): Observable<any>;

}