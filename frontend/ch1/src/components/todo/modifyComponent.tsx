import { useEffect, useState, type ChangeEvent } from "react"
import { deleteOne, getOne, putOne } from "../../api/todoApi";
import type { TodoModify } from "../../types/todo";
import useCustomMove from "../../types/hooks/useCustomMove";
import ResultModalComponent from "../common/resultModalComponent";

const initState = {tno:0 , title :'' , writer:'', dueDate :null, complete : false}

const ModifyConponent = ({tno}:{tno:number}) => {

    const [todo,setTodo] = useState({...initState})

    const {moveToList, moveToRead} = useCustomMove()

    const [result, setResult] = useState<string | null>(null);

    //들어오자마자 시작
    useEffect(() => {
        getOne(tno).then(data => setTodo(data))
    },[tno]);


    //입력할시 이벤트 (처음에는 getOne로인해 조회값에서 계속 바뀜)
    const handleChangeTodo = (e:ChangeEvent<HTMLInputElement>) =>{

        const{name,value} = e.target;
        
        console.log("name:"+name);
        console.log("value:"+value);

        //입력한것을 todo에  name,value로 적용 과정
        setTodo((prevState) => ({                   // ② 최신 state 기준으로
            ...prevState,                           // ③ 기존 값 복사
            [name]: value                           // ④ 해당 필드만 새 값으로 교체
        }))

    }

    //COMPLETE SELECT 박스 이벤트
    const handleChangeTodoComplete = (e:ChangeEvent<HTMLSelectElement>) => {

        const value = e.target.value
        
        todo.complete = (value === 'Y') // true or false

        console.log(todo.complete);

        setTodo({...todo})
    }

    //수정버튼 클릭
    const handleClickModify = () => {

        const todoModify:TodoModify = {tno: todo.tno, writer:todo.writer, title:todo.title, dueDate: todo.dueDate, complete: todo.complete}

        putOne(todoModify).then(data => {
            console.log("modify result: " + data)
            setResult('Modified') 
        })

    }

    //삭제버튼 클릭
    const handleClickDelete = () => {
        
        deleteOne(tno).then( data => {
           console.log("delete result: " + data)
           setResult('Deleted')
        })
    }

    //모달 창이 close될때 
    const closeModal = () => {
        if(result ==='Deleted') {
          moveToList()
        }else {
          moveToRead(tno)
        }
    }


    return (
        <div className = "border-2 border-sky-200 mt-10 m-2 p-4"> 
            {result && <ResultModalComponent title={'처리결과'} content={result} callbackFn={closeModal}></ResultModalComponent>}
          <div className="flex justify-center mt-10">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
              <div className="w-1/5 p-6 text-right font-bold">TNO</div>
              <div className="w-4/5 p-6 rounded-r border border-solid shadow-md bg-gray-100">
                {todo.tno}        
              </div>  
            </div>
          </div>
          <div className="flex justify-center">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
              <div className="w-1/5 p-6 text-right font-bold">WRITER</div>
              <div className="w-4/5 p-6 rounded-r border border-solid shadow-md bg-gray-100">
                {todo.writer}        
              </div>
        
            </div>
          </div>
          <div className="flex justify-center">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
              <div className="w-1/5 p-6 text-right font-bold">TITLE</div>
              <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md" 
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
              <div className="w-1/5 p-6 text-right font-bold">DUEDATE</div>
              <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md" 
              name="dueDate"
              type={'date'} 
              value={todo.dueDate || ''}
              onChange={handleChangeTodo}
              >
              </input>
            </div>
          </div>
          <div className="flex justify-center">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
              <div className="w-1/5 p-6 text-right font-bold">COMPLETE</div>
              <select
                name="status" 
                className="border-solid border-2 rounded m-1 p-2"
                onChange={handleChangeTodoComplete} 
                value = {todo.complete? 'Y':'N'} >
                <option value='Y'>Completed</option>
                <option value='N'>Not Yet</option>
              </select>
            </div>
          </div>
        
          <div className="flex justify-end p-4">
            <button type="button" 
              className="inline-block rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
            onClick={handleClickDelete}
            >
              Delete
            </button>
            <button type="button" 
              className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
            onClick={handleClickModify}
            >
              Modify
            </button>  
          </div>
        </div>
    );

}

export default ModifyConponent;