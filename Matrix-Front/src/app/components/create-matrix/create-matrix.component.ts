import { Component, EventEmitter, Output } from '@angular/core';
import { Matrix } from '../../interfaces/matrix';
import { MatrixService } from 'src/app/services/matrix.service';

@Component({
  selector: 'app-create-matrix',
  templateUrl: './create-matrix.component.html',
  styleUrls: ['./create-matrix.component.scss'],
})
export class CreateMatrixComponent {
  public matrices: Matrix[] = [];
  public matrixName!: string;

  public xlabel!: string;
  public xlabelStart!: number;
  public xlabelEnd!: number;
  public xlabelInterval!: number;

  public ylabel!: string;
  public ylabelStart!: number;
  public ylabelEnd!: number;
  public ylabelInterval!: number;

  public isButtonDisabled = true;
  private alertElement!: HTMLElement;

  constructor(private matrixService: MatrixService) {}

  ngOnInit(): void {
    this.alertElement = document.getElementById('alert-container')!;
  }

  public createMatrix(): void {
    const xlabels: number[] = this.createLabelsFromInterval(
      this.xlabelStart,
      this.xlabelEnd,
      this.xlabelInterval
    );
    const ylabels: number[] = this.createLabelsFromInterval(
      this.ylabelStart,
      this.ylabelEnd,
      this.ylabelInterval
    );

    // Obtener todas las matrices existentes
    this.matrixService.getMatrices().subscribe((matrices) => {
      // Verificar si ya existe una matriz con el mismo nombre
      const index = matrices.findIndex((matrix) => matrix.name === this.matrixName);
      if (index !== -1) {
        const message = 'Ya existe una matriz con este nombre.';
        this.showAlert('warning', message);
        return;
      }

      try {
        this.matrixService.createMatrix(this.matrixName, xlabels, ylabels).subscribe((data) => {
          console.log(data);
          this.matrixService.updateMatrices();
          this.showAlert('success', 'Matriz creada exitosamente.');
        });
      } catch (error: any) {
        console.log(error);
        this.showAlert('danger', error.message);
        return;
      }

      this.matrixName = '';
      this.xlabel = '';
      this.ylabel = '';
      this.isButtonDisabled = true;
    });
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

  public onInputChange(): void {
    if (
      this.matrixName &&
      this.xlabelStart !== undefined &&
      this.xlabelEnd !== undefined &&
      this.xlabelInterval &&
      this.ylabelStart !== undefined &&
      this.ylabelEnd !== undefined &&
      this.ylabelInterval
    ) {
      this.isButtonDisabled = false;
    } else {
      this.isButtonDisabled = true;
    }
  }

  public limitDecimals(event: any, inputName: string): void {
    const input = event.target;
    const value = input.value;

    // Validates that the entered value has a maximum of two decimals.
    if (value.includes('.') && value.split('.')[1].length > 2) {
      input.value = value.slice(0, value.lastIndexOf('.') + 3);
      this.showAlert('danger', `Solo se permiten máximo dos decimales en ${inputName}`);
    }
  }

  private createLabelsFromInterval(
    startValue: number,
    endValue: number,
    interval: number
  ): number[] {
    startValue = this.fixPrecision(startValue);
    endValue = this.fixPrecision(endValue);
    interval = this.fixPrecision(interval);

    if (endValue <= startValue) {
      const message = 'El valor final debe ser mayor al valor inicial';
      this.showAlert('danger', message);
      return [];
    }

    let diff = this.fixPrecision(endValue - startValue);
    if (!Number.isInteger(this.fixPrecision(diff / interval))) {
      const message = 'El intervalo tiene que ser un divisor de la diferencia entre el valor inicial y el valor final';
      this.showAlert('danger', message);
      return [];
    }

    let labels: number[] = [];
    for (let i = startValue; i <= endValue; i = this.fixPrecision(i + interval)) {
      labels.push(i);
    }

    return labels;
  }

  public fixPrecision(num: number): number {
    return Number.parseFloat(num.toPrecision(3));
  }
}
