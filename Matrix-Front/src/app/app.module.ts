import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';
import { NgxPaginationModule } from 'ngx-pagination';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './views/home/home.component';
import { PageNotFoundComponent } from './views/page-not-found/page-not-found.component';
import { IncreaseStockComponent } from './components/increase-stock/increase-stock.component';
import { ReduceStockComponent } from './components/reduce-stock/reduce-stock.component';
import { CreateMatrixComponent } from './components/create-matrix/create-matrix.component';
import { ListOfMatricesComponent } from './components/list-of-matrices/list-of-matrices.component';
import { MatrixDetailComponent } from './views/matrix-detail/matrix-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    PageNotFoundComponent,
    IncreaseStockComponent,
    ReduceStockComponent,
    CreateMatrixComponent,
    ListOfMatricesComponent,
    MatrixDetailComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgxPaginationModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
