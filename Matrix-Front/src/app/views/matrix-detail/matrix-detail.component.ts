import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Matrix } from 'src/app/interfaces/matrix';
import { MatrixService } from 'src/app/services/matrix.service';

@Component({
  selector: 'app-matrix-detail',
  templateUrl: './matrix-detail.component.html',
  styleUrls: ['./matrix-detail.component.scss'],
})
export class MatrixDetailComponent implements OnInit {
  private id!: number;
  public inputsData: number[][] = [];
  public matrix!: Matrix;

  constructor(private matrixService: MatrixService, private route: ActivatedRoute,private router: Router) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id')!);

    this.matrixService.getMatrix(this.id).subscribe(
      (data) => {
        console.log(data);
        this.matrix = data;
        this.inputsData = data.data;
        console.log();
      },
      (error) => {
        console.log(error);
        if (error.status === 400) {
          this.router.navigate(['/PageNotFound']);
        }
      }
    );
  }
}
