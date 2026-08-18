import {   useState, type ChangeEvent } from "react";
import type { TodoAdd } from "../../types/todo";
import type { UseCustomMoveReturn } from "../../types/global";
import useCustomMove from "../../types/hooks/useCustomMove";
import { postAdd } from "../../api/todoApi";
import ResultModalComponent from "../common/resultModalComponent";




const initState:TodoAdd = {
    title:'',
    writer:'',
    dueDate:''
}


function AddComponent(){

    const[todo , setTodo] = useState<TodoAdd>({...initState})

    const[result,setResult] = useState<string | null>(null);

    const {moveToList}:UseCustomMoveReturn = useCustomMove()

    //입력할시 이벤트 (계속 바뀜)
    const handleChangeTodo = (e: ChangeEvent<HTMLInputElement>) => {
        
        const { name, value } = e.target;           // ① 어떤 input인지, 뭐가 입력됐는지

        console.log("name: "+name);
        console.log("value: "+value);

        //입력한것을 todo에  name,value로 적용 과정
        setTodo((prevState) => ({                   // ② 최신 state 기준으로
            ...prevState,                           // ③ 기존 값 복사
            [name]: value                           // ④ 해당 필드만 새 값으로 교체
        }))

    }

    //저장 버튼 클릭
    const handleClickAdd = () : void => {
        
        postAdd(todo).then(result =>{
            console.log(result);

            //모달에 보낼 데이터
            setResult(result.Result);

            setTodo({...initState})
        }).catch(e => {
            console.error(e);
        })
    }

    const closeModal = () : void => {

        //모달값 초기화
        setResult(null);

        //리스트 페이지 이동 ex) moveToList({page:1,size:10});
        moveToList();
    }

    return(

    <div className = "border-2 border-sky-200 mt-10 m-2 p-4"> 
        {result && <ResultModalComponent title={"등록처리 완료"} content={result} callbackFn={closeModal} />}
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">TITLE</div>
          <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md" 
            name="title"
            type={'text'} 
            value={todo.title}
            onChange={handleChangeTodo}
            >
            </input>
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">WRITER</div>
          <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md" 
            name="writer"
            type={'text'} 
            value={todo.writer}
            onChange={handleChangeTodo}
            >
            </input>
        </div>  
      </div>
 <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">DUEDATE</div>
          <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md" 
            name="dueDate"
            type={'date'} 
            value={todo.dueDate}
            onChange={handleChangeTodo}
            >
            </input>
        </div>
      </div>
      <div className="flex justify-end">
        <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
          <button type="button" 
          className="rounded p-4 w-36 bg-blue-500 text-xl  text-white "
          onClick={handleClickAdd}          
          >
          ADD
          </button>
        </div>
      </div>
    </div>

    )

}

export default AddComponent;