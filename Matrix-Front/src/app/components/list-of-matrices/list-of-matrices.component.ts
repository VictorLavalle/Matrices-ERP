import { Component, OnInit } from '@angular/core';
import { Matrix } from '../../interfaces/matrix';
import { MatrixService } from 'src/app/services/matrix.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
@Component({
  selector: 'app-list-of-matrices',
  templateUrl: './list-of-matrices.component.html',
  styleUrls: ['./list-of-matrices.component.scss'],
})
export class ListOfMatricesComponent implements OnInit {
  pageNumber: number = 1;
  shouldNavigate: boolean = false;
  public matrices: Matrix[] = [];

  constructor(private matrixService: MatrixService, private router: Router) {
    this.matrixService.getMatrices().subscribe((matrices) => (this.matrices = matrices));
  }

  ngOnInit(): void {
    this.matrixService.getMatricesObservable().subscribe((matrices) => (this.matrices = matrices));
  }

  public deleteMatrix(id: any): void {
    console.log(id);
    Swal.fire({
      title: '¿Estás seguro de que deseas eliminar el elemento?',
      text: 'Esta acción no se puede deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.matrixService.deleteMatrix(id).subscribe((data) => {
          console.log(data);
          this.matrixService.getMatrices().subscribe((data) => {
            this.matrices = data;
          });
        });
      }
    });
  }

  public onRowClick(event: any, matrix: Matrix) {
    if (event.target.id !== 'deleteButton') {
      this.router.navigate(['/details', matrix.id]);
    }
  }
}
