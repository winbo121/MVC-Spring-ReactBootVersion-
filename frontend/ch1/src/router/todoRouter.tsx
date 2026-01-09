import {  lazy,Suspense } from "react";

const Loading = () => <div>Loading......</div>
const TodoIndex = lazy(() => import("../pages/todo/indexPage"))
const TodoList = lazy(() => import("../pages/todo/listPage"))
const TodoRead = lazy(() => import("../pages/todo/readPage"))


const todoRouter = () => {

    return(
        {
            path : 'todo',
            Component : TodoIndex, //해당 페이지에서 Outlet을 사용하면 Component를 만들어야한다.
            children : [ //해당 페이지에서 Outlet에 들어갈 췰드런들을 만드는 작업
                {
                    path: 'list',
                    element : <Suspense fallback={<Loading/>}><TodoList/></Suspense>,
                },
                {
                    path: 'read/:tno',
                    element : <Suspense fallback={<Loading/>}><TodoRead/></Suspense>,
                }
                
                
            ]
        }
    
    )
}
export default todoRouter