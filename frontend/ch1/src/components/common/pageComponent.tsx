import type { PageComponentProps } from "../../types/global";



const PageComponent =({listData, movePage}: PageComponentProps<any>) =>{


    return (
    <div className="m-6 flex justify-center">
        {listData.prev ? 
      <div 
      className="m-2 p-2 w-16 text-center  font-bold text-blue-400 "
      onClick={() => movePage({page:listData.prevPage} )}>
      Prev </div> : <></>}  
        {listData.pageNumList.map(pageNum => 
      <div 
      key={pageNum}
      className={ `m-2 p-2 w-12  text-center rounded shadow-md text-white ${listData.current === pageNum? 'bg-gray-500':'bg-blue-400'}`}
      onClick={() => movePage( {page:pageNum})}>
      {pageNum}
      </div>
         )}
       {listData.next ? 
      <div 
      className="m-2 p-2 w-16 text-center font-bold text-blue-400"
      onClick={() => movePage( {page:listData.nextPage})}> 
      Next 
      </div> : <></>}  
    </div>   

    )

}

export default PageComponent;