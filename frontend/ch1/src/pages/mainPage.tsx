import { NavLink } from "react-router";


function MainPage() {
    return ( 
    <div className=" text-3xl">
      <div>Main Page</div>
      <div className="flex">
        <NavLink to='/about'>About</NavLink>           
      </div>
    </div>
     );
}

export default MainPage;