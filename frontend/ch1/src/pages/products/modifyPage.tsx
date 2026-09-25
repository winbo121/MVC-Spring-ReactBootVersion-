import { useLoaderData } from "react-router";
import type { ProductDTO } from "../../types/product";
import ModifyComponent from "../../components/products/modifyComponent";

function ModifyPage() {
    const product: ProductDTO = useLoaderData();

    return (
        <div className="p-4 w-full bg-white">
            <div className="text-3xl font-extrabold">
                Products Modify Page
            </div>
            <ModifyComponent product={product} />
        </div>
    );
}

export default ModifyPage;