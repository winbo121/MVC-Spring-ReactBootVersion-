import {lazy, Suspense} from "react";
import { Navigate } from "react-router";
import { loadProducts } from "../pages/products/listPage";

const Loading = () =>  <div>Products Loading....</div>
const ProductsIndex = lazy(() => import("../pages/products/indexPage"))
const ProductsList = lazy(() => import("../pages/products/listPage"))
const ProductsAdd = lazy(() => import("../pages/products/addPage"))

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
                }
            ]

        }
    )

}