import { createSearchParams, type LoaderFunctionArgs } from "react-router";
import AddComponent from "../../components/products/addComponent";
import axios from "axios";

export async function loadProducts({request}:LoaderFunctionArgs){
    const url = new URL(request.url)
    const page = url.searchParams.get('page') || "1";
    const size = url.searchParams.get('size') || "2";

    const queryStr = createSearchParams({page,size}).toString()
    const res = await axios.get(`http://localhost:8080/api/products/list?${queryStr}`)
    
    return res.data
}

const AddPage = () => {
    return ( 
        <div className="p-4 w-full bg-white">
            <div className="text-3xl font-extrabold">
                Products Add Page 
            </div>
            <AddComponent/>
        </div>
    );
}
export default AddPage;
