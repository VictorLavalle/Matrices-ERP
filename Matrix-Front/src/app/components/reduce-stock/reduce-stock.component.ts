import { Component } from '@angular/core';
import { Matrix } from 'src/app/interfaces/matrix';
import { MatrixService } from 'src/app/services/matrix.service';
import { ActivatedRoute, Router, RouterStateSnapshot } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-reduce-stock',
  templateUrl: './reduce-stock.component.html',
  styleUrls: ['./reduce-stock.component.scss'],
})
export class ReduceStockComponent {
  private id!: number;
  public inputsData: number[][] = [];
  private destroy$ = new Subject<void>();
  public matrix!: Matrix;
  private alertElement!: HTMLElement;

  constructor(private matrixService: MatrixService, private route: ActivatedRoute, private router: Router) {}

  //Function to paste and fill the matrix with the data from the clipboard (ONLY EXCEL)
  onPaste(event: ClipboardEvent) {
    let clipboardData = event.clipboardData;
    if (!clipboardData) return;
    let matrix = this.clipboardTextToMatrix(clipboardData.getData('text'));
    console.log({ matrix });
    if (
      !this.matricesDimensionsAreEqual(matrix, this.inputsData) ||
      this.someMatrixValueIsNan(matrix)
    )
      return;
    for (let i in matrix) {
      for (let j in matrix[i]) {
        this.inputsData[j][i] = matrix[i][j];
      }
    }
    event.preventDefault();
  }

  private someMatrixValueIsNan(matrix: number[][]): boolean {
    for (let i in matrix) {
      for (let j in matrix[i]) {
        if (isNaN(matrix[i][j])) return true;
      }
    }
    return false;
  }

  //Function to check if both matrices have the same dimensions
  private matricesDimensionsAreEqual(matrix1: number[][], matrix2: number[][]): boolean {
    let dimensions1 = this.getMatrixDimensions(matrix1);
    let dimensions2 = this.getMatrixDimensions(matrix2);

    console.log({ dimensions1, dimensions2 });

    return dimensions1[0] === dimensions2[1] && dimensions1[0] === dimensions2[1];
  }

  //Function to get the dimensions of a matrix to check if it is valid to paste
  private getMatrixDimensions(matrix: number[][]): number[] {
    let row_count = matrix.length;
    let row_sizes = [];
    for (let i = 0; i < row_count; i++) row_sizes.push(matrix[i].length);
    return [row_count, Math.min.apply(null, row_sizes)];
  }

  //Function to convert the clipboard (excel) text to a matrix
  private clipboardTextToMatrix(data: string): number[][] {
    data = data.replaceAll('\r', '');
    let rows = data.split('\n');
    let matrixString: string[][] = rows.map((row) => row.split('\t'));
    let matrix = matrixString.map((row) => row.map((col) => Number.parseInt(col)));
    return matrix;
  }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id')!);
    this.alertElement = document.getElementById('alert-container')!;

    this.matrixService
      .getMatrix(this.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          console.log(data);
          this.matrix = data;
          this.inputsData = this.fillInventorty(this.matrix);
          console.log(this.inputsData);
        },
        error: (err) => {
          console.log(err);
          if (err.status === 400) {
            this.router.navigate(['/PageNotFound']);
          }
        }
      });
  }
  public save(): void {
    if (this.validateNumericArray(this.inputsData)) {
      this.matrixService.reduceStock(this.id, this.inputsData).subscribe((data) => {
        console.log(data);
        this.showAlert('success', 'Matriz actualizada exitosamente.');
        this.matrixService.getMatrix(this.id).subscribe((data) => {
          this.matrix = data;
          this.inputsData = this.fillInventorty(this.matrix);
        });
      });
    } else {
      console.log(this.inputsData);
      this.showAlert('danger', 'El array contiene elementos que no son números.');
      throw new Error('El array contiene elementos que no son números.');
    }
  }

  public validateNumericArray(arr: any[]): boolean {
    const regex = /^[0-9;]*$/;
    for (const item of arr) {
      if (Array.isArray(item)) {
        if (!this.validateNumericArray(item)) {
          return false;
        }
      } else if (!regex.test(item)) {
        return false;
      }
    }
    return true;
  }

  private showAlert(type: string, message: string): void {
    this.alertElement.innerHTML = `
      <div class="alert alert-${type} alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
    `;
    this.alertElement.classList.remove('d-none');

    setTimeout(() => {
      this.alertElement.querySelector('.alert')?.classList.add('fade-out');
      setTimeout(() => {
        this.alertElement.classList.add('d-none');
        this.alertElement.querySelector('.alert')?.classList.remove('fade-out');
      }, 500);
    }, 3500);
  }

  public fillInventorty(matrix: Matrix): number[][] {
    const colums: number[][] = [];

    for (let index = 0; index < matrix.ylabels.length; index++) {
      const rows: number[] = [];
      for (let index = 0; index < matrix.xlabels.length; index++) {
        rows.push(0);
      }
      colums.push(rows);
    }
    console.log(colums);

    return colums;
  }
}
