import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Matrix } from '../interfaces/matrix';

@Injectable({
  providedIn: 'root',
})
export class MatrixService {
  private BASEURL = 'http://localhost:8080/matrices';

  private matrixSubject = new BehaviorSubject<Matrix[]>([]);
  private matrices: Matrix[] = [];

  constructor(private httpClient: HttpClient) {}

  public createMatrix(matrixName: string, xlabels: number[], ylabels: number[]): Observable<any> {
    return this.httpClient.post(this.BASEURL, {
      name: matrixName,
      xlabels: xlabels,
      ylabels: ylabels,
    });
  }

  public deleteMatrix(id: any): Observable<any> {
    return this.httpClient.delete(this.BASEURL + '/' + id);
  }

  public increaseStock(id: any, data: number[][]): Observable<Matrix> {
    return this.httpClient.put<Matrix>(`${this.BASEURL}/${id}/increaseStock`, {
      data: data,
    });
  }

  public reduceStock(id: any, data: number[][]): Observable<Matrix> {
    return this.httpClient.put<Matrix>(`${this.BASEURL}/${id}/reduceStock`, {
      data: data,
    });
  }

  public getMatrices(): Observable<Matrix[]> {
    return this.httpClient.get<Matrix[]>(this.BASEURL);
  }

  public getMatricesObservable(): Observable<Matrix[]> {
    return this.matrixSubject.asObservable();
  }

  public updateMatrices(): void {
    this.httpClient.get<Matrix[]>(this.BASEURL).subscribe((matrices) => {
      this.matrices = matrices;
      this.matrixSubject.next(matrices);
    });
  }

  public getMatrix(id: any): Observable<Matrix> {
    return this.httpClient.get<Matrix>(this.BASEURL + '/' + id);
  }
}
