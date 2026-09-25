import axios from "axios";
import { useActionState, useEffect, useMemo, useRef, useState, type ChangeEvent, type MouseEvent } from "react";
import type { ProductDTO } from "../../types/product";
import useCustomMove from "../../types/hooks/useCustomMove";
import PendingModal from "../common/pendingModal";
import ResultModalComponent from "../common/resultModalComponent";
import type { UseCustomMoveReturn } from "../../types/global";

// 수정 / 상품삭제 후 서버가 돌려준 결과
interface ProductTaskResult {
    actionType: string; // "modify" | "delete"
    result: string;
    error?: string;
}

const initState: ProductTaskResult = {
    actionType: "modify",
    result: "",
};

// 폼 제출 시 실행. actionType으로 상품 수정 PUT / 상품 삭제 DELETE를 나눔
const modifyDeleteAsyncAction = async (
    _state: ProductTaskResult,
    formData: FormData
): Promise<ProductTaskResult> => {
    const pno = formData.get("pno");
    const actionType = formData.get("actionType") as string;
    let res;

    if (actionType === "modify") {
        // 화면에서 남긴 기존 파일명(uploadFileNames) + 새로 고른 파일(files)을 같이 보냄
        res = await axios.put(`http://localhost:8080/api/products/${pno}`, formData);
    } else if (actionType === "delete") {
        res = await axios.delete(`http://localhost:8080/api/products/${pno}`);
    }

    return { actionType, result: res?.data?.Result };
};

function ModifyComponent({ product }: { product: ProductDTO }) {
    const { moveToRead, moveToList }: UseCustomMoveReturn = useCustomMove();
    const fileInputRef = useRef<HTMLInputElement>(null);

    // 이미 저장된 이미지. DELETE 누르면 여기서만 빠지고, Modify 눌러야 서버에 반영됨
    const [images, setImages] = useState<string[]>([...(product.uploadFileNames ?? [])]);
    // 방금 파일 선택창에서 고른 새 파일. 아직 서버에 안 올라간 상태
    const [newFiles, setNewFiles] = useState<File[]>([]);
    const [state, action, isPending] = useActionState(modifyDeleteAsyncAction, initState);

    // 새로 고른 파일을 <img>로 보여주기 위한 브라우저 임시 URL
    const newImageUrls = useMemo(
        () => newFiles.map((file) => URL.createObjectURL(file)),
        [newFiles]
    );

    useEffect(() => {
        return () => {
            newImageUrls.forEach((url) => URL.revokeObjectURL(url));
        };
    }, [newImageUrls]);

    // 미리보기에서 파일을 빼면, 실제 <input type="file"> 내용도 같이 맞춰줌
    // (안 맞추면 화면에서는 지웠는데 제출할 때 파일이 다시 따라감)
    const syncFileInput = (files: File[]) => {
        if (!fileInputRef.current) {
            return;
        }
        const dataTransfer = new DataTransfer();
        files.forEach((file) => dataTransfer.items.add(file));
        fileInputRef.current.files = dataTransfer.files;
    };

    // 기존 이미지 삭제: 화면 목록에서만 제거. 폼 제출이 되면 안 되어서 type="button"
    const deleteOldImages = (event: MouseEvent<HTMLButtonElement>, target: string) => {
        event.preventDefault();
        event.stopPropagation();
        setImages((prev) => prev.filter((img) => img !== target));
    };

    const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
        const selected = event.target.files;
        const next = selected ? Array.from(selected) : [];
        setNewFiles(next);
    };

    // 새로 고른 파일 미리보기 삭제. input.files도 같이 갱신
    const deleteNewImage = (event: MouseEvent<HTMLButtonElement>, index: number) => {
        event.preventDefault();
        event.stopPropagation();
        const next = newFiles.filter((_, i) => i !== index);
        setNewFiles(next);
        syncFileInput(next);
    };

    const closeModal = () => {
        if (state.actionType === "modify") {
            moveToRead(product.pno);
        }
        if (state.actionType === "delete") {
            moveToList();
        }
    };

    return (
        <div className="border-2 border-sky-200 mt-10 m-2 p-4 bg-white">
            {isPending && <PendingModal />}
            {state.result && (
                <ResultModalComponent
                    title="처리완료"
                    content="처리 완료"
                    callbackFn={closeModal}
                />
            )}

            <form action={action}>
                <div className="flex justify-center mt-10">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">PNO</div>
                        <input
                            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md bg-gray-100"
                            name="pno"
                            required
                            readOnly
                            defaultValue={product.pno}
                        />
                    </div>
                </div>

                <div className="flex justify-center">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">PNAME</div>
                        <input
                            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                            name="pname"
                            required
                            defaultValue={product.pname}
                        />
                    </div>
                </div>

                <div className="flex justify-center">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">PRICE</div>
                        <input
                            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                            name="price"
                            type="number"
                            defaultValue={product.price}
                        />
                    </div>
                </div>

                <div className="flex justify-center">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">PDESC</div>
                        <textarea
                            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
                            name="pdesc"
                            rows={4}
                            required
                            defaultValue={product.pdesc}
                        />
                    </div>
                </div>

                <div className="flex justify-center">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">Files</div>
                        <input
                            ref={fileInputRef}
                            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                            type="file"
                            name="files"
                            multiple={true}
                            accept="image/*"
                            onChange={handleFileChange}
                        />
                    </div>
                </div>

                <div className="w-full justify-center flex flex-col m-auto items-center">
                    {/* 기존 이미지. hidden uploadFileNames가 "아직 남긴 파일" 목록 */}
                    {images.map((imgFile, i) => (
                        <div className="flex justify-center flex-col w-1/3" key={`old-${imgFile}-${i}`}>
                            <button
                                type="button"
                                className="bg-blue-500 text-3xl text-white"
                                onClick={(event) => deleteOldImages(event, imgFile)}
                            >
                                DELETE
                            </button>
                            <img
                                alt="img"
                                src={`http://localhost:8080/api/products/view/${imgFile}`}
                            />
                            {/* Modify 누르면 이 값들이 서버로 감. DELETE한 이미지는 여기 없음 */}
                            <input type="hidden" name="uploadFileNames" value={imgFile} />
                        </div>
                    ))}

                    {/* 새로 고른 파일 미리보기. 실제 파일은 위 files input으로 전송 */}
                    {newImageUrls.map((url, i) => (
                        <div className="flex justify-center flex-col w-1/3" key={`new-${url}`}>
                            <button
                                type="button"
                                className="bg-orange-500 text-3xl text-white"
                                onClick={(event) => deleteNewImage(event, i)}
                            >
                                DELETE
                            </button>
                            <img alt="new-img" src={url} />
                        </div>
                    ))}
                </div>

                <div className="flex justify-end p-4">
                    {/* type="button"이라 폼이 안 넘어감. 상품 전체 삭제만 따로 호출 */}
                    <button
                        type="button"
                        className="rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
                        onClick={() => {
                            const formData = new FormData();
                            formData.set("pno", String(product.pno));
                            formData.set("actionType", "delete");
                            action(formData);
                        }}
                    >
                        Delete
                    </button>
                    {/* 상품 정보 + 남은 기존파일명 + 새 파일을 서버로 저장 */}
                    <button
                        type="submit"
                        name="actionType"
                        value="modify"
                        className="inline-block rounded p-4 m-2 text-xl w-32 text-white bg-orange-500"
                    >
                        Modify
                    </button>
                    <button
                        type="button"
                        className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
                        onClick={() => moveToList()}
                    >
                        List
                    </button>
                </div>
            </form>
        </div>
    );
}

export default ModifyComponent;
