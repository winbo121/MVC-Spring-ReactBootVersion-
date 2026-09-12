import axios from "axios"
import { useActionState } from "react"
import PendingModal from "../common/pendingModal"
import ResultModalComponent from "../common/resultModalComponent"
import useCustomMove from "../../types/hooks/useCustomMove"
import type { UseCustomMoveReturn } from "../../types/global"

interface ProcductAddResult{
    result : string,
    error ? : string
}

const initState : ProcductAddResult = {
    result: "fail"
}

const addAsyncAction = async(state:ProcductAddResult, formData:FormData) =>{
    console.log("addAsyncAction.......")

    await new Promise(resolve => setTimeout(resolve,2000))

    const pname = formData.get("pname") as string

    if(!pname){
        return {result:"fail" ,error : "Insert Product Name"}
    }

    const res = await axios.post('http://localhost:8080/api/products/register',formData)

    return {result:res.data.result}
}

const AddComponent = () => {
    const {moveToList}: UseCustomMoveReturn = useCustomMove()
    const [state, action, isPending] = useActionState(addAsyncAction, initState)

    const closeModal = (): void => {
        //리스트 페이지 이동 ex) moveToList({page:1,size:10});
        moveToList()
    }

return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
            {isPending && <PendingModal/>}
            {state.result != 'fail' && 
                <ResultModalComponent
                title="상품 추가 결과"
                content={`새로운 상품 추가됨`}
                callbackFn={closeModal}/>
            }
        <form action={action}>
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Product Name</div>
                    <input
                        className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                        name="pname"
                        required
                    />
                </div>
            </div>

            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Desc</div>
                    <textarea
                        className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
                        name="pdesc"
                        rows={4}
                        required
                    />
                </div>
            </div>

            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Price</div>
                    <input
                        className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                        name="price"
                        type="number"
                        required
                    />
                </div>
            </div>

            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Files</div>
                    <input
                        className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                        type="file"
                        name="files"
                        multiple={true}
                    />
                </div>
            </div>

            <div className="flex justify-end">
                <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
                    <button
                        type="submit"
                        className="rounded p-4 w-36 bg-blue-500 text-xl text-white"
                    >
                        ADD
                    </button>
                </div>
            </div>
        </form>
    </div>
);
}
export default AddComponent;