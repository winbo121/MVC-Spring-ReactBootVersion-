import { createBrowserRouter } from "react-router";

import { lazy, Suspense} from "react";
import BasicLayout from "../layouts/basicLayout";
import TodoRouter from "./todoRouter";


const Loading = () => <div>Loading......</div>
const Main = lazy(() => import("../pages/mainPage"))
const About = lazy(() => import("../pages/aboutPage"))

const router = createBrowserRouter([
    {
        path:  "/" ,
        Component: BasicLayout, //해당 페이지에서 Outlet을 사용하면 Component를 만들어야한다.
        children :[ //해당 페이지에서 Outlet에 들어갈 췰드런들을 만드는 작업
            {
                index : true,
                element : <Suspense fallback={<Loading/>}><Main/></Suspense>,
            },
            {
                path : "about",
                element : <Suspense fallback={<Loading/>}><About/></Suspense>,
            },
            TodoRouter()
        ],
    }
])

export default router