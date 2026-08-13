import { useSearchParams } from "react-router";
import ListComponent from "../../components/todo/listComponent";

function ListPage() {

    const [queryParms] = useSearchParams();

    const page : string | null = queryParms.get("page");
    const size : string | null = queryParms.get("size");

    return (  

    <div className="bg-white w-full">
      <div className="text-4xl">Todo List {page}   {size}</div>
      <ListComponent></ListComponent> 
    </div>

    );
}

export default ListPage;