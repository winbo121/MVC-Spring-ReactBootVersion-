import {lazy, Suspense} from "react";
import { Navigate } from "react-router";
import { loadProducts } from "../pages/products/listPage";
import { loadProduct } from "../pages/products/readPage";

const Loading = () =>  <div>Products Loading....</div>
const ProductsIndex = lazy(() => import("../pages/products/indexPage"))
const ProductsList = lazy(() => import("../pages/products/listPage"))
const ProductsAdd = lazy(() => import("../pages/products/addPage"))
const ProductRead = lazy(() => import("../pages/products/readPage"))
const ProductModify = lazy(() => import("../pages/products/modifyPage"))
export default function productsRouter(){
    return(
        {
            path:"products",
            Component : ProductsIndex,
            children : [
                {
                    path: "", //products/ 로 그냥 했을때 리스트를 바로 뜰수 있도록 하기
                    element: <Navigate to={'/products/list'}></Navigate>,
                    loader:loadProducts
                },
                {
                    path:"list",
                    element : <Suspense fallback={<Loading/>}><ProductsList/></Suspense>,
                    loader:loadProducts
                },
                {
                    path:"add",
                    element : <Suspense fallback={<Loading/>}><ProductsAdd/></Suspense>,
                },
                {
                    path:"read/:pno",
                    element : <Suspense fallback={<Loading/>}><ProductRead/></Suspense>,
                    loader:loadProduct
                },
                {
                    path:"modify/:pno",
                    element:<Suspense fallback={<Loading/>}><ProductModify/></Suspense>,
                    loader:loadProduct
                }                
            ]

        }
    )

}