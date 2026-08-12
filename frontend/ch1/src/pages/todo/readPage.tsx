import { useParams } from "react-router";
import ReadComponent from "../../components/todo/readComponent";

function   ReadPage(){

    const {tno} = useParams();


    return ( 
    <div className="font-extrabold w-full bg-white mt-6">
      <div className="text-2xl "> Todo Read Page Component {tno} </div> 
      <ReadComponent tno={Number(tno)}></ReadComponent> 
    </div>

     );
}

export default ReadPage;