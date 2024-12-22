import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../common/product';


//service product/product.service
@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  product: Product=new Product();

  constructor(private produtcService:ProductService,private route:ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(()=> {this.handleProductDetails();})
  }
  handleProductDetails()
  {
    const theProductId: number=+this.route.snapshot.paramMap.get('id')!;
    this.produtcService.getProduct(theProductId).subscribe(
      data=>{
        this.product=data;
      }
    )
  }

}